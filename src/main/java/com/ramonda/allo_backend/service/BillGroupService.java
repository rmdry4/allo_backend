package com.ramonda.allo_backend.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ramonda.allo_backend.dto.request.CreateGroupRequest;
import com.ramonda.allo_backend.exception.ResourceNotFoundException;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Participant;
import com.ramonda.allo_backend.repository.BillGroupRepository;

@Service
public class BillGroupService {
    private final BillGroupRepository repository;

    public BillGroupService(BillGroupRepository repository) {
        this.repository = repository;
    }

    // terima input untuk membuat group baru
    public BillGroup createGroup(CreateGroupRequest request) {
        List<Participant> participants = request.participantNames().stream()
                .map(name -> new Participant(UUID.randomUUID().toString(), name))
                .toList();

        BillGroup group = new BillGroup(
                UUID.randomUUID().toString(), request.name(), participants, Instant.now());

        repository.save(group);
        
        return group;
    }

    // request data group berdasarkan id group
    // respon data group
    public BillGroup getGroup(String groupId) {
        return repository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group tidak ditemukan: " + groupId));
    }

    // request data seluruh group dalam memory
    // respon data group
    public List<BillGroup> getAllGroups() {
        return repository.findAll();
    }
}
