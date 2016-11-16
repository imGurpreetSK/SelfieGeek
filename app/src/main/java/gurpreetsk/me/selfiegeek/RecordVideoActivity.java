package gurpreetsk.me.selfiegeek;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.CAMERA_RQ;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.IMAGE_BROADCAST;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.MY_PERMISSIONS_REQUEST_ACCESS_CAMERA;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.VIDEO_BROADCAST;
import static gurpreetsk.me.selfiegeek.utils.Utility.getFileFromCacheAndUpload;
import static gurpreetsk.me.selfiegeek.utils.permissions.askCameraPermission;
import static gurpreetsk.me.selfiegeek.utils.permissions.askMicrophonePermission;

public class RecordVideoActivity extends AppCompatActivity {

    private static final String TAG = "RecordVideoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_image);
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (cameraPermissionCheck != PackageManager.PERMISSION_GRANTED) {
            askCameraPermission(this);
            askMicrophonePermission(this);
        } else {
            new MaterialCamera(this)
                    .maxAllowedFileSize(1024 * 1024 * 20)    //20MB
                    .showPortraitWarning(false)
                    .videoPreferredAspect(4f/3f)
                    .videoPreferredHeight(720)
                    .start(CAMERA_RQ);
            TextView tv = (TextView) findViewById(R.id.textview_image_activity);
            tv.setText(getResources().getString(R.string.thanks_for_camera_permission));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {

            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Upload started", Toast.LENGTH_LONG).show();
                Log.i(TAG, "onActivityResult: " + data.getDataString());
                String name = data.getDataString().substring(71, 90);
                getFileFromCacheAndUpload(name, data.getData(), getApplicationContext());
                Intent intent = new Intent();
                intent.setAction(VIDEO_BROADCAST);
                sendBroadcast(intent);
                finish();
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "Camera access permission needed to take image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
