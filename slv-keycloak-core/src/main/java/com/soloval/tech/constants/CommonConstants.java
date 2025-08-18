package com.soloval.tech.constants;

public class CommonConstants {
    private CommonConstants(){}
    public static final String ZONE_ID = "Asia/Ho_Chi_Minh";
    public static final String FORMAT_DATE_TIME="dd-MM-yyyy HH:mm:ss";
    public static final String DEFAULT_PASSWORD = "ChangeMe#1";
    public static final class VerificationCodeType {
        public static final String OTP = "OTP";
    }
    public static final class ResponseFields {
        public static final String TRANSACTION_ID = "transactionId";
        public static final String OTP = "otp";
    }
    public static final class RequestFields {
        public static final String PHONE_NUMBER = "phone";
        public static final String OTP = "otp";
        public static final String TRANSACTION_ID = "transactionId";
    }
    public static final String BODY = "Body";
    public static final String PATH_VARIABLE = "Path Variable";
    public static final String QUERY_PARAMETER = "Query Parameter";
}
