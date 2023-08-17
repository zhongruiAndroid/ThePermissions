package com.github.permissions.task;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;

import com.github.acttool.ActTools;
import com.github.acttool.ResultCallback;
import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import java.util.List;

public class ManageExternalStoragePermissionTask extends BaseTask {
    public static final String MANAGE_EXTERNAL_STORAGE = "android.permission.MANAGE_EXTERNAL_STORAGE";

    public ManageExternalStoragePermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }


    @Override
    public void request(final List<String> originRequestPermissions, final List<String> agreePermissions, final List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(MANAGE_EXTERNAL_STORAGE);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (Build.VERSION.SDK_INT < 30) {
            if (hasPermission(fragmentInter.getActivity(), MANAGE_EXTERNAL_STORAGE) == 1) {
                agreePermissions.add(MANAGE_EXTERNAL_STORAGE);
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
            } else {
                fragmentInter.getRequestHelper().requestSimple(fragmentInter, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                    @Override
                    public void agreeAll(List<String> agreeList) {
                        agreePermissions.add(MANAGE_EXTERNAL_STORAGE);
                        finish(originRequestPermissions, agreePermissions, deniedPermissions);
                    }

                    @Override
                    public void denied(List<String> agreeList, List<String> deniedList) {
                        deniedPermissions.add(MANAGE_EXTERNAL_STORAGE);
                        finish(originRequestPermissions, agreePermissions, deniedPermissions);
                    }
                });
            }
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + fragmentInter.getActivity().getPackageName()));
        if (intent.resolveActivity(fragmentInter.getActivity().getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        }
        ActTools.get(fragmentInter.getActivity()).startForResult(intent, new ResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (MyPermission.hasPermission(fragmentInter.getActivity(), MANAGE_EXTERNAL_STORAGE)) {
                    agreePermissions.add(MANAGE_EXTERNAL_STORAGE);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                } else {
                    deniedPermissions.add(MANAGE_EXTERNAL_STORAGE);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission, MANAGE_EXTERNAL_STORAGE)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 30) {
            return Environment.isExternalStorageManager() ? 1 : -1;
        }
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
    }
}
