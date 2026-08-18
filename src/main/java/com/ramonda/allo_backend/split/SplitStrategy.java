package com.ramonda.allo_backend.split;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    Map<String, BigDecimal> calculateSplit(
        BigDecimal totalAmount,
        List<String> participantIds,
        Map<String, BigDecimal> splitInput
    );
}
