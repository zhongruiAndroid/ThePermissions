package com.github.permissions.task;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import com.github.acttool.ResultCallback;
import java.util.List;

public class WifiPermissionTask extends BaseTask {
    public static final String NEARBY_WIFI_DEVICES = "android.permission.NEARBY_WIFI_DEVICES";

    public WifiPermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }

    @Override
    public void request(final List<String> originRequestPermissions,final  List<String> agreePermissions,final   List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(NEARBY_WIFI_DEVICES);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (hasPermission(fragmentInter.getActivity(), NEARBY_WIFI_DEVICES) == 1) {
            agreePermissions.add(NEARBY_WIFI_DEVICES);
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        String permission;
        if (sdkInt >= 33) {
            permission = NEARBY_WIFI_DEVICES;
        } else {
            permission = Manifest.permission.ACCESS_FINE_LOCATION;
        }
        if (fragmentInter != null) {
            fragmentInter.getRequestHelper().requestSimple(fragmentInter, permission, new PermissionCallback() {
                @Override
                public void agreeAll(List<String> agreeList) {
                    agreePermissions.add(NEARBY_WIFI_DEVICES);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }

                @Override
                public void denied(List<String> agreeList, List<String> deniedList) {
                    deniedPermissions.add(NEARBY_WIFI_DEVICES);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            });
        }
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission, NEARBY_WIFI_DEVICES)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 33) {
            return ActivityCompat.checkSelfPermission(context, NEARBY_WIFI_DEVICES) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
        }
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
    }
}
