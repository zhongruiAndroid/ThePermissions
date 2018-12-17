package com.test.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.permissions.MyPermissions;
import com.github.permissions.PermissionsCallback;
import com.github.permissions.PermissionsManager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    Button btGet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btGet = findViewById(R.id.btGet);
        btGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyPermissions.get(MainActivity.this).request(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionsCallback() {
                    @Override
                    public void granted() {
                        Log.i("==","===granted");
                    }
                    @Override
                    public void denied(String firstDenied) {
                        Log.i("==","==denied="+firstDenied);
                    }
                    @Override
                    public void eachGranted(String permissions) {
                        super.eachGranted(permissions);
                        Log.i("==","===eachGranted"+permissions);
                    }
                    @Override
                    public void eachDenied(String permissions) {
                        super.eachDenied(permissions);
                        Log.i("==","===eachDenied"+permissions);
                    }
                });

                //或者
               /*
               PermissionsManager.get().request(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionsCallback() {
                    @Override
                    public void granted() {
                        Log.i("==","===granted");
                    }
                    @Override
                    public void denied(String firstDenied) {
                        Log.i("==","==denied="+firstDenied);
                    }
                    @Override
                    public void eachGranted(String permissions) {
                        super.eachGranted(permissions);
                        Log.i("==","===eachGranted"+permissions);
                    }
                    @Override
                    public void eachDenied(String permissions) {
                        super.eachDenied(permissions);
                        Log.i("==","===eachDenied"+permissions);
                    }
                });*/
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//      PermissionsManager.get().setPermissionsResult(requestCode,permissions,grantResults);
    }
}
