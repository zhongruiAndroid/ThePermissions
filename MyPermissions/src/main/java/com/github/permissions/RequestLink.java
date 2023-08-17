package com.github.permissions;

import java.util.List;

public class RequestLink {
    private BaseTask firstTask;
    private BaseTask endTask;

    public void addNext(BaseTask linkTask) {
        if (linkTask == null) {
            return;
        }
        if (firstTask == null) {
            firstTask = linkTask;
        }
        if (endTask != null) {
            endTask.next = linkTask;
        }
        endTask = linkTask;
    }

    public void request(List<String> originRequestPermissions, List<String> agreePermissions, List<String> deniedPermissions) {
        if (firstTask != null) {
            firstTask.request(originRequestPermissions, agreePermissions, deniedPermissions);
        }
    }
}
