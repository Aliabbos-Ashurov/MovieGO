package com.abbos.moviego.mapper.base;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Aliabbos Ashurov
 * @version 1.0
 * @since 2025-05-23
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { UtilMapperImpl.class })
public class UtilMapperTest {

    @Autowired
    private UtilMapper utilMapper;

    @Test
    void big_decimal_to_formatted_string() {
        final var price = new BigDecimal("100.000");
        String result = utilMapper.bigDecimalToFormattedString(price);
        String resultEmpty = utilMapper.bigDecimalToFormattedString(null);

        assertEquals("100.00", result);
        assertEquals("", resultEmpty);
    }

    @Test
    void enum_to_string() {
        String result = utilMapper.enumToString(TestEnum.ONE);
        String resultNull = utilMapper.enumToString(null);

        assertEquals("ONE", result);
        assertNull(resultNull);
    }

    enum TestEnum {
        ONE, TWO
    }
}