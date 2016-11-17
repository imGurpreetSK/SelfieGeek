package gurpreetsk.me.selfiegeek.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.java.Query;
import com.kinvey.java.core.DownloaderProgressListener;
import com.kinvey.java.core.MediaHttpDownloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

        Query q = new Query();
        Object[] find = {"{\"creator\":\"" + userID + "\"}"};
        q.all("_acl", find);

        try {
            File file = new File(getApplicationContext().getCacheDir().getPath());
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            FileOutputStream fos = new FileOutputStream(file + "/" + timeStamp + ".jpg");
            new Client.Builder(getApplicationContext()).build().file().download(q,
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

                            new Handler(Looper.getMainLooper()) {
                                @Override
                                public void handleMessage(Message message) {
                                    Toast.makeText(DownloadService.this, "success: not-much :(", Toast.LENGTH_SHORT).show();
                                }
                            };

                            throwable.printStackTrace();
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
