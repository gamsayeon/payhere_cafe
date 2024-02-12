package com.payhere.cafe.exception;

public class NotMatchedUsersException extends CafeCommonException {
    public NotMatchedUsersException(String code, Object responseBody) {
        super(code, responseBody);
    }
}
