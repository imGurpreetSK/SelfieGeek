package gurpreetsk.me.selfiegeek;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import java.io.File;

import id.zelory.compressor.Compressor;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.IMAGE_INTENT_EXTRA;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Uri fileUri = Uri.parse(getIntent().getStringExtra(IMAGE_INTENT_EXTRA));
        File file = new File(fileUri.getPath());

        if (file.exists()) {
            ImageView imageView = (ImageView) findViewById(R.id.imageview_large);
            imageView.setImageURI(Uri.fromFile(Compressor.getDefault(this).compressToFile(file)));
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }
}
