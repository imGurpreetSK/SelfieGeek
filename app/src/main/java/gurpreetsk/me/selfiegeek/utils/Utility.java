package gurpreetsk.me.selfiegeek.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import gurpreetsk.me.selfiegeek.service.UploadService;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.SERVICE_EXTRA;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.SERVICE_FILENAME_EXTRA;

/**
 * Created by Gurpreet on 15/11/16.
 */

public class Utility {

    public static void getFileFromCacheAndUpload(final String fileName, Uri path, Context context) {

        if (path != null) {
            Intent FileUploadIntent = new Intent(context, UploadService.class);
            FileUploadIntent.putExtra(SERVICE_EXTRA, path.toString());
            FileUploadIntent.putExtra(SERVICE_FILENAME_EXTRA, fileName);
            context.startService(FileUploadIntent);
        }
    }

}
