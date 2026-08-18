package com.ramonda.allo_backend.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ramonda.allo_backend.exception.InvalidSplitException;

@Component
public class PercentageSplitStrategy implements SplitStrategy {
    @SuppressWarnings("null")
    @Override
    public Map<String, BigDecimal> calculateSplit(
            BigDecimal totalAmount, List<String> participantIds, Map<String, BigDecimal> splitInput) {

        BigDecimal totalPercentage = splitInput.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPercentage.compareTo(BigDecimal.valueOf(100)) != 0) {
            throw new InvalidSplitException("Total percentage harus 100%, saat ini: " + totalPercentage);
        }

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (String pid : participantIds) {
            BigDecimal pct = splitInput.getOrDefault(pid, BigDecimal.ZERO);
            BigDecimal amount = totalAmount.multiply(pct)
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            result.put(pid, amount);
        }
        return result;
    }
}
