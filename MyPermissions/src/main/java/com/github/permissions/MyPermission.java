package com.github.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.github.permissions.task.BackgroundLocationPermissionTask;
import com.github.permissions.task.BluetoothPermissionTask;
import com.github.permissions.task.BodySensorsBackgroundTask;
import com.github.permissions.task.ManageExternalStoragePermissionTask;
import com.github.permissions.task.NotificationPermissionTask;
import com.github.permissions.task.ReadMediaTask;
import com.github.permissions.task.RequestInstallPackagesTask;
import com.github.permissions.task.SystemAlertWindowTask;
import com.github.permissions.task.WifiPermissionTask;
import com.github.permissions.task.WriteSettingsTask;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;


/**
 * @createBy Administrator
 * @time 2018-12-14 15:16
 */
public class MyPermission {
    /*每适配一个特殊权限，需要修改以下三处地方
    MyPermission.hasPermission()
    RequestHelper.addNext()
    NormalPermissionTask.remove()
    */
    private final static String TAG = "MyPermission";

    private MyPermission() {

    }

    public static PermissionRequest get(android.app.Fragment fragment) {
        if (fragment == null) {
            throw new IllegalStateException("get(fragment)不能为空");
        }
        return get(fragment.getActivity());
    }

    public static PermissionRequest get(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("get(activity)不能为空");
        }
        if (activity instanceof FragmentActivity) {
            return get((FragmentActivity) activity);
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
            throw new IllegalStateException("get(fragment)不能为空");
        }
        return get(fragment.getActivity());
    }

    public static PermissionRequest get(FragmentActivity activity) {
        if (activity == null) {
            throw new IllegalStateException("get(activity)不能为空");
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
    public static void toSystemAlertWindowSetting(Activity activity) {
        toSystemAlertWindowSetting(activity, -1);
    }

    public static void toSystemAlertWindowSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 23) {
            toSetting(activity, requestCode);
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (requestCode == -1) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到系统设置页面
     */
    public static void toSystemSetting(Activity activity) {
        toSystemSetting(activity, -1);
    }

    public static void toSystemSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 23) {
            toSetting(activity, requestCode);
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (requestCode == -1) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 跳转到设置页面
     */
    public static void toSetting(Activity activity) {
        toSetting(activity, -1);
    }

    public static void toSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (requestCode == -1) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPermission(Context context, @NonNull String permission) {
        int result = BackgroundLocationPermissionTask.hasPermission(context, permission);
        if (result == 0) {
            result = BodySensorsBackgroundTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = ManageExternalStoragePermissionTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = NotificationPermissionTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = RequestInstallPackagesTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = SystemAlertWindowTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = WriteSettingsTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = ReadMediaTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = WifiPermissionTask.hasPermission(context, permission);
        }
        if (result == 0) {
            result = BluetoothPermissionTask.hasPermission(context, permission);
        }

        if (result != 0) {
            return result == 1;
        }
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /*是否有获取大致位置权限*/
    public static boolean hasCoarseLocationPermission(Context context) {
        return hasPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /*是否有获取精确位置权限*/
    public static boolean hasFineLocationPermission(Context context) {
        return hasPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
    }

    /*app在后台是否有获取位置权限*/
    public static boolean hasBackgroundLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= 29) {
            if (hasCoarseLocationPermission(context) || hasFineLocationPermission(context)) {
                return hasPermission(context, Manifest.permission.ACCESS_BACKGROUND_LOCATION);
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldShowRequestPermissionRationale(android.app.Fragment context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    public static boolean shouldShowRequestPermissionRationale(Activity context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.shouldShowRequestPermissionRationale(permission);
        }
        return false;
    }

    public static boolean shouldShowRequestPermissionRationale(Fragment context, String permission) {
        return context.shouldShowRequestPermissionRationale(permission);
    }

    public static boolean hasNotificationPermission(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }

    /*获取清单文件配置的所有权限*/
    public static String[] getManifestPermissions(Context context) {
        if (context == null) {
            return new String[0];
        }
        String[] permission = {};
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permission = packageInfo.requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permission;
    }


    public static void toNotificationSetting(Activity activity) {
        toNotificationSetting(activity, -1);
    }

    public static void toNotificationSetting(Activity activity, int requestCode) {
        if (activity == null) {
            return;
        }
        try {
            Intent intent = new Intent();
            //还有一个ACTION_CHANNEL_NOTIFICATION_SETTINGS
            if (Build.VERSION.SDK_INT >= 26) {
                //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, activity.getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, activity.getApplicationInfo().uid);
            }

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", activity.getPackageName());
            intent.putExtra("app_uid", activity.getApplicationInfo().uid);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (requestCode == -1) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (requestCode == -1) {
                activity.startActivity(intent);
            } else {
                activity.startActivityForResult(intent, requestCode);
            }
        }
    }


    public static void toManageExternalStorageSetting(Activity activity) {
        toManageExternalStorageSetting(activity, -1);
    }

    public static void toManageExternalStorageSetting(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < 30 || activity == null) {
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        if (intent.resolveActivity(activity.getPackageManager()) == null) {
            intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
        }
        if (requestCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void toInstallPackageSetting(Activity activity) {
        toInstallPackageSetting(activity, -1);
    }

    public static void toInstallPackageSetting(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT < 26 || activity == null) {
            return;
        }
        // 开启当前应用的权限管理页
        Uri packageUri = Uri.parse("package:" + activity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageUri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (requestCode == -1) {
            activity.startActivity(intent);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

}
