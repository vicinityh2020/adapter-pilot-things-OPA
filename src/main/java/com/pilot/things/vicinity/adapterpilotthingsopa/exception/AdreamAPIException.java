package com.pilot.things.vicinity.adapterpilotthingsopa.exception;

public class AdreamAPIException extends Exception{
    private int errorCode;
    private String errorMessage;

    public AdreamAPIException(Throwable throwable) {
        super(throwable);
    }

    public AdreamAPIException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public AdreamAPIException(String msg) {
        super(msg);
    }

    public AdreamAPIException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }


    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        return this.errorCode + " : " + this.getErrorMessage();
    }
}
