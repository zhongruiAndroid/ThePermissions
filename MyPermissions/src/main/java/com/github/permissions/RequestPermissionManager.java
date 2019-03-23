package com.github.permissions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/***
 *   created by zhongrui on 2019/3/23
 */
public class RequestPermissionManager {
    private RequestPermissionFragment fragment;
    public RequestPermissionManager(RequestPermissionFragment fragment) {
        this.fragment = fragment;
    }

    public void request(String permission, PermissionCallback callback){
        if(permission == null || permission.length() == 0){
            return;
        }
        request(new String[]{permission},callback);
    }
    public void request(String[]permission,PermissionCallback callback){
        if(permission==null||permission.length==0){
            return;
        }
        startRequest(permission,callback);
    }

    public void requestAll(PermissionCallback callback) {
        String[] permission = getManifestPermissions(fragment.getActivity());
        if(permission!=null&&permission.length>0){
            request(permission, callback);
        }
    }

    private void startRequest(String[]permission,PermissionCallback callback){
        int requestCode = fragment.setCallbackForCode(callback);
        fragment.requestPermissions(permission,requestCode);
    }



    private String[] getManifestPermissions(Activity activity) {
        String[] permission = null;
        try {
            PackageInfo packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            permission=packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permission;
    }
}
