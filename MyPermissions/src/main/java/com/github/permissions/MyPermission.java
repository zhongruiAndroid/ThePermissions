package com.github.permissions;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @createBy Administrator
 * @time 2018-12-14 15:16
 */
public class MyPermission {
    private final static String TAG = "MyPermission";

    private MyPermission() {
    }

    public static PermissionRequest get(android.app.Fragment fragment) {
        if (fragment == null) {
            new IllegalStateException("get(fragment)不能为空");
        }
        return get(fragment.getActivity());
    }

    public static PermissionRequest get(Activity activity) {
        if (activity == null) {
            new IllegalStateException("get(activity)不能为空");
        }

        android.app.FragmentManager fm = activity.getFragmentManager();
        android.app.Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment == null) {
            RequestPermissionFragment requestPermissionFragment = RequestPermissionFragment.newInstance();
            fm.beginTransaction().add(requestPermissionFragment, TAG).commitAllowingStateLoss();
            fm.executePendingTransactions();
            return requestPermissionFragment;
        } else {
            return (RequestPermissionFragment) fragment;
        }

    }

    public static PermissionRequest get(Fragment fragment) {
        if (fragment == null) {
            new IllegalStateException("get(fragment)不能为空");
        }
        return get(fragment.getActivity());
    }

    public static PermissionRequest get(FragmentActivity activity) {
        if (activity == null) {
            new IllegalStateException("get(activity)不能为空");
        }

        FragmentManager fm = activity.getSupportFragmentManager();
        Fragment fragment = fm.findFragmentByTag(TAG);
        if (fragment == null) {
            RequestPermissionSupportFragment requestPermissionFragment = RequestPermissionSupportFragment.newInstance();
            fm.beginTransaction().add(requestPermissionFragment, TAG).commitAllowingStateLoss();
            fm.executePendingTransactions();
            return requestPermissionFragment;
        } else {
            return (RequestPermissionSupportFragment) fragment;
        }

    }


    /**
     * 跳转到系统设置页面
     */
    public static void goIntentSetting(Activity activity) {
        goIntentSetting(activity, -100);
    }

    public static void goIntentSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            if (requestCode == -100) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
