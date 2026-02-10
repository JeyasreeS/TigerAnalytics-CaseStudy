package com.retail.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDTO {

	private final int status;
    private final String message;
    private final List<String> errors;
    private final LocalDateTime timestamp;

    
    public ErrorResponseDTO(int status, String message) {
        this.status = status;
        this.message = message;
        this.errors = null;
        this.timestamp = LocalDateTime.now();
    }

    
    public ErrorResponseDTO(int status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }
}
