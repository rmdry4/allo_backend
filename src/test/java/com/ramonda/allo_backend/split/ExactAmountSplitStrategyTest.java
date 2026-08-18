package com.ramonda.allo_backend.split;

import com.ramonda.allo_backend.exception.InvalidSplitException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ExactAmountSplitStrategyTest {

    private final ExactAmountSplitStrategy strategy = new ExactAmountSplitStrategy();

    @Test
    void acceptsExactAmountsThatSumToTotal() {
        Map<String, BigDecimal> input = Map.of("a", new BigDecimal("70.00"), "b", new BigDecimal("30.00"));

        Map<String, BigDecimal> result = strategy.calculateSplit(
                new BigDecimal("100.00"), List.of("a", "b"), input);

        assertThat(result.get("a")).isEqualByComparingTo("70.00");
        assertThat(result.get("b")).isEqualByComparingTo("30.00");
    }

    @Test
    void throwsWhenAmountsDontSumToTotal() {
        Map<String, BigDecimal> input = Map.of("a", new BigDecimal("70.00"), "b", new BigDecimal("40.00"));

        assertThatThrownBy(() -> strategy.calculateSplit(
                new BigDecimal("100.00"), List.of("a", "b"), input))
                .isInstanceOf(InvalidSplitException.class);
    }
}
