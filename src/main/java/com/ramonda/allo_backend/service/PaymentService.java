/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.ramonda.allo_backend.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import com.ramonda.allo_backend.dto.request.RecordPaymentRequest;
import com.ramonda.allo_backend.exception.InvalidSplitException;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Payment;
import com.ramonda.allo_backend.repository.BillGroupRepository;

@Service
public class PaymentService {

    private final BillGroupRepository repository;
    private final BillGroupService billGroupService;

    public PaymentService(BillGroupRepository repository,
                           BillGroupService billGroupService) {
        this.repository = repository;
        this.billGroupService = billGroupService;
    }

    public Payment recordPayment(String groupId, RecordPaymentRequest request) {
        BillGroup group = billGroupService.getGroup(groupId);

        if (!group.hasParticipant(request.from())) {
            throw new InvalidSplitException("'from' bukan peserta group ini: " + request.from());
        }
        if (!group.hasParticipant(request.to())) {
            throw new InvalidSplitException("'to' bukan peserta group ini: " + request.to());
        }
        if (request.from().equals(request.to())) {
            throw new InvalidSplitException("'from' dan 'to' tidak boleh sama");
        }

        Payment payment = new Payment(
                UUID.randomUUID().toString(), groupId, request.from(), request.to(),
                request.amount(), Instant.now());

        repository.addPayment(groupId, payment);
       
        return payment;
    }
}
