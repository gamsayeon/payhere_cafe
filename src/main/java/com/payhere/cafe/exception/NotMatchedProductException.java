package com.payhere.cafe.exception;

public class NotMatchedProductException extends CafeCommonException {
    public NotMatchedProductException(String code, Object responseBody) {
        super(code, responseBody);
    }
}
