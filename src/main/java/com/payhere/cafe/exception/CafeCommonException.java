package com.payhere.cafe.exception;

public class CafeCommonException extends RuntimeException {
    private Object responseBody;

    public CafeCommonException(String code, Object responseBody) {
        super(code);
        this.responseBody = responseBody;
    }

    public Object getResponseBody() {
        return responseBody;
    }
}