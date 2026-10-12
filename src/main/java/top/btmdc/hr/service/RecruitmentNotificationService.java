package top.btmdc.hr.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.domain.NotificationTemplate;
import top.btmdc.hr.domain.RecruitmentApplication;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.repository.NotificationTemplateRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.mapper.NotificationMessageMapper;

/** Creates immutable previews and sends confirmed recruitment notices. */
@Service
@Transactional
public class RecruitmentNotificationService {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{\\{\\s*([A-Za-z0-9_.-]+)\\s*}} ".trim());

    private final NotificationTemplateRepository templateRepository;
    private final NotificationMessageRepository messageRepository;
    private final RecruitmentApplicationRepository applicationRepository;
    private final NotificationMessageMapper mapper;
    private final MailService mailService;
    private final ObjectMapper objectMapper;

    public RecruitmentNotificationService(
        NotificationTemplateRepository templateRepository,
        NotificationMessageRepository messageRepository,
        RecruitmentApplicationRepository applicationRepository,
        NotificationMessageMapper mapper,
        MailService mailService,
        ObjectMapper objectMapper
    ) {
        this.templateRepository = templateRepository;
        this.messageRepository = messageRepository;
        this.applicationRepository = applicationRepository;
        this.mapper = mapper;
        this.mailService = mailService;
        this.objectMapper = objectMapper;
    }

    public NotificationMessageDTO createDraft(
        Long templateId,
        Long applicationId,
        List<String> recipients,
        Map<String, String> variables,
        String actor
    ) {
        if (recipients == null || recipients.isEmpty() || recipients.stream().anyMatch(address -> address == null || address.isBlank())) {
            throw new IllegalArgumentException("At least one recipient is required");
        }
        NotificationTemplate template = templateRepository
            .findById(templateId)
            .orElseThrow(() -> new IllegalArgumentException("Template not found"));
        if (!Boolean.TRUE.equals(template.getPublished())) {
            throw new IllegalArgumentException("Only published templates can be used");
        }
        RecruitmentApplication application =
            applicationId == null
                ? null
                : applicationRepository.findById(applicationId).orElseThrow(() -> new IllegalArgumentException("Application not found"));
        Map<String, String> safeVariables = variables == null ? Map.of() : variables;
        NotificationMessage message = new NotificationMessage()
            .eventType(template.getEventType())
            .templateId(template.getId())
            .status(NotificationMessageStatus.WAITING_CONFIRMATION)
            .sendMode("EMAIL")
            .finalSubject(render(template.getSubjectTemplate(), safeVariables))
            .finalBody(render(template.getBodyTemplate(), safeVariables))
            .recipientsJson(serialize(recipients))
            .retryCount(0)
            .maxRetries(3)
            .createdBy(actor)
            .createdAt(Instant.now())
            .application(application);
        return mapper.toDto(messageRepository.save(message));
    }

    public NotificationMessageDTO confirmAndSend(Long id, String actor) {
        NotificationMessage message = messageRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Notification not found"));
        if (message.getStatus() != NotificationMessageStatus.WAITING_CONFIRMATION) {
            throw new IllegalArgumentException("Only unconfirmed notifications can be sent");
        }
        message.setStatus(NotificationMessageStatus.SENDING);
        message.setConfirmedBy(actor);
        message.setConfirmedAt(Instant.now());
        messageRepository.saveAndFlush(message);
        try {
            List<String> recipients = objectMapper.readValue(
                message.getRecipientsJson(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, String.class)
            );
            int delivered = 0;
            for (String recipient : recipients) {
                if (mailService.sendRecruitmentEmail(recipient, message.getFinalSubject(), message.getFinalBody())) delivered++;
            }
            if (delivered == recipients.size()) {
                message.setStatus(NotificationMessageStatus.SENT);
                message.setSentAt(Instant.now());
            } else {
                message.setRetryCount(message.getRetryCount() + 1);
                message.setStatus(NotificationMessageStatus.FAILED);
                message.setLastError("Delivered to " + delivered + " of " + recipients.size() + " recipients");
            }
        } catch (JsonProcessingException exception) {
            message.setStatus(NotificationMessageStatus.FAILED);
            message.setLastError("Stored recipient list could not be read");
        }
        return mapper.toDto(messageRepository.save(message));
    }

    private String render(String template, Map<String, String> variables) {
        Matcher matcher = PLACEHOLDER.matcher(template == null ? "" : template);
        StringBuilder output = new StringBuilder();
        while (matcher.find()) {
            String value = variables.getOrDefault(matcher.group(1), "");
            matcher.appendReplacement(output, Matcher.quoteReplacement(value));
        }
        matcher.appendTail(output);
        return output.toString();
    }

    private String serialize(List<String> recipients) {
        try {
            return objectMapper.writeValueAsString(recipients);
        } catch (JsonProcessingException exception) {
            throw new IllegalArgumentException("Recipients could not be serialized", exception);
        }
    }
}
