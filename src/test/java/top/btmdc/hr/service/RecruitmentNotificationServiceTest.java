package top.btmdc.hr.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import top.btmdc.hr.domain.NotificationMessage;
import top.btmdc.hr.domain.NotificationTemplate;
import top.btmdc.hr.domain.enumeration.NotificationMessageStatus;
import top.btmdc.hr.domain.enumeration.RecruitmentType;
import top.btmdc.hr.repository.NotificationMessageRepository;
import top.btmdc.hr.repository.NotificationTemplateRepository;
import top.btmdc.hr.repository.RecruitmentApplicationRepository;
import top.btmdc.hr.service.dto.NotificationMessageDTO;
import top.btmdc.hr.service.mapper.NotificationMessageMapper;

@ExtendWith(MockitoExtension.class)
class RecruitmentNotificationServiceTest {

    @Mock
    private NotificationTemplateRepository templates;

    @Mock
    private NotificationMessageRepository messages;

    @Mock
    private RecruitmentApplicationRepository applications;

    @Mock
    private NotificationMessageMapper mapper;

    @Mock
    private MailService mailService;

    private RecruitmentNotificationService service;

    @BeforeEach
    void setUp() {
        service = new RecruitmentNotificationService(templates, messages, applications, mapper, mailService, new ObjectMapper());
    }

    @Test
    void draftRendersAndFreezesTemplateContentUntilConfirmed() {
        when(templates.findById(7L)).thenReturn(Optional.of(template(true)));
        when(messages.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDto(any(NotificationMessage.class))).thenReturn(new NotificationMessageDTO());

        service.createDraft(7L, null, List.of("candidate@example.com"), Map.of("candidateName", "Lin"), "hr-user");

        ArgumentCaptor<NotificationMessage> captor = ArgumentCaptor.forClass(NotificationMessage.class);
        verify(messages).save(captor.capture());
        NotificationMessage draft = captor.getValue();
        assertThat(draft.getStatus()).isEqualTo(NotificationMessageStatus.WAITING_CONFIRMATION);
        assertThat(draft.getFinalSubject()).isEqualTo("Interview invitation for Lin");
        assertThat(draft.getFinalBody()).contains("Hello Lin");
        assertThat(draft.getCreatedBy()).isEqualTo("hr-user");
    }

    @Test
    void confirmationSendsAndRecordsAuditStatus() throws Exception {
        NotificationMessage message = new NotificationMessage()
            .eventType("INTERVIEW_INVITATION")
            .status(NotificationMessageStatus.WAITING_CONFIRMATION)
            .sendMode("EMAIL")
            .finalSubject("Interview")
            .finalBody("Hello")
            .recipientsJson("[\"candidate@example.com\"]")
            .retryCount(0)
            .maxRetries(3)
            .createdBy("hr-user");
        when(messages.findById(11L)).thenReturn(Optional.of(message));
        when(messages.saveAndFlush(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(messages.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(mapper.toDto(any(NotificationMessage.class))).thenAnswer(invocation -> {
            NotificationMessage entity = invocation.getArgument(0);
            NotificationMessageDTO dto = new NotificationMessageDTO();
            dto.setStatus(entity.getStatus());
            return dto;
        });
        when(mailService.sendRecruitmentEmail("candidate@example.com", "Interview", "Hello")).thenReturn(true);

        NotificationMessageDTO result = service.confirmAndSend(11L, "hr-confirming-user");

        assertThat(result.getStatus()).isEqualTo(NotificationMessageStatus.SENT);
        assertThat(message.getConfirmedBy()).isEqualTo("hr-confirming-user");
        assertThat(message.getConfirmedAt()).isNotNull();
        assertThat(message.getSentAt()).isNotNull();
        verify(mailService).sendRecruitmentEmail("candidate@example.com", "Interview", "Hello");
    }

    @Test
    void unpublishedTemplateCannotCreateDraft() {
        when(templates.findById(7L)).thenReturn(Optional.of(template(false)));

        assertThatThrownBy(() -> service.createDraft(7L, null, List.of("candidate@example.com"), Map.of(), "hr-user"))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Only published templates");
        verifyNoInteractions(messages);
    }

    private NotificationTemplate template(boolean published) {
        return new NotificationTemplate()
            .id(7L)
            .eventType("INTERVIEW_INVITATION")
            .recruitmentType(RecruitmentType.GRADUATE)
            .language("en")
            .versionNumber(1)
            .subjectTemplate("Interview invitation for {{candidateName}}")
            .bodyTemplate("Hello {{ candidateName }}")
            .externalSafe(true)
            .published(published);
    }
}
