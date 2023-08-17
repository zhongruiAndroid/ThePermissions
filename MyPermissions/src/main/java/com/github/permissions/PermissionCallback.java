package com.github.permissions;

/**
 * @createBy Administrator
 * @time 2018-12-17 10:41
 */
public abstract class PermissionCallback {
    public abstract void agree();
    public abstract void denied(String firstDenied);
    public void eachAgree(String permission,boolean preIsAgree){

    };
    public void eachDenied(String permission){

    };
}
