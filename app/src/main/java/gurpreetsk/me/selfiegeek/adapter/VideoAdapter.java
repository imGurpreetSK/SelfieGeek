package gurpreetsk.me.selfiegeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import gurpreetsk.me.selfiegeek.R;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{

    private Context context;

    public VideoAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {
//        holder.videoView.setBackground(context.getResources().getDrawable());
    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_element, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class VideoHolder extends RecyclerView.ViewHolder{

        VideoView videoView;

        VideoHolder(View v) {
            super(v);
            videoView = (VideoView) v.findViewById(R.id.gridVideoView);
        }
    }
}
