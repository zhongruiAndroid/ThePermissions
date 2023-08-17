package com.github.permissions;

import android.app.Activity;

import java.util.List;

public interface PermissionRequest {
    void request(String permission, PermissionCallback callback);
    void request(String[] permission, PermissionCallback callback);
    void request(List<String> permission, PermissionCallback callback);
    void requestAll(PermissionCallback callback);
}
