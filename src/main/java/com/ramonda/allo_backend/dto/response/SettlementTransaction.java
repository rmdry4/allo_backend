package com.ramonda.allo_backend.dto.response;

import java.math.BigDecimal;

/**
 * One recommended transfer: `from` should pay `amount` to `to` to help
 * settle the group's balances. The set of these transactions is the
 * minimal-transaction-count solution produced by SettlementService.
 */
public record SettlementTransaction(String from, String to, BigDecimal amount) {
}
