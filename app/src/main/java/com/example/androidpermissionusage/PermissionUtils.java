package com.example.androidpermissionusage;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

/**
 * @PackageName: com.example.androidpermissionusage
 * @ClassName: PermissionUtils
 * @Author: winwa
 * @Date: 2023/4/5 8:13
 * @Description:
 **/
public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    private final static int REQUEST_RECORD_AUDIO = 0;
    private final static int REQUEST_GET_ACCOUNTS = 1;
    private final static int REQUEST_READ_PHONE_STATE = 2;
    private final static int REQUEST_CALL_PHONE = 3;
    private final static int REQUEST_CAMERA = 4;
    private final static int REQUEST_ACCESS_FINE_LOCATION = 5;
    private final static int REQUEST_ACCESS_COARSE_LOCATION = 6;
    private final static int REQUEST_READ_EXTERNAL_STORAGE = 7;
    private final static int REQUEST_WRITE_EXTERNAL_STORAGE = 8;

    private final static String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    private final static String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    private final static String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    private final static String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    private final static String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private final static String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final static String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String[] mRequestPermissions = {
            PERMISSION_RECORD_AUDIO, PERMISSION_GET_ACCOUNTS, PERMISSION_READ_PHONE_STATE,
            PERMISSION_CALL_PHONE, PERMISSION_CAMERA, PERMISSION_ACCESS_FINE_LOCATION,
            PERMISSION_ACCESS_COARSE_LOCATION, PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE
    };

    public static void requestPermission(Activity activity, int requestCode, OnPermissionGrant onPermissionGrant) {
        if (activity == null) {
            Log.i(TAG, "requestPermission: activity == null");
            return;
        }

        if (requestCode < 0 || requestCode >= mRequestPermissions.length) {
            Toast.makeText(activity, "requestPermission: requestCode < 0 || requestCode >= " + mRequestPermissions.length, Toast.LENGTH_SHORT).show();
            return;
        }

        String requestPermission = mRequestPermissions[requestCode];

        if (Build.VERSION.SDK_INT < 23) {
            Log.i(TAG, "requestPermission: Build.VERSION.SDK_INT<23");
            Toast.makeText(activity, "Got " + requestPermission, Toast.LENGTH_SHORT).show();
            if (onPermissionGrant != null) {
                onPermissionGrant.onPermissionGrant(requestCode);
            }
            return;
        }

        int checkPermissionResult;
        try {
            checkPermissionResult = ActivityCompat.checkSelfPermission(activity, requestPermission);
        } catch (Exception e) {
            Toast.makeText(activity, "权限检查失败", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkPermissionResult != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Request " + requestPermission, Toast.LENGTH_SHORT).show();
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                new AlertDialog.Builder(activity)
                        .setMessage("Request " + requestPermission)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity, new String[]{requestPermission}, requestCode);
            }
        } else {
            Toast.makeText(activity, "Got " + requestPermission, Toast.LENGTH_SHORT).show();
            if (onPermissionGrant != null) {
                onPermissionGrant.onPermissionGrant(requestCode);
            }
        }
    }

    public static void requestPermissionResult(Activity activity, int requestCode,
                                               String[] permissions, int[] grantResults, OnPermissionGrant onPermissionGrant) {
        if (activity == null) {
            Log.i(TAG, "requestPermissionResult: activity == null");
            return;
        }

        if (requestCode < 0 || requestCode >= mRequestPermissions.length) {
            Toast.makeText(activity, "requestPermissionResult: requestCode < 0 || requestCode >= " + mRequestPermissions.length, Toast.LENGTH_SHORT).show();
            return;
        }

        String requestPermission = permissions[0];

        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(activity, "Got " + requestPermission, Toast.LENGTH_SHORT).show();
            if (onPermissionGrant != null) {
                onPermissionGrant.onPermissionGrant(requestCode);
            }
        } else {
            openSettingActivity(activity, "Request " + requestPermission);
        }
    }

    private static void openSettingActivity(Activity activity, String msg) {
        new AlertDialog.Builder(activity)
                .setMessage(msg)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public interface OnPermissionGrant {
        void onPermissionGrant(int requestCode);
    }
}
