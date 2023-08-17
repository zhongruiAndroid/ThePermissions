package com.test.permissions;

import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class PermissionItem {
    public static class Item{
        public String permission;
        public boolean hasPermission;

        public Item(String permission, boolean hasPermission) {
            this.permission = permission;
            this.hasPermission = hasPermission;
        }
    }
    private List<Item> permission = new ArrayList<>();
    private int requestCount;

    public List<Item> getPermission() {
        if (permission == null) {
            permission = new ArrayList<>();
        }
        return permission;
    }

    public void setPermission(List<Item> permission) {
        this.permission = permission;
    }

    public void addPermission(Item permission) {
        getPermission().add(permission);
    }

    public int getRequestCount() {
        return requestCount;
    }

    public void setRequestCount(int requestCount) {
        this.requestCount = requestCount;
    }
    public void addRequestCount() {
        this.requestCount += 1;
    }
}
