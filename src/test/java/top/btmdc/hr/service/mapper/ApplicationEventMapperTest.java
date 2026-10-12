package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.ApplicationEventAsserts.*;
import static top.btmdc.hr.domain.ApplicationEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ApplicationEventMapperTest {

    private ApplicationEventMapper applicationEventMapper;

    @BeforeEach
    void setUp() {
        applicationEventMapper = new ApplicationEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getApplicationEventSample1();
        var actual = applicationEventMapper.toEntity(applicationEventMapper.toDto(expected));
        assertApplicationEventAllPropertiesEquals(expected, actual);
    }
}
