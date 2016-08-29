package com.persistentbit.jjson.mapping.impl;

/**
 * Created by petermuys on 27/08/16.
 */
public class JJsonException extends RuntimeException{

    public JJsonException() {
        super();
    }

    public JJsonException(String message) {
        super(message);
    }

    public JJsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JJsonException(Throwable cause) {
        super(cause);
    }

    protected JJsonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
