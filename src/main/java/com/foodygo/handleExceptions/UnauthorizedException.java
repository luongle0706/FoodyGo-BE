package com.foodygo.handleExceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UnauthorizedException extends RuntimeException {

    private String message;

    public UnauthorizedException(String message) {
        this.message = message;
    }

}
