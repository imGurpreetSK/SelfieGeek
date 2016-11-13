package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{

    private Context context;
    private Activity activity;
    private ArrayList<File> videos;

    public VideoAdapter(Context context, Activity activity, ArrayList<File> videos) {
        this.context = context;
        this.activity = activity;
        this.videos = videos;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        holder.videoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context, "Ask to delete!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });

    }

    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_video_element, parent, false);
        return new VideoHolder(view);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    class VideoHolder extends RecyclerView.ViewHolder{

        VideoView videoView;

        VideoHolder(View v) {
            super(v);
            videoView = (VideoView) v.findViewById(R.id.gridVideoView);
        }
    }
}
