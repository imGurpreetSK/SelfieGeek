package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.PlayVideoActivity;
import gurpreetsk.me.selfiegeek.R;

import static gurpreetsk.me.selfiegeek.utils.Constants.VIDEO_INTENT_EXTRA;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder>{

    private Context context;
    private Activity activity;
    private ArrayList<File> videos;

    private static final String TAG = "VideoAdapter";

    public VideoAdapter(Context context, Activity activity, ArrayList<File> videos) {
        this.context = context;
        this.activity = activity;
        this.videos = videos;
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {

        Glide.with(context)
                .load(videos.get(holder.getAdapterPosition())) // or URI/path
                .into(holder.videoThumbnail); //imageview to set thumbnail to

        holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(TAG, "onClick: Video element clicked" );

                Intent intent = new Intent(context, PlayVideoActivity.class);
                intent.putExtra(VIDEO_INTENT_EXTRA ,videos.get(holder.getAdapterPosition()).toString());
                context.startActivity(intent);

            }
        });
        holder.videoThumbnail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setCancelable(true);
                builder.setTitle(context.getString(R.string.dialog_delete_title));
                builder.setMessage(context.getString(R.string.dialog_delete_desc));
                builder.setPositiveButton(context.getString(R.string.delete_everywhere), new DialogInterface.OnClickListener() {
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
                                        Log.i(TAG, "onSuccess: Video "+fileName);
                                        if(f.getName().contentEquals(fileName)) {
                                            Log.i(TAG, "onSuccess: Video "+fileName);
                                            f.delete();
                                        }
                                    }
                                }
                                Toast.makeText(context, "File deleted!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Throwable throwable) {
                                Toast.makeText(context, "Oops! Something went wrong\nPlease try again", Toast.LENGTH_SHORT).show();
                                throwable.printStackTrace();
                            }
                        });
                    }
                })
                        .setNegativeButton(context.getString(R.string.delete_from_device), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String fileName = videos.get(holder.getAdapterPosition()).getName();
                                File dir = new File(context.getString(R.string.CACHE));
                                if (dir.exists()) {
                                    for (File f : dir.listFiles()) {
                                        Log.i(TAG, "onSuccess: Video "+fileName);
                                        if(f.getName().equals(fileName)) {
                                            Log.i(TAG, "onSuccess: Video "+fileName);
                                            f.delete();
                                        }
                                    }
                                }
                                Toast.makeText(context, "File deleted from device!", Toast.LENGTH_SHORT).show();
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

        ImageView videoThumbnail;

        VideoHolder(View v) {
            super(v);
            videoThumbnail = (ImageView) v.findViewById(R.id.gridVideoView);
        }
    }
}
