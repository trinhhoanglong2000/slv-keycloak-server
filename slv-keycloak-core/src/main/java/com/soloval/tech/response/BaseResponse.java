package com.soloval.tech.response;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class BaseResponse {
    private Integer statusCode;

    private String description;

    private String messageCode;

    private String message;

    private String timestamp;

    private List<String> detailMessage;
}
