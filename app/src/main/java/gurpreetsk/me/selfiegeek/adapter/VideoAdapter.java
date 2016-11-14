package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.model.KinveyDeleteResponse;

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
    public void onBindViewHolder(final VideoHolder holder, int position) {

        holder.videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });
        holder.videoView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(context.getString(R.string.dialog_delete_title));
                builder.setMessage(context.getString(R.string.dialog_delete_desc));
                builder.setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        final String fileName = videos.get(holder.getAdapterPosition()).getName();
                        FileMetaData metadata = new FileMetaData(fileName);
                        metadata.setPublic(false);  //set the file not to be publicly accessible
                        metadata.setFileName(fileName);

                        new Client.Builder(context).build().file().delete(metadata, new KinveyDeleteCallback() {
                            @Override
                            public void onSuccess(KinveyDeleteResponse kinveyDeleteResponse) {
                                File dir = new File(context.getString(R.string.CACHE));
                                if (dir.exists()) {
                                    for (File f : dir.listFiles()) {
                                        if(f.getName().contentEquals(fileName)) {
                                            f.delete();
                                        }
                                    }
                                }
                                Toast.makeText(context, "File deleted!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Toast.makeText(context, "Oops! Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                        .setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                builder.create().show();

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
