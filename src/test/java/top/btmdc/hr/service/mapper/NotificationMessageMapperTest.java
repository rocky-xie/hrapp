package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.NotificationMessageAsserts.*;
import static top.btmdc.hr.domain.NotificationMessageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class NotificationMessageMapperTest {

    private NotificationMessageMapper notificationMessageMapper;

    @BeforeEach
    void setUp() {
        notificationMessageMapper = new NotificationMessageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getNotificationMessageSample1();
        var actual = notificationMessageMapper.toEntity(notificationMessageMapper.toDto(expected));
        assertNotificationMessageAllPropertiesEquals(expected, actual);
    }
}
