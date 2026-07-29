package top.btmdc.hr.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static top.btmdc.hr.web.rest.TestUtil.createUpdateProxyForBean;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import top.btmdc.hr.IntegrationTest;
import top.btmdc.hr.domain.ActionItem;
import top.btmdc.hr.domain.enumeration.ActionPriority;
import top.btmdc.hr.domain.enumeration.ActionSourceType;
import top.btmdc.hr.domain.enumeration.ActionStatus;
import top.btmdc.hr.repository.ActionItemRepository;
import top.btmdc.hr.service.ActionItemService;

@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ActionItemResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";
    private static final String DEFAULT_ASSIGNEE = "test-user";
    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.now().plusDays(7);

    private static final String ENTITY_API_URL = "/api/action-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ActionItemRepository actionItemRepository;

    @Autowired
    private ActionItemService actionItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restActionItemMockMvc;

    private ActionItem actionItem;

    private ActionItem insertedActionItem;

    public static ActionItem createEntity() {
        return new ActionItem()
            .description(DEFAULT_DESCRIPTION)
            .assignee(DEFAULT_ASSIGNEE)
            .dueDate(DEFAULT_DUE_DATE)
            .status(ActionStatus.OPEN)
            .priority(ActionPriority.P2_MEDIUM)
            .createdAt(LocalDate.now());
    }

    @BeforeEach
    void initTest() {
        actionItem = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedActionItem != null) {
            actionItemRepository.delete(insertedActionItem);
            insertedActionItem = null;
        }
    }

    @Test
    @Transactional
    void createActionItem() throws Exception {
        long dbSize = actionItemRepository.count();

        restActionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(actionItem)))
            .andExpect(status().isCreated());

        assertThat(actionItemRepository.count()).isEqualTo(dbSize + 1);
        ActionItem saved = actionItemRepository
            .findAll()
            .stream()
            .filter(a -> a.getDescription().equals(DEFAULT_DESCRIPTION))
            .findFirst()
            .orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(ActionStatus.OPEN);
        assertThat(saved.getPriority()).isEqualTo(ActionPriority.P2_MEDIUM);
        assertThat(saved.getCreatedAt()).isNotNull();
        insertedActionItem = saved;
    }

    @Test
    @Transactional
    void createActionItemWithDefaults() throws Exception {
        ActionItem minimal = new ActionItem().description("minimal test");
        restActionItemMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(minimal)))
            .andExpect(status().isCreated());

        ActionItem saved = actionItemRepository
            .findAll()
            .stream()
            .filter(a -> a.getDescription().equals("minimal test"))
            .findFirst()
            .orElse(null);
        assertThat(saved).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(ActionStatus.OPEN);
        assertThat(saved.getPriority()).isEqualTo(ActionPriority.P2_MEDIUM);
        assertThat(saved.getCreatedAt()).isEqualTo(LocalDate.now());
        insertedActionItem = saved;
    }

    @Test
    @Transactional
    void getAllActionItems() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(get(ENTITY_API_URL))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getAllActionItemsByStatusFilter() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(get(ENTITY_API_URL + "?status.equals=OPEN"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));

        restActionItemMockMvc
            .perform(get(ENTITY_API_URL + "?status.equals=COMPLETED"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @Transactional
    void getActionItem() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(get(ENTITY_API_URL_ID, actionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void startActionItem() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(post(ENTITY_API_URL_ID + "/start", actionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    @Transactional
    void completeActionItem() throws Exception {
        actionItem.setStatus(ActionStatus.IN_PROGRESS);
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(post(ENTITY_API_URL_ID + "/complete", actionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("COMPLETED"))
            .andExpect(jsonPath("$.completedAt").isNotEmpty());
    }

    @Test
    @Transactional
    void cancelActionItem() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(post(ENTITY_API_URL_ID + "/cancel", actionItem.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    @Test
    @Transactional
    void startCompletedActionItem_returns400() throws Exception {
        actionItem.setStatus(ActionStatus.COMPLETED);
        actionItem.setCompletedAt(LocalDate.now());
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc.perform(post(ENTITY_API_URL_ID + "/start", actionItem.getId())).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void completeCancelledActionItem_returns400() throws Exception {
        actionItem.setStatus(ActionStatus.CANCELLED);
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc.perform(post(ENTITY_API_URL_ID + "/complete", actionItem.getId())).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void cancelCompletedActionItem_returns400() throws Exception {
        actionItem.setStatus(ActionStatus.COMPLETED);
        actionItem.setCompletedAt(LocalDate.now());
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc.perform(post(ENTITY_API_URL_ID + "/cancel", actionItem.getId())).andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void updateActionItemDoesNotOverwriteStatus() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        ActionItem updated = new ActionItem();
        updated.setId(actionItem.getId());
        updated.setDescription(UPDATED_DESCRIPTION);
        updated.setStatus(ActionStatus.COMPLETED);

        restActionItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, actionItem.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(updated))
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.description").value(UPDATED_DESCRIPTION))
            .andExpect(jsonPath("$.status").value("OPEN"));
    }

    @Test
    @Transactional
    void countOpenItems() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        ActionItem completed = new ActionItem().description("completed item").status(ActionStatus.COMPLETED).completedAt(LocalDate.now());
        actionItemRepository.saveAndFlush(completed);

        restActionItemMockMvc
            .perform(get(ENTITY_API_URL + "/open/count"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value(1));
    }

    @Test
    @Transactional
    void deleteActionItem() throws Exception {
        actionItemRepository.saveAndFlush(actionItem);
        insertedActionItem = actionItem;

        restActionItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, actionItem.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        assertThat(actionItemRepository.findById(actionItem.getId())).isEmpty();
        insertedActionItem = null;
    }

    @Test
    @Transactional
    void createFromSourceWithSameSourceTypeAndIdDoesNotDuplicate() {
        ActionItem first = actionItemMapper(
            actionItemService.createFromSource(
                ActionSourceType.SKILL_REVIEW,
                1001L,
                "PERSON_SKILL",
                "First description",
                "user-1",
                ActionPriority.P2_MEDIUM
            )
        );
        insertedActionItem = actionItemRepository.findById(first.getId()).orElse(null);

        ActionItem second = actionItemMapper(
            actionItemService.createFromSource(ActionSourceType.SKILL_REVIEW, 1001L, "PERSON_SKILL", "Updated description", null, null)
        );

        assertThat(second.getId()).isEqualTo(first.getId());
        assertThat(second.getDescription()).isEqualTo("Updated description");
        assertThat(actionItemRepository.count()).isEqualTo(1);
    }

    @Test
    @Transactional
    void createFromSourceWithCompletedStatusAllowsNew() {
        ActionItem first = actionItemMapper(
            actionItemService.createFromSource(
                ActionSourceType.SKILL_REVIEW,
                2001L,
                "PERSON_SKILL",
                "Old description",
                "user-1",
                ActionPriority.P2_MEDIUM
            )
        );
        actionItemService.complete(first.getId());

        ActionItem second = actionItemMapper(
            actionItemService.createFromSource(
                ActionSourceType.SKILL_REVIEW,
                2001L,
                "PERSON_SKILL",
                "New description after completion",
                null,
                ActionPriority.P1_HIGH
            )
        );

        assertThat(second.getId()).isNotEqualTo(first.getId());
        assertThat(second.getDescription()).isEqualTo("New description after completion");
        assertThat(actionItemRepository.count()).isEqualTo(2);
    }

    private ActionItem actionItemMapper(top.btmdc.hr.service.dto.ActionItemDTO dto) {
        return actionItemRepository.findById(dto.getId()).orElse(null);
    }
}
