package com.social.mcnotification.exceptions;


import lombok.Data;

@Data
public class ErrorMessages {
    private String error;

    public ErrorMessages(String error) {
        this.error = error;
    }
}
