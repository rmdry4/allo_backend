package com.ramonda.allo_backend.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public class Expense {

    private String id;
    private String groupId;
    private String paidBy;
    private BigDecimal amount;
    private BigDecimal serviceChargePercentage;
    private BigDecimal serviceChargeAmount;
    private BigDecimal totalAmount;
    private String description;
    private String category; 
    private SplitType splitType;
    private Map<String, BigDecimal> splitDetail;
    private Instant createdAt;

    public Expense(String id, String groupId, String paidBy, BigDecimal amount, BigDecimal serviceChargePercentage,
            BigDecimal serviceChargeAmount, BigDecimal totalAmount, String description, String category,
            SplitType splitType, Map<String, BigDecimal> splitDetail, Instant createdAt) {
        this.id = id;
        this.groupId = groupId;
        this.paidBy = paidBy;
        this.amount = amount;
        this.serviceChargePercentage = serviceChargePercentage;
        this.serviceChargeAmount = serviceChargeAmount;
        this.totalAmount = totalAmount;
        this.description = description;
        this.category = category;
        this.splitType = splitType;
        this.splitDetail = splitDetail;
        this.createdAt = createdAt;
    }
    
    public String getId() { 
        return id; 
    }

    public String getGroupId() { 
        return groupId; 
    }

    public String getPaidBy() { 
        return paidBy; 
    }

    public BigDecimal getAmount() { 
        return amount; 
    }

    public BigDecimal getServiceChargePercentage() {
        return serviceChargePercentage;
    }

    public BigDecimal getServiceChargeAmount() {
        return serviceChargeAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getDescription() { 
        return description; 
    }

    public String getCategory() { 
        return category; 
    }

    public SplitType getSplitType() { 
        return splitType; 
    }

    public Map<String, BigDecimal> getSplitDetail() { 
        return splitDetail; 
    }

    public Instant getCreatedAt() { 
        return createdAt; 
    }

    public void setId(String id) { 
        this.id = id;
    }

    public void setGroupId(String groupId) { 
        this.groupId = groupId;
    }

    public void setPaidBy(String paidBy) { 
        this.paidBy = paidBy; 
    }

    public void setAmount(BigDecimal amount) { 
        this.amount = amount;
    }

    public void setDescription(String description) { 
        this.description = description; 
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSplitType(SplitType splitType) { 
        this.splitType = splitType; 
    }

    public void setSplitDetail(Map<String, BigDecimal> splitDetail) { 
        this.splitDetail = splitDetail; 
    }
    public void setCreatedAt(Instant createdAt) { 
        this.createdAt = createdAt; 
    }

    public void setServiceChargePercentage(BigDecimal serviceChargePercentage) {
        this.serviceChargePercentage = serviceChargePercentage;
    }

    public void setServiceChargeAmount(BigDecimal serviceChargeAmount) {
        this.serviceChargeAmount = serviceChargeAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
