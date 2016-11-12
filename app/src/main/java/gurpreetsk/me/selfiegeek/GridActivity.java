package gurpreetsk.me.selfiegeek;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyPingCallback;

public class GridActivity extends AppCompatActivity {

    private static final String TAG = "GridActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

    }
}
