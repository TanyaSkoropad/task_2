package com.task.task;


public enum Roles {
    USER("USER"),
    ROLE_ANONYMOUS("ROLE_ANONYMOUS");

    private String value;

    Roles(String value) {
        this.value = value;
    }

    public static Roles from(String value) {
        return Roles.valueOf(value);
    }

    public String getValue() {
        return value;
    }
}
