package com.ramonda.allo_backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ramonda.allo_backend.dto.request.AddExpenseRequest;
import com.ramonda.allo_backend.dto.response.ExpenseResponse;
import com.ramonda.allo_backend.model.Expense;
import com.ramonda.allo_backend.service.BillGroupService;
import com.ramonda.allo_backend.service.ExpenseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups/{groupId}/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final BillGroupService billGroupService;

    public ExpenseController(ExpenseService expenseService, BillGroupService billGroupService) {
        this.expenseService = expenseService;
        this.billGroupService = billGroupService;
    }

    // tambahkan data pengeluaran dari tiap-tiap partisipan berdasarkan group
    @PostMapping
    public ResponseEntity<ExpenseResponse> addExpense(@PathVariable String groupId,
                                                        @Valid @RequestBody AddExpenseRequest request) {
        Expense expense = expenseService.addExpense(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ExpenseResponse.from(expense));
    }

    @GetMapping
    public ResponseEntity<List<ExpenseResponse>> getExpenses(@PathVariable String groupId) {
        List<ExpenseResponse> expenses = billGroupService.getGroup(groupId).getExpenses().stream()
                .map(ExpenseResponse::from)
                .toList();
        return ResponseEntity.ok(expenses);
    }
}
