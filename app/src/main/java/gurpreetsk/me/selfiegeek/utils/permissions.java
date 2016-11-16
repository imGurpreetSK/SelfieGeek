package gurpreetsk.me.selfiegeek.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_ACCESS_STORAGE;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_RECORD_AUDIO;

/**
 * Created by Gurpreet on 12/11/16.
 */

public class permissions {

    public static void askCameraPermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.CAMERA)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_ACCESS_CAMERA);
            }
        }
    }

    public static void askMicrophonePermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.RECORD_AUDIO)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        }
    }

    public static void askStoragePermission(Activity context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_ACCESS_STORAGE);
            }
        }
    }

}
