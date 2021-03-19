package com.github.permissions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.ArrayList;
import java.util.List;

/***
 *   created by zhongrui on 2019/3/23
 */
public class RequestPermissionManager {
    private RequestPermissionFragment fragment;

    public RequestPermissionManager(RequestPermissionFragment fragment) {
        this.fragment = fragment;
    }

    public void request(String permission, PermissionCallback callback) {
        if (permission == null || permission.length() == 0) {
            return;
        }
        request(new String[]{permission}, callback);
    }

    public void request(String[] permission, PermissionCallback callback) {
        if (permission == null || permission.length == 0 || callback == null) {
            return;
        }


        FragmentActivity activity = fragment.getActivity();

        List<String> permissionList = new ArrayList<>();

        for (String permissionItem : permission) {
            int checkSelfPermission = ActivityCompat.checkSelfPermission(activity, permissionItem);
            if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                callback.eachGranted(permissionItem);
            } else {
                permissionList.add(permissionItem);
            }
        }
        if (permissionList.size() <= 0) {
            callback.granted();
        } else {
            String[] permissionOther = new String[permissionList.size()];
            permissionList.toArray(permissionOther);

            int requestCode = fragment.setCallbackForCode(callback);
            fragment.requestPermissions(permissionOther, requestCode);
        }


    }

    public void requestAll(PermissionCallback callback) {
        String[] permission = getManifestPermissions(fragment.getActivity());
        request(permission, callback);
    }

    private String[] getManifestPermissions(Activity activity) {
        String[] permission = null;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            permission = packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permission;
    }
}
