package com.ramonda.allo_backend.split;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ramonda.allo_backend.exception.InvalidSplitException;

@Component
public class EqualSplitStrategy implements SplitStrategy {

    @Override
    public Map<String, BigDecimal> calculateSplit(BigDecimal totalAmount,
                                                    List<String> participantIds,
                                                    Map<String, BigDecimal> splitInput) {
        if (participantIds == null || participantIds.isEmpty()) {
            throw new InvalidSplitException("Butuh minimal satu peserta untuk split EQUAL");
        }

        int n = participantIds.size();
        BigDecimal share = totalAmount.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_UP);
        BigDecimal distributed = share.multiply(BigDecimal.valueOf(n));
        BigDecimal remainder = totalAmount.subtract(distributed);

        Map<String, BigDecimal> result = new LinkedHashMap<>();
        for (int i = 0; i < n; i++) {
            BigDecimal amount = share;
            if (i == 0) {
                // Sisa pembulatan (akibat pembagian ganjil) dibebankan ke peserta
                // pertama agar total split selalu sama persis dengan totalAmount.
                amount = amount.add(remainder);
            }
            result.put(participantIds.get(i), amount);
        }
        return result;
    }
}

