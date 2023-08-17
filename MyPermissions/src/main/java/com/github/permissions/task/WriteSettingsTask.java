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

import com.github.acttool.ResultCallback;
import androidx.annotation.NonNull;



import java.util.List;

public class WriteSettingsTask extends BaseTask {
    public static final String WRITE_SETTINGS = "android.permission.WRITE_SETTINGS";

    public WriteSettingsTask(FragmentInter helper, PermissionCallback callback) {
        super(helper, callback);
    }
    @Override
    public void request(final List<String> originRequestPermissions, final List<String> agreePermissions, final List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(WRITE_SETTINGS);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (Build.VERSION.SDK_INT < 23||hasPermission(fragmentInter.getActivity(), WRITE_SETTINGS) == 1) {
            agreePermissions.add(WRITE_SETTINGS);
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + fragmentInter.getActivity().getPackageName()));
        ActTools.get(fragmentInter.getActivity()).startForResult(intent, new ResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if(MyPermission.hasPermission(fragmentInter.getActivity(),WRITE_SETTINGS)){
                    agreePermissions.add(WRITE_SETTINGS);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }else{
                    deniedPermissions.add(WRITE_SETTINGS);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }

    /*1:有权限，-1：无权限*/
    public static int hasPermission(Context context, @NonNull String permission) {
        if (TextUtils.equals(permission, WRITE_SETTINGS)) {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt >= 23) {
                return Settings.System.canWrite(context) ? 1 : -1;
            } else {
                String[] manifestPermissions = MyPermission.getManifestPermissions(context);
                for (String item:manifestPermissions){
                    if(TextUtils.equals(WRITE_SETTINGS,item)){
                        return 1;
                    }
                }
                return -1;
            }
        }
        return 0;
    }

}
