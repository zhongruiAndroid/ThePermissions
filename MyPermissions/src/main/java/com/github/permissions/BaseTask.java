package com.github.permissions;

import java.util.Arrays;
import java.util.List;

public abstract class BaseTask {
    protected BaseTask next;
    protected PermissionCallback callback;
    protected FragmentInter fragmentInter;

    public BaseTask(FragmentInter helper, PermissionCallback callback) {
        this.callback = callback;
        this.fragmentInter = helper;
    }


    public void finish(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        if (next != null) {
            next.request(originRequestPermissions, agreePermissions, deniedPermissions);
        } else {
            if (callback != null) {
                if (deniedPermissions == null || deniedPermissions.isEmpty()) {
                    callback.agreeAll(agreePermissions);
                } else {
                    callback.denied(agreePermissions, deniedPermissions);
                }
            }
        }
    }

    public abstract void request(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions);


    public void requestSimple(String permissions, List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        requestSimple(new String[]{permissions}, originRequestPermissions, agreePermissions, deniedPermissions);
    }

    public void requestSimple(String[] permissions, List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        requestSimple(Arrays.asList(permissions),originRequestPermissions,agreePermissions,deniedPermissions);
    }
    public void requestSimple(List<String> permissions, final List<String> originRequestPermissions,final  List<String> agreePermissions,final  List<String> deniedPermissions) {
        if (fragmentInter != null) {
            fragmentInter.getRequestHelper().requestSimple(fragmentInter, permissions, new PermissionCallback() {
                @Override
                public void agreeAll(List<String> agreeList) {
                    agreePermissions.addAll(agreeList);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }

                @Override
                public void denied(List<String> agreeList, List<String> deniedList) {
                    agreePermissions.addAll(agreeList);
                    deniedPermissions.addAll(deniedList);
                    finish(originRequestPermissions, agreePermissions, deniedPermissions);
                }
            });
        }
    }
}
