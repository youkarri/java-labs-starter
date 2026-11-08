package edu.course.lab05;

public final class DataLoadingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}