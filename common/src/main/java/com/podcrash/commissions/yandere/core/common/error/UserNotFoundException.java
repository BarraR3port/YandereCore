package com.podcrash.commissions.yandere.core.common.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String msg){
        super("User not found: " + msg);
    }
}
