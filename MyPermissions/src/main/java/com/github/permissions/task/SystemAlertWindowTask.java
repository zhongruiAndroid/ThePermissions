package com.github.permissions.task;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.github.acttool.ResultCallback;
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

public class SystemAlertWindowTask extends BaseTask {
    public static final String SYSTEM_ALERT_WINDOW = "android.permission.SYSTEM_ALERT_WINDOW";
    public SystemAlertWindowTask(FragmentInter helper, PermissionCallback callback) {
        super(helper,callback);
    }



    @Override
    public void request(final List<String> originRequestPermissions,final  List<String> agreePermissions,final  List<String> deniedPermissions) {
        boolean has = originRequestPermissions.contains(SYSTEM_ALERT_WINDOW);
        if (!has) {
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }
        if (Build.VERSION.SDK_INT < 23||hasPermission(fragmentInter.getActivity(), SYSTEM_ALERT_WINDOW) == 1) {
            agreePermissions.add(SYSTEM_ALERT_WINDOW);
            finish(originRequestPermissions, agreePermissions, deniedPermissions);
            return;
        }

        Intent intent =new  Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + fragmentInter.getActivity().getPackageName()));
        ActTools.get(fragmentInter.getActivity()).startForResult(intent, new ResultCallback() {
            @Override
            public void onActivityResult(int resultCode, Intent data) {
                if(MyPermission.hasPermission(fragmentInter.getActivity(),SYSTEM_ALERT_WINDOW)){
                    agreePermissions.add(SYSTEM_ALERT_WINDOW);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }else{
                    deniedPermissions.add(SYSTEM_ALERT_WINDOW);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            }
        });
    }
    /*1:有权限，-1：无权限*/
    public static int hasPermission(Context context, @NonNull String permission) {
        if (TextUtils.equals(permission, SystemAlertWindowTask.SYSTEM_ALERT_WINDOW)) {
            int sdkInt = Build.VERSION.SDK_INT;
            if (sdkInt >= 23) {
                return Settings.canDrawOverlays(context)?1:-1;
            } else {
                String[] manifestPermissions = MyPermission.getManifestPermissions(context);
                for (String item:manifestPermissions){
                    if(TextUtils.equals(SYSTEM_ALERT_WINDOW,item)){
                        return 1;
                    }
                }
                return -1;
            }
        }
        return 0;
    }

}
