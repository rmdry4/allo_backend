package com.ramonda.allo_backend.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ramonda.allo_backend.dto.request.RecordPaymentRequest;
import com.ramonda.allo_backend.dto.response.PaymentResponse;
import com.ramonda.allo_backend.dto.response.SettlementSummaryResponse;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Payment;

import com.ramonda.allo_backend.service.SettlementService;
import com.ramonda.allo_backend.service.BillGroupService;
import com.ramonda.allo_backend.service.PaymentService;

@RestController
@RequestMapping("/api/groups/{groupId}")
public class SettlementController {

    private final SettlementService settlementService;
    private final PaymentService paymentService;
    private final BillGroupService billGroupService;

    public SettlementController(SettlementService settlementService,
                                 PaymentService paymentService,
                                 BillGroupService billGroupService) {
        this.settlementService = settlementService;
        this.paymentService = paymentService;
        this.billGroupService = billGroupService;
    }

    // perhitungan tagihan  berdasarkan id group
    @GetMapping("/settlement")
    public ResponseEntity<SettlementSummaryResponse> getSettlement(@PathVariable String groupId) {
        BillGroup group = billGroupService.getGroup(groupId);
        return ResponseEntity.ok(settlementService.calculateSettlement(group));
    }

    //pembayaran
    @PostMapping("/payments")
    public ResponseEntity<PaymentResponse> recordPayment(@PathVariable String groupId,
                                                           @Valid @RequestBody RecordPaymentRequest request) {
        Payment payment = paymentService.recordPayment(groupId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(PaymentResponse.from(payment));
    }
}
