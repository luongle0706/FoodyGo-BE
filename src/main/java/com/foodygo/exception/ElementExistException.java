package com.foodygo.exception;

import lombok.Data;

@Data
public class ElementExistException extends RuntimeException {

    private String message;

    public ElementExistException(String message) {
        this.message = message;
    }

}
