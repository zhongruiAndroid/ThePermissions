package com.test.permissions;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BlankFragment extends Fragment {
    Button btRequest;
    public BlankFragment() {
    }
    public static BlankFragment newInstance() {
        BlankFragment fragment = new BlankFragment();
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btRequest = view.findViewById(R.id.btRequest);
        btRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermission.get(BlankFragment.this).request(new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionCallback() {
                    @Override
                    public void agree() {
                        Log.i("==","f===granted");
                    }
                    @Override
                    public void denied(String firstDenied) {
                        Log.i("==","f==denied="+firstDenied);
                    }
                    @Override
                    public void eachAgree(String permissions,boolean is) {
                        super.eachAgree(permissions,is);
                        Log.i("==","f===eachGranted"+permissions);
                    }
                    @Override
                    public void eachDenied(String permissions) {
                        super.eachDenied(permissions);
                        Log.i("==","f===eachDenied"+permissions);
                    }
                });
                /*MyPermissions.get(BlankFragment.this).requestAll(new PermissionCallback() {
                    @Override
                    public void granted() {
                        Log.i("==","f===granted");
                    }
                    @Override
                    public void denied(String firstDenied) {
                        Log.i("==","f==denied="+firstDenied);
                    }
                    @Override
                    public void eachGranted(String permissions) {
                        super.eachGranted(permissions);
                        Log.i("==","f===eachGranted"+permissions);
                    }
                    @Override
                    public void eachDenied(String permissions) {
                        super.eachDenied(permissions);
                        Log.i("==","f===eachDenied"+permissions);
                    }
                });*/
            }
        });
    }
}
