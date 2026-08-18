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

import com.ramonda.allo_backend.dto.request.CreateGroupRequest;
import com.ramonda.allo_backend.dto.response.GroupResponse;
import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.service.BillGroupService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/groups")
public class BillGroupController {

    private final BillGroupService billGroupService;

    public BillGroupController(BillGroupService billGroupService) {
        this.billGroupService = billGroupService;
    }

    // create group baru dengan nama group dan list partisipan
    // return-nya berupa nama group, id group, list partisipan beserta id partisipan 
    @PostMapping
    public ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        BillGroup group = billGroupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(GroupResponse.from(group));
    }

    // request data group berdasarkan id group
    // respon data group
    @GetMapping("/{groupId}")
    public ResponseEntity<GroupResponse> getGroup(@PathVariable String groupId) {
        BillGroup group = billGroupService.getGroup(groupId);
        return ResponseEntity.ok(GroupResponse.from(group));
    }

    // request data seluruh group
    // respon data group
    @GetMapping
    public ResponseEntity<List<GroupResponse>> getAllGroups() {
        List<GroupResponse> groups = billGroupService.getAllGroups().stream()
                .map(GroupResponse::from)
                .toList();
        return ResponseEntity.ok(groups);
    }
}

