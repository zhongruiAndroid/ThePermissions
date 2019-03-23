package com.github.permissions;

/**
 * @createBy Administrator
 * @time 2018-12-17 10:41
 */
public abstract class PermissionCallback {
    public abstract void granted();
    public abstract void denied(String firstDenied);
    public void eachGranted(String permission){

    };
    public void eachDenied(String permission){

    };
}
