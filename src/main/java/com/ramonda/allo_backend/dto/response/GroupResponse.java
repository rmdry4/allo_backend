package com.ramonda.allo_backend.dto.response;

import com.ramonda.allo_backend.model.BillGroup;
import com.ramonda.allo_backend.model.Participant;

import java.time.Instant;
import java.util.List;

public record GroupResponse(String id, String name, List<Participant> participants, Instant createdAt) {

    public static GroupResponse from(BillGroup group) {
        return new GroupResponse(group.getId(), group.getName(), group.getParticipants(), group.getCreatedAt());
    }
}
