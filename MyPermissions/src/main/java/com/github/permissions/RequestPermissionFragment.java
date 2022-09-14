package com.github.permissions;

import android.app.Activity;
import android.app.Fragment;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @createBy Administrator
 * @time 2018-12-17 13:45
 */
public class RequestPermissionFragment extends Fragment implements PermissionRequest {

    private SparseArray<PermissionCallback> callbackSparseArray = new SparseArray<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static RequestPermissionFragment newInstance() {
        RequestPermissionFragment fragment = new RequestPermissionFragment();
        return fragment;
    }

    public int setCallbackForCode(PermissionCallback callback) {
        if (callbackSparseArray == null) {
            callbackSparseArray = new SparseArray<>();
        }
        int requestCode = getRequestCode();
        callbackSparseArray.put(requestCode, callback);
        return requestCode;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults == null || grantResults.length <= 0) {
            return;
        }
        PermissionCallback permissionCallback = callbackSparseArray.get(requestCode);
        if (permissionCallback != null) {
            boolean allGranted = true;
            String firstDenied = null;
            String permission = "";
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permission = permissions[i];
                    permissionCallback.eachAgree(permission, false);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permissionCallback.eachDenied(permissions[i]);
                    allGranted = false;
                    if (firstDenied == null) {
                        firstDenied = permissions[i];
                    }
                }
            }
            if (allGranted) {
                permissionCallback.agree();
            } else {
                permissionCallback.denied(firstDenied);
            }

            callbackSparseArray.remove(requestCode);
        }
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


    public void request(String permission, PermissionCallback callback) {
        if (permission == null || permission.length() == 0) {
            return;
        }
        request(new String[]{permission}, callback);
    }

    public void request(String[] permission, PermissionCallback callback) {
        if (permission == null || permission.length == 0 ) {
            return;
        }
        Activity activity = getActivity();

        List<String> permissionList = new ArrayList<>();

        for (String permissionItem : permission) {
            if(TextUtils.isEmpty(permissionItem)||TextUtils.isEmpty(permissionItem.replace(" ",""))){
                continue;
            }
            int checkSelfPermission = ActivityCompat.checkSelfPermission(activity, permissionItem);
            if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                if (callback != null) {
                    callback.eachAgree(permissionItem, true);
                }
            } else {
                permissionList.add(permissionItem);
            }
        }
        if (permissionList.size() <= 0) {
            if (callback != null) {
                callback.agree();
            }
        } else {
            String[] permissionOther = new String[permissionList.size()];
            permissionList.toArray(permissionOther);

            int requestCode = setCallbackForCode(callback);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(permissionOther, requestCode);
            }
        }


    }

    public void requestAll(PermissionCallback callback) {
        String[] permission = getManifestPermissions(getActivity());
        request(permission, callback);
    }

    public String[] getManifestPermissions(Activity activity) {
        String[] permission = {};
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            permission = packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permission;
    }

}
