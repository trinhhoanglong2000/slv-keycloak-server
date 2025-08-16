package com.soloval.tech.utils;

import com.soloval.tech.response.BaseResponse;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class RestCommonUtils {
    public static BaseResponse buildSuccessResponse(String messageCode, String message) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setMessageCode(messageCode);
        baseResponse.setMessage(message);
        baseResponse.setStatusCode(200);
        baseResponse.setDescription("Success");
        baseResponse.setTimestamp(java.time.ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        return baseResponse;
    }

    public static <T> BaseResponse buildSuccessDataResponse(String messageCode, String message, T data) {
        BaseResponse baseResponse = buildSuccessResponse(messageCode, message);
        return baseResponse;
    }

    public static BaseResponse buildBaseResponse(int statusCode, String description, String messageCode, String message, List<String> detailMessage) {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setStatusCode(statusCode);
        baseResponse.setMessageCode(messageCode);
        baseResponse.setMessage(message);
        baseResponse.setDescription(description);
        baseResponse.setTimestamp(java.time.ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
        baseResponse.setDetailMessage(detailMessage);
        return baseResponse;
    }
}
