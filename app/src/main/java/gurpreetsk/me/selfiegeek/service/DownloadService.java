package gurpreetsk.me.selfiegeek.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.MediaHttpDownloader;
import com.kinvey.java.model.FileMetaData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import gurpreetsk.me.selfiegeek.R;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.SHARED_PREF_KEY;

/**
 * Created by Gurpreet on 15/11/16.
 */

public class DownloadService extends IntentService {

    private static final String TAG = "DownloadService";

    public DownloadService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String userID = getSharedPreferences("gurpreetsk.me.selfiegeek", MODE_PRIVATE).getString(SHARED_PREF_KEY, "GURPREET");
//        String fileName = "IMG_20161113_124347";
//        String QUERY = "SELECT * FROM "
        FileMetaData metadata = new FileMetaData();
//        metadata.setFileName(fileName + ".jpg");
        metadata.setId(userID);
        try {
            File file = new File(getString(R.string.CACHE));
            FileOutputStream fos = new FileOutputStream(file+"/hello.jpg");
            new Client.Builder(getApplicationContext()).build().file().download(metadata,
                    fos,
                    new DownloaderProgressListener() {
                        @Override
                        public void progressChanged(MediaHttpDownloader mediaHttpDownloader) throws IOException {
                        }

                        @Override
                        public void onSuccess(Void aVoid) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DownloadService.this, "success!!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            new Handler().post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DownloadService.this, "success not-much :(", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
