package com.test.permissions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;

import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;
import com.github.permissions.task.BodySensorsBackgroundTask;
import com.github.permissions.task.NotificationPermissionTask;
import com.github.permissions.task.ReadMediaTask;
import com.github.permissions.task.WifiPermissionTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("=====","==SDK_INT==="+ Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.i("=====","==minSdkVersion==="+ getApplicationInfo().minSdkVersion);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i("=====","==compileSdkVersionCodename==="+ getApplicationInfo().compileSdkVersionCodename);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Log.i("=====","==compileSdkVersion==="+ getApplicationInfo().compileSdkVersion);
        }
        Log.i("=====","==targetSdkVersion==="+ getApplicationInfo().targetSdkVersion);
        Activity activity = this;
        findViewById(R.id.btSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.toSetting(activity);
            }
        });
        findViewById(R.id.btSetting1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.toNotificationSetting(activity);
            }
        });
        findViewById(R.id.btSetting2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.toInstallPackageSetting(activity);
            }
        });

        findViewById(R.id.btSystemSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.toSystemSetting(activity);
            }
        });
        findViewById(R.id.btWindowAlertSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.toSystemAlertWindowSetting(activity);
            }
        });
        findViewById(R.id.btRequestAll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.get(activity).requestAll(new PermissionCallback() {
                    @Override
                    public void agreeAll(List<String> agreeList) {
                        Log.i("=====", "=========================agreeAll=========================");
                        for (String strItem : agreeList) {
                            Log.i("=====", "===同意==" + strItem);
                        }
                    }

                    @Override
                    public void denied(List<String> agreeList, List<String> deniedList) {
                        Log.i("=====", "========================denied==========================");
                        for (String strItem : agreeList) {
                            Log.i("=====", "===同意==" + strItem);
                        }
                        for (String strItem : deniedList) {
                            Log.i("=====", "===拒绝==" + strItem);
                        }
                    }
                });
            }
        });


        List<PermissionItem> list = new ArrayList<>();

        PermissionItem item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(WifiPermissionTask.NEARBY_WIFI_DEVICES, false));
        list.add(item);

        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_AUDIO, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_IMAGES, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_VIDEO, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_AUDIO, false));
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_IMAGES, false));
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_VIDEO, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_AUDIO, false));
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_IMAGES, false));
        item.addPermission(new PermissionItem.Item(ReadMediaTask.READ_MEDIA_VIDEO, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.READ_EXTERNAL_STORAGE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.READ_EXTERNAL_STORAGE, false));
        list.add(item);

        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.READ_EXTERNAL_STORAGE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.READ_PHONE_STATE, false));
        list.add(item);

        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_FINE_LOCATION, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_COARSE_LOCATION, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_COARSE_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_FINE_LOCATION, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_COARSE_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_FINE_LOCATION, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.MANAGE_EXTERNAL_STORAGE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.READ_EXTERNAL_STORAGE, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.MANAGE_EXTERNAL_STORAGE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.REQUEST_INSTALL_PACKAGES, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BODY_SENSORS, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(BodySensorsBackgroundTask.BODY_SENSORS_BACKGROUND, false));
        list.add(item);

        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(BodySensorsBackgroundTask.BODY_SENSORS_BACKGROUND, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.BODY_SENSORS, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.WRITE_SETTINGS, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(NotificationPermissionTask.POST_NOTIFICATIONS, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.SYSTEM_ALERT_WINDOW, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_CONNECT, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_SCAN, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_ADVERTISE, false));
        list.add(item);


        item = new PermissionItem();
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_CONNECT, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_ADVERTISE, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.BLUETOOTH_SCAN, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_BACKGROUND_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_FINE_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.ACCESS_COARSE_LOCATION, false));
        item.addPermission(new PermissionItem.Item(Manifest.permission.WRITE_EXTERNAL_STORAGE, false));
        list.add(item);

        Adapter adapter = new Adapter();
        adapter.setList(list);


        RecyclerView rvList = findViewById(R.id.rvList);

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

    }
}