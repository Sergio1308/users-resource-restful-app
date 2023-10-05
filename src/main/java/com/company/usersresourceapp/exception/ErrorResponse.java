package com.company.usersresourceapp.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {
    private HttpStatus title;
    private int status;

    private LocalDateTime timestamp;

    private String message;
    private String detail;

    private List<String> invalidParams;

    private String instance;

    private ErrorResponse() {
        timestamp = LocalDateTime.now();
    }
    public ErrorResponse(HttpStatus title, String instance) {
        this();
        this.title = title;
        this.status = title.value();
        this.instance = instance;
    }

    public ErrorResponse(HttpStatus title, String detail, String instance) {
        this();
        this.title = title;
        this.status = title.value();
        this.message = "Unexpected error";
        this.detail = detail;
        this.instance = instance;
    }

    public ErrorResponse(HttpStatus title, String message, String detail, String instance) {
        this();
        this.title = title;
        this.status = title.value();
        this.message = message;
        this.detail = detail;
        this.instance = instance;
    }
}
