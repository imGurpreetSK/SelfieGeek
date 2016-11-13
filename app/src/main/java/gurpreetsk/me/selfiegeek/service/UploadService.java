package gurpreetsk.me.selfiegeek.service;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.java.core.MediaHttpUploader;
import com.kinvey.java.core.UploaderProgressListener;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import gurpreetsk.me.selfiegeek.KinveyApplication;

import static gurpreetsk.me.selfiegeek.utils.Constants.SERVICE_EXTRA;
import static gurpreetsk.me.selfiegeek.utils.Constants.SERVICE_FILENAME_EXTRA;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class UploadService extends IntentService {

    private static final String TAG = "UploadService";

    public UploadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Uri imagePath = Uri.parse(intent.getStringExtra(SERVICE_EXTRA));
        String fileName = intent.getStringExtra(SERVICE_FILENAME_EXTRA);

        File file = new File(imagePath.getPath());
        Log.i(TAG, "getFileFromCacheAndUpload: Filename " + imagePath.toString());

        try {
            FileMetaData metadata = new FileMetaData(fileName + ".jpg");
            metadata.setPublic(false);  //set the file not to be publicly accessible
            metadata.setFileName(fileName + ".jpg");
            Log.i(TAG, "getFileFromCacheAndUpload: Metadata created");

            FileInputStream fin = new FileInputStream(file);
            Log.i(TAG, "getFileFromCacheAndUpload: FileInputStream created");
            UploaderProgressListener upl = new UploaderProgressListener() {
                @Override
                public void onSuccess(Void result) {
                    Log.i(TAG, "upload success!");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadService.this, "File upload successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Throwable error) {
                    Log.i(TAG, "upload progress change!");
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(UploadService.this, "File upload successful", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void progressChanged(MediaHttpUploader uploader) throws IOException {
                    Log.i(TAG, "progressChanged: " + uploader.getUploadState());
                }
            };
            new Client.Builder(getApplicationContext()).build().file().upload(metadata, fin, upl);
        } catch (Exception e) {
            Log.e(TAG, "getFileFromCacheAndUpload: " + e);
            e.printStackTrace();
        }
    }

}
