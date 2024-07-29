package com.wanted.august.exception;

public class AugustApplicationException extends RuntimeException {
    private ErrorCode errorCode;
    private String desc;


    public AugustApplicationException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.desc = null;
    }

    @Override
    public String getMessage() {
        if (desc == null) {
            return errorCode.getDesc();
        } else {
            return String.format("%s. %s", errorCode.getDesc(), desc);
        }
    }
}
