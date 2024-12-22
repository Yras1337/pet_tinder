package com.dream.pet_tinder.model.user;

public enum Permission {
    USE("use");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
