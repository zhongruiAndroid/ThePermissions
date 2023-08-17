package com.github.permissions;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

/**
 * @createBy Administrator
 * @time 2018-12-17 13:45
 */
public class RequestPermissionSupportFragment extends Fragment implements PermissionRequest, FragmentInter {

    private RequestHelper requestHelper = new RequestHelper();

    private RequestHelper getHelper() {
        if (requestHelper == null) {
            requestHelper = new RequestHelper();
        }
        return requestHelper;
    }

    public static RequestPermissionSupportFragment newInstance() {
        RequestPermissionSupportFragment fragment = new RequestPermissionSupportFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*旋转屏幕保留fragment实例*/
        setRetainInstance(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        getHelper().onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    public void request(String permission, PermissionCallback callback) {
        if (permission == null || permission.length() == 0) {
            return;
        }
        request(new String[]{permission}, callback);
    }

    public void request(String[] permission, PermissionCallback callback) {
        if (permission == null || permission.length == 0) {
            return;
        }
        getHelper().request(this, permission, callback);
    }

    @Override
    public void request(List<String> permission, PermissionCallback callback) {
        if (permission == null || permission.size() == 0) {
            return;
        }
        getHelper().request(this, permission, callback);
    }

    public void requestAll(PermissionCallback callback) {
        String[] permission = MyPermission.getManifestPermissions(getActivity());
        request(permission, callback);
    }

    @Override
    public RequestHelper getRequestHelper() {
        return getHelper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getHelper().onDestroy();
    }
}
