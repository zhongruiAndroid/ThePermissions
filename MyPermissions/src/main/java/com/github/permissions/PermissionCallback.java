package com.github.permissions;

import java.util.List;

/**
 * @createBy Administrator
 * @time 2018-12-17 10:41
 */
public abstract class PermissionCallback {
    public abstract void agreeAll(List<String> agreeList);

    public abstract void denied(List<String> agreeList, List<String> deniedList);
}
