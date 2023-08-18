package com.github.permissions;

import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.SparseArray;

import com.github.permissions.task.BackgroundLocationPermissionTask;
import com.github.permissions.task.BluetoothPermissionTask;
import com.github.permissions.task.BodySensorsBackgroundTask;
import com.github.permissions.task.ManageExternalStoragePermissionTask;
import com.github.permissions.task.NormalPermissionTask;
import com.github.permissions.task.NotificationPermissionTask;
import com.github.permissions.task.ReadMediaTask;
import com.github.permissions.task.RequestInstallPackagesTask;
import com.github.permissions.task.SystemAlertWindowTask;
import com.github.permissions.task.WifiPermissionTask;
import com.github.permissions.task.WriteSettingsTask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RequestHelper implements Serializable {

    private SparseArray<PermissionCallback> callbackSparseArray = new SparseArray<>();
    /*已经同意不需要请求的权限*/
    private SparseArray<List<String>> noNeedRequestPermissions = new SparseArray<>();
    private Random random = new Random();
    private OnBeforeRequestListener onBeforeRequestListener;

    public void setOnBeforeRequestListener(OnBeforeRequestListener onBeforeRequestListener) {
        this.onBeforeRequestListener = onBeforeRequestListener;
    }

    public int setCallbackForCode(PermissionCallback callback) {
        if (callbackSparseArray == null) {
            callbackSparseArray = new SparseArray<>();
        }
        int requestCode = getRequestCode();
        callbackSparseArray.put(requestCode, callback);
        return requestCode;
    }

    private int getRequestCode() {
        int code;
        do {
            code = (System.currentTimeMillis() + "" + random.nextInt(900) + 100).hashCode();
        } while (callbackSparseArray.indexOfKey(code) >= 0);
        return code;
    }

    public void onDestroy() {
        if (callbackSparseArray != null) {
            callbackSparseArray.clear();
        }
        if (noNeedRequestPermissions != null) {
            noNeedRequestPermissions.clear();
        }
        onBeforeRequestListener=null;
    }

    public void request(FragmentInter fragment, String permission, PermissionCallback callback) {
        if (TextUtils.isEmpty(permission)) {
            if (callback != null) {
                callback.agreeAll(new ArrayList<String>());
            }
            return;
        }
        request(fragment, new String[]{permission}, callback);
    }

    public void request(FragmentInter fragment, String[] permission, PermissionCallback callback) {
        if (permission == null || permission.length == 0) {
            if (callback != null) {
                callback.agreeAll(new ArrayList<String>());
            }
            return;
        }
        request(fragment, Arrays.asList(permission), callback);
    }

    public void request(FragmentInter fragment,final List<String> permission, PermissionCallback permissionCallback) {
        if (permission == null || permission.size() == 0) {
            if (permissionCallback != null) {
                permissionCallback.agreeAll(new ArrayList<String>());
            }
            return;
        }
        final RequestLink link = new RequestLink();
        /*常规权限请求*/
        link.addNext(new NormalPermissionTask(fragment, permissionCallback));
        /*后台位置权限*/
        link.addNext(new BackgroundLocationPermissionTask(fragment, permissionCallback));
        /*后台传感器权限*/
        link.addNext(new BodySensorsBackgroundTask(fragment, permissionCallback));
        /*通知权限*/
        link.addNext(new NotificationPermissionTask(fragment, permissionCallback));
        /*安装权限*/
        link.addNext(new RequestInstallPackagesTask(fragment, permissionCallback));
        /*悬浮窗权限*/
        link.addNext(new SystemAlertWindowTask(fragment, permissionCallback));
        /*修改系统设置权限*/
        link.addNext(new WriteSettingsTask(fragment, permissionCallback));
        /*android13存储权限*/
        link.addNext(new ReadMediaTask(fragment, permissionCallback));
        /*android13wifi权限*/
        link.addNext(new WifiPermissionTask(fragment, permissionCallback));
        /*所有文件权限,先处理ReadMediaTask再处理ManageExternalStoragePermissionTask*/
        link.addNext(new ManageExternalStoragePermissionTask(fragment, permissionCallback));
        /*android11蓝牙权限*/
        link.addNext(new BluetoothPermissionTask(fragment, permissionCallback));




        if(onBeforeRequestListener!=null){
            final List<String>deniedPermissions=new ArrayList<>();
            for (String item:permission){
                if(!MyPermission.hasPermission(fragment.getActivity(),item)){
                    deniedPermissions.add(item);
                }
            }
            if(deniedPermissions.isEmpty()){
                link.request(new ArrayList<>(permission), new ArrayList<String>(), new ArrayList<String>());
                return;
            }
            onBeforeRequestListener.handle(deniedPermissions, new OnBeforeRequestListener.Listener() {
                @Override
                public void onResult(List<String> agreeRequestList) {
                    if(agreeRequestList==null||agreeRequestList.size()==0){
                        /*如果被拒绝的权限用户不同意请求获取*/
                        permission.removeAll(deniedPermissions);
                        link.request(new ArrayList<>(permission), new ArrayList<String>(), new ArrayList<String>(deniedPermissions));
                        return;
                    }
                    deniedPermissions.removeAll(agreeRequestList);
                    permission.removeAll(deniedPermissions);
                    link.request(new ArrayList<>(permission), new ArrayList<String>(), new ArrayList<String>(deniedPermissions));
                }
            });
        }else{
            link.request(new ArrayList<>(permission), new ArrayList<String>(), new ArrayList<String>());
        }


    }

    public void requestSimple(FragmentInter fragment, String permission, PermissionCallback callback) {
        requestSimple(fragment, new String[]{permission}, callback);
    }

    public void requestSimple(FragmentInter fragment, String[] permission, PermissionCallback callback) {
        requestSimple(fragment,Arrays.asList(permission),callback);
    }
    public void requestSimple(FragmentInter fragment, List<String> permission, PermissionCallback callback) {
        if (fragment == null) {
            return;
        }
        if (permission == null || permission.size() == 0) {
            if (callback != null) {
                callback.agreeAll(new ArrayList<String>());
            }
            return;
        }

        List<String> permissionList = new ArrayList<>();
        List<String> noNeedRequestPermissionsList = new ArrayList<>();

        for (String permissionItem : permission) {
            if (TextUtils.isEmpty(permissionItem)) {
                continue;
            }
            int checkSelfPermission = ActivityCompat.checkSelfPermission(fragment.getActivity(), permissionItem);
            if (checkSelfPermission == PackageManager.PERMISSION_GRANTED) {
                if (!noNeedRequestPermissionsList.contains(permissionItem)) {
                    noNeedRequestPermissionsList.add(permissionItem);
                }
            } else {
                if (!permissionList.contains(permissionItem)) {
                    permissionList.add(permissionItem);
                }
            }
        }
        if (permissionList.size() == 0) {
            if (callback != null) {
                callback.agreeAll(permission);
            }
        } else {
            String[] permissionOther = new String[permissionList.size()];
            permissionList.toArray(permissionOther);

            int requestCode = setCallbackForCode(callback);

            noNeedRequestPermissions.put(requestCode, noNeedRequestPermissionsList);
            fragment.requestPermissions(permissionOther, requestCode);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults == null || grantResults.length <= 0) {
            return;
        }
        PermissionCallback permissionCallback = callbackSparseArray.get(requestCode);
        List<String> strings = noNeedRequestPermissions.get(requestCode);
        if (permissionCallback != null) {

            List<String> agreeList = new ArrayList<>();
            List<String> deniedList = new ArrayList<>();
            if (strings != null) {
                agreeList.addAll(strings);
            }
            boolean allGranted = true;
            String permission = "";

            for (int i = 0; i < grantResults.length; i++) {
                permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    agreeList.add(permission);
                } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedList.add(permission);
                    allGranted = false;
                }
            }
            if (allGranted) {
                permissionCallback.agreeAll(agreeList);
            } else {
                permissionCallback.denied(agreeList, deniedList);
            }
            callbackSparseArray.remove(requestCode);
        }
        if (strings != null) {
            noNeedRequestPermissions.remove(requestCode);
        }
    }
}
