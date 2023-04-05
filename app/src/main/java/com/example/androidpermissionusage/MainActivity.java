package com.example.androidpermissionusage;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick(View view) {
        PermissionUtilsV2.requestPermission(this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionUtilsV2.OnRequestPermission() {
            @Override
            public void onPermissionGrant(String[] requestPermissions) {
                Toast.makeText(MainActivity.this, "onPermissionGrant", Toast.LENGTH_SHORT).show();
                call("10086");
            }

            @Override
            public void onPermissionDenied(String[] requestPermissions) {
                Toast.makeText(MainActivity.this, "onPermissionDenied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        PermissionUtilsV2.requestPermissionResult(this, requestCode, permissions, grantResults, new PermissionUtilsV2.OnRequestPermission() {
            public void onPermissionGrant(String[] requestPermissions) {
                Toast.makeText(MainActivity.this, "onPermissionGrant", Toast.LENGTH_SHORT).show();
                call("10086");
            }

            @Override
            public void onPermissionDenied(String[] requestPermissions) {
                Toast.makeText(MainActivity.this, "onPermissionDenied", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void call(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri uri = Uri.parse("tel:" + phoneNumber);
        intent.setData(uri);
        startActivity(intent);
    }
}