package com.github.permissions.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import com.github.acttool.ActTools;
import com.github.permissions.BaseTask;
import com.github.permissions.FragmentInter;
import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;

import com.github.acttool.ResultCallback;

import java.util.List;

public class RequestInstallPackagesTask extends BaseTask {
    public static final String REQUEST_INSTALL_PACKAGES = "android.permission.REQUEST_INSTALL_PACKAGES";

    public RequestInstallPackagesTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }


    @Override
    public void request(final List<String> originRequestPermissions,final  List<String> agreePermissions,final  List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(REQUEST_INSTALL_PACKAGES);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (Build.VERSION.SDK_INT < 26 || hasPermission(fragmentInter.getActivity(), REQUEST_INSTALL_PACKAGES) == 1) {
            agreePermissions.add(REQUEST_INSTALL_PACKAGES);
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }

        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        intent.setData(Uri.parse("package:" + fragmentInter.getActivity().getPackageName()));
        ActTools.get(fragmentInter.getActivity()).startForResult(intent, new ResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if (MyPermission.hasPermission(fragmentInter.getActivity(), REQUEST_INSTALL_PACKAGES)) {
                    agreePermissions.add(REQUEST_INSTALL_PACKAGES);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                } else {
                    deniedPermissions.add(REQUEST_INSTALL_PACKAGES);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }

    public static int hasPermission(Context context, @NonNull String permission) {
        if (!TextUtils.equals(permission, REQUEST_INSTALL_PACKAGES)) {
            return 0;
        }
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 26) {
            return context.getPackageManager().canRequestPackageInstalls() ? 1 : -1;
        }
        return 1;
    }
}
