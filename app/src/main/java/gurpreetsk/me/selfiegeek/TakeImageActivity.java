package gurpreetsk.me.selfiegeek;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialcamera.MaterialCamera;
import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static gurpreetsk.me.selfiegeek.utils.permissions.askCameraPermission;

public class TakeImageActivity extends AppCompatActivity {

    private static final String TAG = "TakeImageActivity";
    MaterialCamera camera;
    public static final int CAMERA_RQ = 1001;
    public static final int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 1;

    Client mKinveyClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_image);

        camera = new MaterialCamera(this);
        mKinveyClient = new Client.Builder(getApplicationContext()).build();

        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED)
            askCameraPermission(this);
        else
            camera.stillShot().start(CAMERA_RQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Received recording or error from MaterialCamera
        if (requestCode == CAMERA_RQ) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Saved to: " + data.getDataString(), Toast.LENGTH_LONG).show();
                Log.i(TAG, "onActivityResult: " + data.getDataString());
                String name = data.getDataString().substring(71, 90);
                getFileFromCacheAndUpload(name);
            } else if (data != null) {
                Exception e = (Exception) data.getSerializableExtra(MaterialCamera.ERROR_EXTRA);
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getFileFromCacheAndUpload(String fileName) {

        File file = new File(getCacheDir(), fileName);
        if (file.exists()) {

            try {
                FileMetaData metadata = new FileMetaData(fileName + ".jpg");
                metadata.setPublic(false);  //set the file not to be publicly accessible
                metadata.setFileName(fileName + ".jpg");

                FileInputStream fin = new FileInputStream(file);
                UploaderProgressListener upl = new UploaderProgressListener() {
                    @Override
                    public void onSuccess(Void result) {
                        Log.i(TAG, "upload success!");
                        Toast.makeText(TakeImageActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Throwable error) {
                        Log.i(TAG, "upload progress change!");
                    }

                    @Override
                    public void progressChanged(MediaHttpUploader uploader) throws IOException {
                        Log.i(TAG, "progressChanged: " + uploader.getUploadState());
                    }
                };
                mKinveyClient.file().upload(metadata, fin, upl);
            } catch (Exception e) {
                Log.e(TAG, "getFileFromCacheAndUpload: " + e);
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission granted
                    camera.stillShot().start(CAMERA_RQ);
                } else {
                    Toast.makeText(this, "Camera access permission needed to take image", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

}
