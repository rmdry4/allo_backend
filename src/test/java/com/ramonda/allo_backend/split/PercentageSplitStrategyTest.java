package com.ramonda.allo_backend.split;

import com.ramonda.allo_backend.exception.InvalidSplitException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PercentageSplitStrategyTest {

    private final PercentageSplitStrategy strategy = new PercentageSplitStrategy();

    @Test
    void splitsByGivenPercentages() {
        Map<String, BigDecimal> input = Map.of("a", new BigDecimal("50"), "b", new BigDecimal("50"));

        Map<String, BigDecimal> result = strategy.calculateSplit(
                new BigDecimal("200.00"), List.of("a", "b"), input);

        assertThat(result.get("a")).isEqualByComparingTo("100.00");
        assertThat(result.get("b")).isEqualByComparingTo("100.00");
    }

    @SuppressWarnings("null")
    @Test
    void totalAlwaysMatchesAmountDespiteRounding() {
        // 33.33% / 33.33% / 33.34% of 100 -> must still sum to exactly 100.00
        Map<String, BigDecimal> input = Map.of(
                "a", new BigDecimal("33.33"),
                "b", new BigDecimal("33.33"),
                "c", new BigDecimal("33.34"));

        Map<String, BigDecimal> result = strategy.calculateSplit(
                new BigDecimal("100.00"), List.of("a", "b", "c"), input);

        BigDecimal sum = result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(sum).isEqualByComparingTo("100.00");
    }

    @Test
    void throwsWhenPercentagesDontSumTo100() {
        Map<String, BigDecimal> input = Map.of("a", new BigDecimal("40"), "b", new BigDecimal("50"));

        assertThatThrownBy(() -> strategy.calculateSplit(
                new BigDecimal("100.00"), List.of("a", "b"), input))
                .isInstanceOf(InvalidSplitException.class);
    }
}
