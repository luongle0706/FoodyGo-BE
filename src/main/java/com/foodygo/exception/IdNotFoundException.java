package com.foodygo.exception;

import lombok.Data;

@Data
public class IdNotFoundException extends RuntimeException {

    private String message;

    public IdNotFoundException(String message) {
        this.message = message;
    }
}
