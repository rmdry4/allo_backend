package com.ramonda.allo_backend.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.model.Payment;

@Repository
public class BillGroupRepository {
    private final Map<String, BillGroup> storage = new ConcurrentHashMap<>();

    public BillGroup save(BillGroup group) {
        storage.put(group.getId(), group);
        return group;
    }

    public Optional<BillGroup> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<BillGroup> findAll() {
        return List.copyOf(storage.values());
    }

    public boolean existsById(String id) {
        return storage.containsKey(id);
    }

    public void addExpense(String groupId, Expense expense) {
        storage.computeIfPresent(groupId, (id, group) -> {
            group.addExpense(expense);
            return group;
        });
    }

    public void addPayment(String groupId, Payment payment) {
        storage.computeIfPresent(groupId, (id, group) -> {
            group.addPayment(payment);
            return group;
        });
    }

}
