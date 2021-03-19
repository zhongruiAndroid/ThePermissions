package com.github.permissions;

import android.app.Activity;

public interface PermissionRequest {
    void request(String permission, PermissionCallback callback);
    void request(String[] permission, PermissionCallback callback);
    void requestAll(PermissionCallback callback);
    String[] getManifestPermissions(Activity activity);
}
