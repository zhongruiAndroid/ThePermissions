package com.github.permissions.task;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.text.TextUtils;

import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class ReadMediaTask extends BaseTask {
    public static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    public static final String READ_MEDIA_AUDIO = "android.permission.READ_MEDIA_AUDIO";
    public static final String READ_MEDIA_IMAGES = "android.permission.READ_MEDIA_IMAGES";
    public static final String READ_MEDIA_VIDEO = "android.permission.READ_MEDIA_VIDEO";

    public ReadMediaTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }

    @Override
    public void request(final List<String> originRequestPermissions, final List<String> agreePermissions, final List<String> deniedPermissions) {
        final boolean has = originRequestPermissions.contains(READ_EXTERNAL_STORAGE);
        final boolean has1 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_AUDIO);
        final boolean has2 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_IMAGES);
        final boolean has3 = originRequestPermissions.contains(ReadMediaTask.READ_MEDIA_VIDEO);

        if (!(has || has1 || has2 || has3)) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (fragmentInter == null) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        List<String> list = new ArrayList<>();
        /*如果版本大于等于Android13*/
        if (Build.VERSION.SDK_INT >= 33) {
            if (has) {
                list.add(ReadMediaTask.READ_MEDIA_AUDIO);
                list.add(ReadMediaTask.READ_MEDIA_IMAGES);
                list.add(ReadMediaTask.READ_MEDIA_VIDEO);
            } else {
                if (has1) {
                    list.add(READ_MEDIA_AUDIO);
                }
                if (has2) {
                    list.add(READ_MEDIA_IMAGES);
                }
                if (has3) {
                    list.add(READ_MEDIA_VIDEO);
                }
            }
        } else {
            list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            if (ActivityCompat.checkSelfPermission(fragmentInter.getActivity(), READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                if (has) {
                    agreePermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (has1) {
                    agreePermissions.add(READ_MEDIA_AUDIO);
                }
                if (has2) {
                    agreePermissions.add(READ_MEDIA_IMAGES);
                }
                if (has3) {
                    agreePermissions.add(READ_MEDIA_VIDEO);
                }
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
                return;
            }
        }
        fragmentInter.getRequestHelper().requestSimple(fragmentInter, list, new PermissionCallback() {
            @Override
            public void agreeAll(List<String> agreeList) {
                if (has) {
                    agreePermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }
                if (has1) {
                    agreePermissions.add(READ_MEDIA_AUDIO);
                }
                if (has2) {
                    agreePermissions.add(READ_MEDIA_IMAGES);
                }
                if (has3) {
                    agreePermissions.add(READ_MEDIA_VIDEO);
                }
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
            }

            @Override
            public void denied(List<String> agreeList, List<String> deniedList) {
                if (Build.VERSION.SDK_INT >= 33) {
                    if (has) {
                        deniedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (has1) {
                        if (agreeList.contains(READ_MEDIA_AUDIO)) {
                            agreePermissions.add(READ_MEDIA_AUDIO);
                        }
                        if (deniedList.contains(READ_MEDIA_AUDIO)) {
                            deniedPermissions.add(READ_MEDIA_AUDIO);
                        }
                    }
                    if (has2) {
                        if (agreeList.contains(READ_MEDIA_IMAGES)) {
                            agreePermissions.add(READ_MEDIA_IMAGES);
                        }
                        if (deniedList.contains(READ_MEDIA_IMAGES)) {
                            deniedPermissions.add(READ_MEDIA_IMAGES);
                        }
                    }
                    if (has3) {
                        if (agreeList.contains(READ_MEDIA_VIDEO)) {
                            agreePermissions.add(READ_MEDIA_VIDEO);
                        }
                        if (deniedList.contains(READ_MEDIA_VIDEO)) {
                            deniedPermissions.add(READ_MEDIA_VIDEO);
                        }
                    }
                } else {
                    if (has) {
                        deniedPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                    }
                    if (has1) {
                        deniedPermissions.add(READ_MEDIA_AUDIO);
                    }
                    if (has2) {
                        deniedPermissions.add(READ_MEDIA_IMAGES);
                    }
                    if (has3) {
                        deniedPermissions.add(READ_MEDIA_VIDEO);
                    }
                }
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
            }
        });
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        boolean has = TextUtils.equals(permission, READ_EXTERNAL_STORAGE);
        boolean has1 = TextUtils.equals(permission, READ_MEDIA_AUDIO);
        boolean has2 = TextUtils.equals(permission, READ_MEDIA_IMAGES);
        boolean has3 = TextUtils.equals(permission, READ_MEDIA_VIDEO);

        if (!(has || has1 || has2 || has3)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 30) {
            if (Environment.isExternalStorageManager()) {
                return 1;
            }
        }
        if (sdkInt >= 33) {
            if (TextUtils.equals(permission, READ_MEDIA_AUDIO) || TextUtils.equals(permission, READ_MEDIA_IMAGES) || TextUtils.equals(permission, READ_MEDIA_VIDEO)) {
                return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            }
            if (TextUtils.equals(permission, READ_EXTERNAL_STORAGE)) {
                boolean result1 = ActivityCompat.checkSelfPermission(context, READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;
                boolean result2 = ActivityCompat.checkSelfPermission(context, READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
                boolean result3 = ActivityCompat.checkSelfPermission(context, READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
                return (result1 && result2 && result3) ? 1 : -1;
            }
        }
        return ActivityCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
    }
}
