package com.ramonda.allo_backend.split;

import com.ramonda.allo_backend.exception.InvalidSplitException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EqualSplitStrategyTest {

    private final EqualSplitStrategy strategy = new EqualSplitStrategy();

    @Test
    void splitsEvenlyWhenAmountDividesCleanly() {
        Map<String, BigDecimal> result = strategy.calculateSplit(
                new BigDecimal("90.00"), List.of("a", "b", "c"), Map.of());

        assertThat(result.get("a")).isEqualByComparingTo("30.00");
        assertThat(result.get("b")).isEqualByComparingTo("30.00");
        assertThat(result.get("c")).isEqualByComparingTo("30.00");
    }

    @SuppressWarnings("null")
    @Test
    void firstParticipantAbsorbsRoundingRemainder() {
        // 100 / 3 = 33.33 recurring — total must still equal exactly 100.00
        Map<String, BigDecimal> result = strategy.calculateSplit(
                new BigDecimal("100.00"), List.of("a", "b", "c"), Map.of());

        BigDecimal sum = result.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        assertThat(sum).isEqualByComparingTo("100.00");
        assertThat(result.get("a")).isEqualByComparingTo("33.34"); // absorbs remainder
        assertThat(result.get("b")).isEqualByComparingTo("33.33");
        assertThat(result.get("c")).isEqualByComparingTo("33.33");
    }

    @Test
    void throwsWhenNoParticipants() {
        assertThatThrownBy(() -> strategy.calculateSplit(BigDecimal.TEN, List.of(), Map.of()))
                .isInstanceOf(InvalidSplitException.class);
    }
}
