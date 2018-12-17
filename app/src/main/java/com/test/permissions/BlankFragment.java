package com.test.permissions;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.github.permissions.MyPermissions;
import com.github.permissions.PermissionsCallback;

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
                MyPermissions.get(BlankFragment.this).request(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionsCallback() {
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
                });
                /*MyPermissions.get(BlankFragment.this).requestAll(new PermissionsCallback() {
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