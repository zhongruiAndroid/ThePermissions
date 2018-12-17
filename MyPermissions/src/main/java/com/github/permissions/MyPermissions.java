package com.github.permissions;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import java.util.Random;

/**
 * @createBy Administrator
 * @time 2018-12-14 15:16
 */
public class MyPermissions {
    private final String TAG = this.getClass().getSimpleName();
    private FragmentActivity activity;
    private RequestPermissionsFragment requestFragment;

    private MyPermissions(FragmentActivity activity) {
        this.activity=activity;
        initFragment();
    }
    public static MyPermissions get(FragmentActivity activity) {
        return new MyPermissions(activity);
    }
    public static MyPermissions get(Fragment fragment) {
        return get(fragment.getActivity());
    }
    private void initFragment() {
        Fragment fragment = activity.getSupportFragmentManager().findFragmentByTag(TAG);
        if(fragment==null){
            requestFragment=RequestPermissionsFragment.newInstance();
            activity.getSupportFragmentManager().beginTransaction().add(requestFragment,TAG).commitAllowingStateLoss();
            activity.getSupportFragmentManager().executePendingTransactions();
        }else{
            requestFragment= (RequestPermissionsFragment) fragment;
        }
    }

    public void request(String permission, PermissionsCallback callback){
        request(new String[]{permission},callback);
    }
    public void request(String[]permissions,PermissionsCallback callback){
        startRequest(permissions,callback);
    }

    public void requestAll(PermissionsCallback callback) {
        String[] permissions = getManifestPermissions(activity);
        if(permissions!=null&&permissions.length>0){
            request(permissions, callback);
        }
    }

    private void startRequest(String[]permissions,PermissionsCallback callback){
        int requestCode = requestFragment.setCallbackForCode(callback);
        requestFragment.requestPermissions(permissions,requestCode);
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
