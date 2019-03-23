package com.test.permissions;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.permissions.MyPermission;
import com.github.permissions.PermissionCallback;


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
                MyPermission.get(MainActivity.this).request(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionCallback() {
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
               PermissionsManager.get().request(MainActivity.this,new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS}, new PermissionCallback() {
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
