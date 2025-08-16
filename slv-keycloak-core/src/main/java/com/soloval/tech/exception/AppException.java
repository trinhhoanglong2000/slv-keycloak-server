package com.soloval.tech.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AppException extends RuntimeException{
    private Integer statusCode;
    private String messageCode;
    private String message;
    private List<String> detailMessage;
    public AppException(Integer statusCode, String messageCode, String message, List<String> detailMessage) {
        this.statusCode = statusCode;
        this.messageCode = messageCode;
        this.message = message;
        this.detailMessage = detailMessage;
    }
}
