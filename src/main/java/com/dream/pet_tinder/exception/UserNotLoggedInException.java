package com.dream.pet_tinder.exception;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
        super();
    }

    public UserNotLoggedInException(String message) {
        super(message);
    }
}
