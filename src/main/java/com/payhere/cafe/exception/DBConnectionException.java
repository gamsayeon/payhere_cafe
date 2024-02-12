package com.payhere.cafe.exception;

public class DBConnectionException extends CafeCommonException {
    public DBConnectionException(String code, Object responseBody) {
        super(code, responseBody);
    }
}

