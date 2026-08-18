package com.ramonda.allo_backend.exception;

import java.time.Instant;

public record ErrorResponse(String message, Instant timestamp) {
}
