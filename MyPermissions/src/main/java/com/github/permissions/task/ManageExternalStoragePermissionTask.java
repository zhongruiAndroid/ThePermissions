package com.github.permissions.task;

import android.Manifest;
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
            if (ActivityCompat.checkSelfPermission(fragmentInter.getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(fragmentInter.getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                agreePermissions.add(MANAGE_EXTERNAL_STORAGE);
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
                return;
            }
            fragmentInter.getRequestHelper().requestSimple(fragmentInter, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionCallback() {
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
            return;
        }
        if (hasPermission(fragmentInter.getActivity(), MANAGE_EXTERNAL_STORAGE) == 1) {
            extracted(deniedPermissions, originRequestPermissions, agreePermissions);
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
                    extracted(deniedPermissions, originRequestPermissions, agreePermissions);
                } else {
                    deniedPermissions.add(MANAGE_EXTERNAL_STORAGE);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }

    private void extracted(List<String> deniedPermissions, List<String> originRequestPermissions, List<String> agreePermissions) {
        agreePermissions.add(MANAGE_EXTERNAL_STORAGE);
        /*如果android 11有MANAGE_EXTERNAL_STORAGE权限，则代表有WRITE_EXTERNAL_STORAGE和READ_EXTERNAL_STORAGE权限*/
        if (originRequestPermissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            agreePermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            deniedPermissions.remove(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (originRequestPermissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            agreePermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            deniedPermissions.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        boolean has1 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_AUDIO);
        boolean has2 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_IMAGES);
        boolean has3 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_VIDEO);
        if (has1) {
            agreePermissions.add(ReadMediaTask.READ_MEDIA_AUDIO);
            deniedPermissions.remove(ReadMediaTask.READ_MEDIA_AUDIO);
        }
        if (has2) {
            agreePermissions.add(ReadMediaTask.READ_MEDIA_IMAGES);
            deniedPermissions.remove(ReadMediaTask.READ_MEDIA_IMAGES);
        }
        if (has3) {
            agreePermissions.add(ReadMediaTask.READ_MEDIA_VIDEO);
            deniedPermissions.remove(ReadMediaTask.READ_MEDIA_VIDEO);
        }
        finish(originRequestPermissions, agreePermissions, deniedPermissions);
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        boolean has = TextUtils.equals(permission, MANAGE_EXTERNAL_STORAGE);
        boolean has1 = TextUtils.equals(permission, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean has2 = TextUtils.equals(permission, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (!(has || has1 || has2)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 30) {
            if (Environment.isExternalStorageManager()) {
                return 1;
            }
        }
        if (has1) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
        } else if (has2) {
            if (sdkInt >= 33) {
                if (ActivityCompat.checkSelfPermission(context, ReadMediaTask.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, ReadMediaTask.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(context, ReadMediaTask.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED) {
                    return 1;
                }
            }
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
        } else {
            if (sdkInt >= 30) {
                return Environment.isExternalStorageManager() ? 1 : -1;
            } else {
                return (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) ? 1 : -1;
            }
        }
    }
}
