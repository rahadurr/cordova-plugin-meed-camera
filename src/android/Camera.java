package com.meedbankingclub.cordova.camera;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PermissionHelper;
import org.json.JSONArray;
import org.json.JSONException;


/**
 * This class echoes a string called from JavaScript.
 */
public class Camera extends CordovaPlugin {

    private static final String ACTION_GET_PERMISSION = "getPermission";
    private static final String ACTION_CAPTURE_PHOTO = "capturePhoto";
    private static final String ACTION_RECORD_VIDEO = "recordVideo";

    private static final int PERMISSION_STATUS_GRANTED = 100;
    private static final int PERMISSION_STATUS_DENIED_ONCE = 200;
    private static final int PERMISSION_STATUS_DENIED_ALWAYS = 400;

    private int permissionStatus;

    private CordovaInterface cordova;
    private CallbackContext callbackContext;

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        this.cordova = cordova;
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        this.callbackContext = callbackContext;
        switch (action) {
            case ACTION_GET_PERMISSION:
                try {
                    this.getPermission(args, callbackContext);
                } catch (Exception ex) {
                    callbackContext.error(ex.getMessage());
                }
                return true;
            case ACTION_CAPTURE_PHOTO:
                try {
                    this.capturePhoto(args, callbackContext);
                } catch (Exception ex) {
                    callbackContext.error(ex.getMessage());
                }
                return true;
            case ACTION_RECORD_VIDEO:
                try {
                    this.recordVideo(args, callbackContext);
                } catch (Exception ex) {
                    callbackContext.error(ex.getMessage());
                }
                return true;
        }
        return false;
    }
    

    private void getPermission(JSONArray options, CallbackContext callbackContext) throws JSONException {
        JSONArray permissions = options.getJSONArray(0);
        String[] requestedPermissions = CameraUtils.mapPermissions(permissions);

        if (CameraUtils.shouldAskedPermission(this, requestedPermissions)) {
            PermissionHelper.requestPermissions(this, 100, requestedPermissions);
        } else {
            callbackContext.success(PERMISSION_STATUS_GRANTED);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        for (int i = 0; i < permissions.length; i++) {
            int r = grantResults[i];
            if (r == PackageManager.PERMISSION_DENIED) {
                CameraUtils.checkPermissions(this.cordova, permissions[i], new PermissionListener() {
                    @Override
                    public void onPermissionDeniedOnce() {
                        permissionStatus = PERMISSION_STATUS_DENIED_ONCE;
                    }

                    @Override
                    public void onPermissionAlwaysDenied() {
                        permissionStatus = PERMISSION_STATUS_DENIED_ALWAYS;
                    }
                });
                break;
            } else {
                permissionStatus = PERMISSION_STATUS_GRANTED;
            }

        }
        callbackContext.success(permissionStatus);
    }


    private void capturePhoto(JSONArray options, CallbackContext callbackContext) throws JSONException {

    }

    private void recordVideo(JSONArray options, CallbackContext callbackContext) throws JSONException {

    }
}

