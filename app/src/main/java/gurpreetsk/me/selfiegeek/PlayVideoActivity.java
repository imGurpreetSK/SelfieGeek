package gurpreetsk.me.selfiegeek;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.easyvideoplayer.EasyVideoCallback;
import com.afollestad.easyvideoplayer.EasyVideoPlayer;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.VIDEO_INTENT_EXTRA;

public class PlayVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);

        String videoUrl = getIntent().getStringExtra(VIDEO_INTENT_EXTRA);
        if (videoUrl != null) {
            EasyVideoPlayer videoPlayer = (EasyVideoPlayer) findViewById(R.id.videoPlayer);
            videoPlayer.setAutoPlay(true);
            videoPlayer.setSource(Uri.parse(videoUrl));

            videoPlayer.setCallback(new EasyVideoCallback() {
                @Override
                public void onStarted(EasyVideoPlayer player) {
                }

                @Override
                public void onPaused(EasyVideoPlayer player) {
                    player.pause();
                }

                @Override
                public void onPreparing(EasyVideoPlayer player) {
                }

                @Override
                public void onPrepared(EasyVideoPlayer player) {
                }

                @Override
                public void onBuffering(int percent) {
                }

                @Override
                public void onError(EasyVideoPlayer player, Exception e) {
                    Toast.makeText(PlayVideoActivity.this, "An error occurred\nPlease try again", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCompletion(EasyVideoPlayer player) {
//                player.reset();
                }

                @Override
                public void onRetry(EasyVideoPlayer player, Uri source) {
                }

                @Override
                public void onSubmit(EasyVideoPlayer player, Uri source) {
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

}
