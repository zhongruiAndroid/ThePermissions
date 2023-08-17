package com.github.permissions;

import android.app.Activity;

import androidx.annotation.NonNull;

public interface FragmentInter {
    Activity getActivity();
    void requestPermissions(@NonNull String[] permissions, int requestCode);
    RequestHelper getRequestHelper();
}
