package com.ramonda.allo_backend.model;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.time.Instant;

public class BillGroup {
    private String id;
    private String name;
    private List<Participant> participants;
    private List<Expense> expenses = new CopyOnWriteArrayList<>();
    private List<Payment> payments = new CopyOnWriteArrayList<>();
    private Instant createdAt;

    // constructor, getters
    
    public BillGroup(String id, String name, List<Participant> participants, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.participants = List.copyOf(participants);
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public void setExpenses(List<Expense> expenses) {
        this.expenses = expenses;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void addExpense(Expense expense) {
        expenses.add(expense);
    }

    public void addPayment(Payment payment) {
        payments.add(payment);
    }

    public boolean hasParticipant(String participantId) {
        return participants.stream().anyMatch(p -> p.id().equals(participantId));
    }
    
}
