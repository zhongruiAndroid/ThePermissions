package com.github.permissions;

import java.util.List;

public interface OnBeforeRequestListener {
    void handle(List<String> deniedPermission, Listener listener);

    public static interface Listener {
        void onResult(List<String> agreeRequestList);
    }
}
