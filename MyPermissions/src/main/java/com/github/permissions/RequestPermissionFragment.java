package com.github.permissions;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

import java.util.Random;

/**
 * @createBy Administrator
 * @time 2018-12-17 13:45
 */
public class RequestPermissionFragment extends Fragment {

    private SparseArray<PermissionCallback> callbackSparseArray=new SparseArray<>();
    private RequestPermissionManager requestPermissionManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static RequestPermissionFragment newInstance() {
        RequestPermissionFragment fragment = new RequestPermissionFragment();
        return fragment;
    }

    public int setCallbackForCode(PermissionCallback callback){
        if(callbackSparseArray==null){
            callbackSparseArray=new SparseArray<>();
        }
        int requestCode = getRequestCode();
        callbackSparseArray.put(requestCode,callback);
        return requestCode;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults==null||grantResults.length <=0) {
            return;
        }
        PermissionCallback permissionCallback = callbackSparseArray.get(requestCode);
        if(permissionCallback !=null){
            boolean allGranted=true;
            String firstDenied=null;
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    permissionCallback.eachGranted(permissions[i]);
                }else if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionCallback.eachDenied(permissions[i]);
                    allGranted=false;
                    if(firstDenied==null){
                        firstDenied=permissions[i];
                    }
                }
            }
            if (allGranted) {
                permissionCallback.granted();
            }else{
                permissionCallback.denied(firstDenied);
            }

            callbackSparseArray.remove(requestCode);
        }
    }

    private int getRequestCode(){
        Random random=new Random();
        int code;
        int count=0;
        do {
            if(count>=10){
                code = random.nextInt(900)+100;
            }else{
                code = random.nextInt(9000)+1000;
            }
            count++;
        }while(callbackSparseArray.indexOfKey(code)>=0);

        return code;
    }

    public RequestPermissionManager getRequestPermissionManager() {
        if (requestPermissionManager == null) {
            requestPermissionManager=new RequestPermissionManager(this);
        }
        return requestPermissionManager;
    }

}
