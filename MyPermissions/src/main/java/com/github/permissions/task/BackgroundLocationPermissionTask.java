package com.github.permissions.task;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;



import java.util.ArrayList;
import java.util.List;

public class BackgroundLocationPermissionTask extends BaseTask {
    public static final String ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION";

    public BackgroundLocationPermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }



    @Override
    public void request(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        boolean has =originRequestPermissions.contains(ACCESS_BACKGROUND_LOCATION);
        if(!has){
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
            return;
        }
        if(MyPermission.hasBackgroundLocationPermission(fragmentInter.getActivity())){
            agreePermissions.add(ACCESS_BACKGROUND_LOCATION);
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
            return;
        }
        if(MyPermission.hasFineLocationPermission(fragmentInter.getActivity())||MyPermission.hasCoarseLocationPermission(fragmentInter.getActivity())){
            requestSimple(ACCESS_BACKGROUND_LOCATION,originRequestPermissions,agreePermissions,deniedPermissions);
        }else{
            deniedPermissions.add(ACCESS_BACKGROUND_LOCATION);
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
        }

    }

    /*1:有权限，-1：无权限*/
    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission, BackgroundLocationPermissionTask.ACCESS_BACKGROUND_LOCATION)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 29) {
            if (MyPermission.hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) || MyPermission.hasPermission(context, "android.permission.ACCESS_COARSE_LOCATION")) {
                return ActivityCompat.checkSelfPermission(context, BackgroundLocationPermissionTask.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            } else {
                return -1;
            }
        }
        return 1;
    }
}
