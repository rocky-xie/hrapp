package top.btmdc.hr.service.mapper;

import static top.btmdc.hr.domain.DispatchReviewAsserts.*;
import static top.btmdc.hr.domain.DispatchReviewTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DispatchReviewMapperTest {

    private DispatchReviewMapper dispatchReviewMapper;

    @BeforeEach
    void setUp() {
        dispatchReviewMapper = new DispatchReviewMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getDispatchReviewSample1();
        var actual = dispatchReviewMapper.toEntity(dispatchReviewMapper.toDto(expected));
        assertDispatchReviewAllPropertiesEquals(expected, actual);
    }
}
