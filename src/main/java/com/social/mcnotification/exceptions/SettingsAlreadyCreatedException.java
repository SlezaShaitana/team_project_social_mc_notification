package com.social.mcnotification.exceptions;

public class SettingsAlreadyCreatedException extends RuntimeException {
    public SettingsAlreadyCreatedException(String message) {
        super(message);
    }
}
