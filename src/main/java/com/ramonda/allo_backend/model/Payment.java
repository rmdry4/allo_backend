package com.ramonda.allo_backend.model;

import java.math.BigDecimal;
import java.time.Instant;

public class Payment {
    private final String id;
    private final String groupId;
    private final String from;
    private final String to;
    private final BigDecimal amount;
    private final Instant paidAt;

    public Payment(String id, String groupId, String from, String to, BigDecimal amount, Instant paidAt) {
        this.id = id;
        this.groupId = groupId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.paidAt = paidAt;
    }

    public String getId() { 
        return id; 
    }

    public String getGroupId() { 
        return groupId; 
    }

    public String getFrom() { 
        return from; 
    }

    public String getTo() { 
        return to; 
    }

    public BigDecimal getAmount() { 
        return amount; 
    }
    
    public Instant getPaidAt() { 
        return paidAt; 
    }
}
