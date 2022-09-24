package com.podcrash.commissions.yandere.core.common.error;

public class GlobalServerNotFoundException extends RuntimeException {
    public GlobalServerNotFoundException(){
        super("Global server settings not found!");
    }
}
