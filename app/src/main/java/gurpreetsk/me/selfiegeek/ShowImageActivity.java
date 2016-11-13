package gurpreetsk.me.selfiegeek;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import java.io.File;

import id.zelory.compressor.Compressor;

public class ShowImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);

        Uri fileUri = getIntent().getData();

        ImageView imageView = (ImageView) findViewById(R.id.imageview_large);
        imageView.setImageURI(fileUri);
    }
}
