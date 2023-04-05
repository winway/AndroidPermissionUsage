package com.example.androidpermissionusage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @PackageName: com.example.androidpermissionusage
 * @ClassName: PermissionUtilsV2
 * @Author: winwa
 * @Date: 2023/4/5 10:13
 * @Description:
 **/
public class PermissionUtilsV2 {
    private static final String TAG = "PermissionUtilsV2";

    private static final int REQUEST_PERMISSION_V2 = 1;

    public static void requestPermission(Activity activity, String[] requestPermissions, OnRequestPermission onRequestPermission) {
        Log.i(TAG, "requestPermission: " + Arrays.toString(requestPermissions));

        if (activity == null) {
            Log.i(TAG, "requestPermission: activity == null");
            return;
        }

        if (requestPermissions.length <= 0) {
            Toast.makeText(activity, "requestPermissions.length <= 0", Toast.LENGTH_SHORT).show();
            return;
        }

        if (Build.VERSION.SDK_INT < 23) {
            Log.i(TAG, "requestPermission: Build.VERSION.SDK_INT < 23");
            Toast.makeText(activity, "No need permission " + Arrays.toString(requestPermissions), Toast.LENGTH_SHORT).show();
            if (onRequestPermission != null) {
                onRequestPermission.onPermissionGrant(requestPermissions);
            }
            return;
        }

        ArrayList<String> unGrantedPermissions = new ArrayList<>();
        boolean shouldShowRequestPermissionRationale = false;
        for (int i = 0; i < requestPermissions.length; i++) {
            String requestPermission = requestPermissions[i];

            int checkPermissionResult;
            try {
                checkPermissionResult = ActivityCompat.checkSelfPermission(activity, requestPermission);
            } catch (Exception e) {
                Toast.makeText(activity, "权限检查失败", Toast.LENGTH_SHORT).show();
                return;
            }

            if (checkPermissionResult != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(requestPermission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, requestPermission)) {
                    shouldShowRequestPermissionRationale = true;
                }
            }
        }

        if (unGrantedPermissions.size() > 0) {
            Toast.makeText(activity, "Request permission" + unGrantedPermissions, Toast.LENGTH_SHORT).show();

            if (shouldShowRequestPermissionRationale) {
                new AlertDialog.Builder(activity)
                        .setMessage("Request permission" + unGrantedPermissions)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(activity, unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_PERMISSION_V2);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(activity, unGrantedPermissions.toArray(new String[unGrantedPermissions.size()]), REQUEST_PERMISSION_V2);
            }
        } else {
            Toast.makeText(activity, "Got permission" + Arrays.toString(requestPermissions), Toast.LENGTH_SHORT).show();
            if (onRequestPermission != null) {
                onRequestPermission.onPermissionGrant(requestPermissions);
            }
        }
    }

    public static void requestPermissionResult(Activity activity, int requestCode, String[] permissions, int[] grantResults, OnRequestPermission onRequestPermission) {
        Log.i(TAG, "requestPermissionResult: " + requestCode);
        Log.i(TAG, "requestPermissionResult: " + Arrays.toString(permissions));
        Log.i(TAG, "requestPermissionResult: " + Arrays.toString(grantResults));

        if (activity == null) {
            Log.i(TAG, "requestPermissionResult: activity == null");
            return;
        }

        if (requestCode != REQUEST_PERMISSION_V2) {
            Log.i(TAG, "requestPermissionResult: requestCode != REQUEST_PERMISSION_V2");
            return;
        }

        ArrayList<String> unGrantedPermissions = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            String permission = permissions[i];

            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(permission);
            }
        }

        if (unGrantedPermissions.size() > 0) {
            Toast.makeText(activity, "Permission denied " + unGrantedPermissions, Toast.LENGTH_SHORT).show();
            if (onRequestPermission != null) {
                onRequestPermission.onPermissionDenied(permissions);
            }
        } else {
            Toast.makeText(activity, "Got permission " + Arrays.toString(permissions), Toast.LENGTH_SHORT).show();
            if (onRequestPermission != null) {
                onRequestPermission.onPermissionGrant(permissions);
            }
        }
    }

    public interface OnRequestPermission {
        void onPermissionGrant(String[] requestPermissions);

        void onPermissionDenied(String[] requestPermissions);
    }
}
