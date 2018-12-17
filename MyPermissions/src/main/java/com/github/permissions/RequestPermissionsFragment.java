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
public class RequestPermissionsFragment extends Fragment {

    private SparseArray<PermissionsCallback> callbackSparseArray=new SparseArray<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public static RequestPermissionsFragment newInstance() {
        RequestPermissionsFragment fragment = new RequestPermissionsFragment();
        return fragment;
    }

    public int setCallbackForCode(PermissionsCallback callback){
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
        PermissionsCallback permissionsCallback = callbackSparseArray.get(requestCode);
        if(permissionsCallback!=null){
            boolean allGranted=true;
            String firstDenied=null;
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    permissionsCallback.eachGranted(permissions[i]);
                }else if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                    permissionsCallback.eachDenied(permissions[i]);
                    allGranted=false;
                    if(firstDenied==null){
                        firstDenied=permissions[i];
                    }
                }
            }
            if (allGranted) {
                permissionsCallback.granted();
            }else{
                permissionsCallback.denied(firstDenied);
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
}
