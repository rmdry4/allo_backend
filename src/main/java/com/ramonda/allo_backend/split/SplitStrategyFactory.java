package com.ramonda.allo_backend.split;

import java.util.Map;
import org.springframework.stereotype.Component;
import com.ramonda.allo_backend.model.SplitType;

@Component
public class SplitStrategyFactory {
    private final Map<SplitType, SplitStrategy> strategies;

    public SplitStrategyFactory(EqualSplitStrategy equal,
                                 PercentageSplitStrategy percentage,
                                 ExactAmountSplitStrategy exact) {
        this.strategies = Map.of(
            SplitType.EQUAL, equal,
            SplitType.PERCENTAGE, percentage,
            SplitType.EXACT, exact
        );
    }

    public SplitStrategy get(SplitType type) {
        return strategies.get(type);
    }
}
