package com.github.permissions.task;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;



import java.util.List;

public class BodySensorsBackgroundTask extends BaseTask {
    public static final String BODY_SENSORS_BACKGROUND = "android.permission.BODY_SENSORS_BACKGROUND";

    public BodySensorsBackgroundTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }


    @Override
    public void request(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        boolean has =originRequestPermissions.contains(BODY_SENSORS_BACKGROUND);
        if(!has){
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
            return;
        }
        if(hasPermission(fragmentInter.getActivity(),BODY_SENSORS_BACKGROUND)==1){
            agreePermissions.add(BODY_SENSORS_BACKGROUND);
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
            return;
        }
        if(MyPermission.hasPermission(fragmentInter.getActivity(),Manifest.permission.BODY_SENSORS)){
            requestSimple(BODY_SENSORS_BACKGROUND,originRequestPermissions,agreePermissions,deniedPermissions);
        }else{
            deniedPermissions.add(BODY_SENSORS_BACKGROUND);
            finish(originRequestPermissions,agreePermissions,deniedPermissions);
        }
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission,BODY_SENSORS_BACKGROUND)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 33) {
            if (MyPermission.hasPermission(context, Manifest.permission.BODY_SENSORS)) {
                return ActivityCompat.checkSelfPermission(context, BODY_SENSORS_BACKGROUND) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            } else {
                return -1;
            }
        }
        return 1;
    }
}
