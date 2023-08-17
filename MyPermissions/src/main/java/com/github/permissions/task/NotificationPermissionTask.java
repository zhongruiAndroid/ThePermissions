package com.github.permissions.task;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.github.acttool.ActTools;
import com.github.acttool.ResultCallback;
import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;

import java.util.List;

public class NotificationPermissionTask extends BaseTask {
    public static final String POST_NOTIFICATIONS = "android.permission.POST_NOTIFICATIONS";

    public NotificationPermissionTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }

    @Override
    public void request(final List<String> originRequestPermissions,final List<String> agreePermissions,final List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(POST_NOTIFICATIONS);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        Activity activity = fragmentInter.getActivity();
        if (hasPermission(fragmentInter.getActivity(), POST_NOTIFICATIONS) == 1) {
            agreePermissions.add(POST_NOTIFICATIONS);
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        Intent intent = new Intent();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            intent = new Intent();
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
            intent.setData(uri);
        }

        ActTools.get(fragmentInter.getActivity()).startForResult(intent, new ResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if(MyPermission.hasPermission(fragmentInter.getActivity(),POST_NOTIFICATIONS)){
                    agreePermissions.add(POST_NOTIFICATIONS);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }else{
                    deniedPermissions.add(POST_NOTIFICATIONS);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission, POST_NOTIFICATIONS)) {
            return 0;
        }
        return MyPermission.hasNotificationPermission(context) ? 1 : -1;
    }
}
