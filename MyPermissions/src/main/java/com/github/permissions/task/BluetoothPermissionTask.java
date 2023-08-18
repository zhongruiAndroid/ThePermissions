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


import java.util.ArrayList;
import java.util.List;

public class BluetoothPermissionTask extends BaseTask {
    public static final String BLUETOOTH_ADVERTISE = "android.permission.BLUETOOTH_ADVERTISE";
    public static final String BLUETOOTH_CONNECT = "android.permission.BLUETOOTH_CONNECT";
    public static final String BLUETOOTH_SCAN = "android.permission.BLUETOOTH_SCAN";

    public BluetoothPermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }

    @Override
    public void request(final List<String> originRequestPermissions, final List<String> agreePermissions, final List<String> deniedPermissions) {
        final boolean has1 = originRequestPermissions.contains(BLUETOOTH_ADVERTISE);
        final boolean has2 = originRequestPermissions.contains(BLUETOOTH_CONNECT);
        final boolean has3 = originRequestPermissions.contains(BLUETOOTH_SCAN);
        if (!(has1 || has2 || has3)) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }


        final int sdkInt = Build.VERSION.SDK_INT;
        List<String> list = new ArrayList<>();
        if (sdkInt >= 31) {
            if (has1) {
                if (hasPermission(fragmentInter.getActivity(), BLUETOOTH_ADVERTISE) == 1) {
                    agreePermissions.add(BLUETOOTH_ADVERTISE);
                } else {
                    list.add(BLUETOOTH_ADVERTISE);
                }
            }
            if (has2) {
                if (hasPermission(fragmentInter.getActivity(), BLUETOOTH_CONNECT) == 1) {
                    agreePermissions.add(BLUETOOTH_CONNECT);
                } else {
                    list.add(BLUETOOTH_CONNECT);
                }
            }
            if (has3) {
                if (hasPermission(fragmentInter.getActivity(), BLUETOOTH_SCAN) == 1) {
                    agreePermissions.add(BLUETOOTH_SCAN);
                } else {
                    list.add(BLUETOOTH_SCAN);
                }
            }
            if (list.isEmpty()) {
                finish(originRequestPermissions, agreePermissions, deniedPermissions);
                return;
            }
        } else {
            list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (fragmentInter != null) {
            fragmentInter.getRequestHelper().requestSimple(fragmentInter, list, new PermissionCallback() {
                @Override
                public void agreeAll(List<String> agreeList) {
                    if (has1) {
                        if(!agreePermissions.contains(BLUETOOTH_ADVERTISE)){
                            agreePermissions.add(BLUETOOTH_ADVERTISE);
                        }
                    }
                    if (has2) {
                        if(!agreePermissions.contains(BLUETOOTH_CONNECT)){
                            agreePermissions.add(BLUETOOTH_CONNECT);
                        }
                    }
                    if (has3) {
                        if(!agreePermissions.contains(BLUETOOTH_SCAN)){
                            agreePermissions.add(BLUETOOTH_SCAN);
                        }
                    }
                    if (sdkInt < 31) {
                        boolean remove = deniedPermissions.remove(Manifest.permission.ACCESS_FINE_LOCATION);
                        if(remove){
                            /*如果之前存在拒绝的情况*/
                            agreePermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
                        }
                    }
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
                @Override
                public void denied(List<String> agreeList, List<String> deniedList) {
                    if (has1) {
                        deniedPermissions.add(BLUETOOTH_ADVERTISE);
                    }
                    if (has2) {
                        deniedPermissions.add(BLUETOOTH_CONNECT);
                    }
                    if (has3) {
                        deniedPermissions.add(BLUETOOTH_SCAN);
                    }
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            });
        }
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        boolean has1 = TextUtils.equals(permission, BLUETOOTH_ADVERTISE);
        boolean has2 = TextUtils.equals(permission, BLUETOOTH_CONNECT);
        boolean has3 = TextUtils.equals(permission, BLUETOOTH_SCAN);
        if (!(has1 || has2 || has3)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 31) {
            if (has1) {
                return ActivityCompat.checkSelfPermission(context, BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            }
            if (has2) {
                return ActivityCompat.checkSelfPermission(context, BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            }
            if (has3) {
                return ActivityCompat.checkSelfPermission(context, BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
            }
        }
        return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ? 1 : -1;
    }
}
