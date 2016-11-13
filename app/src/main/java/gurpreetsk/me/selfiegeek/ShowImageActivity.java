package gurpreetsk.me.selfiegeek;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;

import id.zelory.compressor.Compressor;

import static gurpreetsk.me.selfiegeek.utils.Constants.IMAGE_INTENT_EXTRA;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Uri fileUri = Uri.parse(getIntent().getStringExtra(IMAGE_INTENT_EXTRA));

        ImageView imageView = (ImageView) findViewById(R.id.imageview_large);
        imageView.setImageURI(Uri.fromFile(Compressor.getDefault(this).compressToFile(new File(fileUri.getPath()))));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
