package com.github.permissions;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * @createBy Administrator
 * @time 2018-12-14 15:16
 */
public class MyPermission {
    private final static String TAG = "MyPermission";
    private MyPermission() {
    }
    public static RequestPermissionManager get(Fragment fragment){
        if(fragment==null){
            new IllegalStateException("get(fragment)不能为空");
        }
        return get(fragment.getActivity());
    }
    public static RequestPermissionManager get(FragmentActivity activity){
        if(activity==null){
            new IllegalStateException("get(activity)不能为空");
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if(fragment==null){
            RequestPermissionFragment requestPermissionFragment = RequestPermissionFragment.newInstance();
            fm.beginTransaction().add(requestPermissionFragment,TAG).commitAllowingStateLoss();
            fm.executePendingTransactions();
            return requestPermissionFragment.getRequestPermissionManager();
        }else{
            RequestPermissionFragment requestPermissionFragment= (RequestPermissionFragment) fragment;
            return requestPermissionFragment.getRequestPermissionManager();
        }

    }
}
