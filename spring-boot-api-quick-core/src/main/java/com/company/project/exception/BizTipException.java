package com.company.project.exception;

public class BizTipException extends RuntimeException {

    private static final long serialVersionUID = 7320901300864964780L;

    public BizTipException(Throwable cause) {
        super(cause);
    }

    public BizTipException(String message) {
        super(message);
    }

    public BizTipException(String message, Throwable cause) {
        super(message, cause);
    }

}
