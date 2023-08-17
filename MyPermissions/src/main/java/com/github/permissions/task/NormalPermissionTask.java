package com.github.permissions.task;

import android.Manifest;


import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.PermissionCallback;

import java.util.ArrayList;
import java.util.List;

public class NormalPermissionTask extends BaseTask {
    public NormalPermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }

    @Override
    public void request(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        List<String> list = new ArrayList<>(originRequestPermissions);
        list.remove(BackgroundLocationPermissionTask.ACCESS_BACKGROUND_LOCATION);
        list.remove(BodySensorsBackgroundTask.BODY_SENSORS_BACKGROUND);
        list.remove(ManageExternalStoragePermissionTask.MANAGE_EXTERNAL_STORAGE);
        list.remove(NotificationPermissionTask.POST_NOTIFICATIONS);
        list.remove(RequestInstallPackagesTask.REQUEST_INSTALL_PACKAGES);
        list.remove(SystemAlertWindowTask.SYSTEM_ALERT_WINDOW);
        list.remove(WriteSettingsTask.WRITE_SETTINGS);


        /*适配android13,交给MediaImageTask处理*/
        list.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
        list.remove(ReadMediaTask.READ_MEDIA_AUDIO);
        list.remove(ReadMediaTask.READ_MEDIA_IMAGES);
        list.remove(ReadMediaTask.READ_MEDIA_VIDEO);

        list.remove(WifiPermissionTask.NEARBY_WIFI_DEVICES);


        String[] objects = list.toArray(new String[0]);

        requestSimple(objects,originRequestPermissions,agreePermissions,deniedPermissions);
    }
}
