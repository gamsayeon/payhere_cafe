package com.payhere.cafe.exception;

public class NotMatchedJWTPatternException extends CafeCommonException {
    public NotMatchedJWTPatternException(String code, Object responseBody) {
        super(code, responseBody);
    }
}