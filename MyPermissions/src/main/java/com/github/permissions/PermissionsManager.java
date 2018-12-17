package com.github.permissions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.Random;

/**
 * @createBy Administrator
 * @time 2018-12-14 15:16
 */
public class PermissionsManager {
    private static PermissionsManager manager;
    private SparseArray<PermissionsCallback> callbackSparseArray = new SparseArray<>();

    private PermissionsManager() {
    }

    static {
        if (manager == null) {
            manager = new PermissionsManager();
        }
    }

    public static PermissionsManager get() {
        return manager;
    }

    /*********************************activity*************************************/
    public void request(Activity activity, String permission, PermissionsCallback callback) {
        request(activity, new String[]{permission}, callback);
    }

    public void request(Activity activity, String[] permissions, PermissionsCallback callback) {
        startRequest(activity, permissions, callback);
    }
    public void requestAll(Activity activity,PermissionsCallback callback) {
        String[] permissions = getManifestPermissions(activity);
        if(permissions!=null&&permissions.length>0){
            request(activity, permissions, callback);
        }
    }

    /*********************************fragment*************************************/
    public void request(Fragment fragment, String permission, PermissionsCallback callback) {
        request(fragment.getActivity(), new String[]{permission}, callback);
    }

    public void request(Fragment fragment, String[] permissions, PermissionsCallback callback) {
        request(fragment.getActivity(), permissions, callback);
    }
    public void requestAll(Fragment fragment, PermissionsCallback callback) {
        requestAll(fragment.getActivity(),callback);
    }

    private void startRequest(Activity activity, String[] permissions, PermissionsCallback callback) {
        int requestCode = getRequestCode();
        callbackSparseArray.put(requestCode, callback);
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }

    private int getRequestCode() {
        Random random = new Random();
        int code;
        int count = 0;
        do {
            if (count >= 10) {
                code = random.nextInt(900) + 100;
            } else {
                code = random.nextInt(9000) + 1000;
            }
            count++;
        } while (callbackSparseArray.indexOfKey(code) >= 0);

        return code;
    }

    public void setPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults == null || grantResults.length <= 0) {
            return;
        }
        PermissionsCallback permissionsCallback = callbackSparseArray.get(requestCode);
        if (permissionsCallback != null) {
            boolean allGranted = true;
            String firstDenied = null;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permissionsCallback.eachGranted(permissions[i]);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionsCallback.eachDenied(permissions[i]);
                    allGranted = false;
                    if (firstDenied == null) {
                        firstDenied = permissions[i];
                    }
                }
            }
            if (allGranted) {
                permissionsCallback.granted();
            } else {
                permissionsCallback.denied(firstDenied);
            }

            callbackSparseArray.remove(requestCode);
        }
    }

    private String[] getManifestPermissions(Activity activity) {
        String[] permissions = null;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions=packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissions;
    }
}
