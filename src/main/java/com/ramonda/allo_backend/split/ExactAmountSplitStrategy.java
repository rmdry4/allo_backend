package com.ramonda.allo_backend.split;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ramonda.allo_backend.exception.InvalidSplitException;

@Component
public class ExactAmountSplitStrategy implements SplitStrategy {
    @SuppressWarnings("null")
    @Override
    public Map<String, BigDecimal> calculateSplit(
            BigDecimal totalAmount, List<String> participantIds, Map<String, BigDecimal> splitInput) {

        BigDecimal sum = splitInput.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (sum.compareTo(totalAmount) != 0) {
            throw new InvalidSplitException(
                "Jumlah exact split (" + sum + ") tidak sama dengan total expense (" + totalAmount + ")");
        }
        return new LinkedHashMap<>(splitInput);
    }
}
