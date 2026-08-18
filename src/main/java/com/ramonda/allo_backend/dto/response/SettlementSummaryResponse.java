package com.ramonda.allo_backend.dto.response;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record SettlementSummaryResponse(String groupId,
                                         Map<String, BigDecimal> netBalances,
                                         List<SettlementTransaction> transactions) {
}
