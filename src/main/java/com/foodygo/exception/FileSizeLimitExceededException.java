package com.foodygo.exception;

public class FileSizeLimitExceededException extends RuntimeException{
    public FileSizeLimitExceededException(String message) { super(message);}
}