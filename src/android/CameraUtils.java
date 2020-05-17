package com.meedbankingclub.cordova.camera;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.LOG;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;

abstract class CameraUtils {
    private static SharedPreferences.Editor editor;
    private static final String PLUGIN_PREFERENCES = "com.meedbankingclub.cordova.Camera";

    private static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;

    public static String[] mapPermissions(JSONArray permissions) throws JSONException {
        String[] permissionList = new String[permissions.length()];
        for (int i = 0; i < permissions.length(); i++) {
            String permission = permissions.getString(i);
            if (permission.equals("camera")) {
                permissionList[i] = PERMISSION_CAMERA;
            } else if (permission.equals("storage")) {
                permissionList[i] = PERMISSION_WRITE_EXTERNAL_STORAGE;
            } else if (permission.equals("audio")) {
                permissionList[i] = PERMISSION_RECORD_AUDIO;
            }
        }
        return permissionList;
    }

    public static boolean shouldAskedPermission(CordovaPlugin plugin, String[] permissions) {
        boolean askedPermission = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permission : permissions) {
                if (!PermissionHelper.hasPermission(plugin, permission)) {
                    askedPermission = true;
                }
            }
        }
        LOG.d("MEED[Camera](shouldAskedPermission)", askedPermission+"");
        return askedPermission;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void checkPermissions(CordovaInterface cordova, String permission, PermissionListener permissionListener) {
        SharedPreferences sharedPreferences = cordova.getContext().getSharedPreferences(PLUGIN_PREFERENCES, Context.MODE_PRIVATE);

        if (cordova.getActivity().shouldShowRequestPermissionRationale(permission)) {
            permissionListener.onPermissionDeniedOnce();
        } else {
            boolean firstTimeAsked = sharedPreferences.getBoolean(permission, true);
            if (firstTimeAsked) {
                if (editor == null) editor = sharedPreferences.edit();
                editor.putBoolean(permission, false);
                editor.apply();
                editor = null;
            } else {
                permissionListener.onPermissionAlwaysDenied();
            }
        }
    }

}

interface PermissionListener {

    void onPermissionDeniedOnce();

    void onPermissionAlwaysDenied();

}
