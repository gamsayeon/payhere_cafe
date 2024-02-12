package com.payhere.cafe.exception;

public class CafeServerException extends CafeCommonException {
    public CafeServerException(String code, Object responseBody) {
        super(code, responseBody);
    }
}
