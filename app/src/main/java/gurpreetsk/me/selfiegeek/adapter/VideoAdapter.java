package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
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

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.VIDEO_INTENT_EXTRA;

/**
 * Created by Gurpreet on 13/11/16.
 */

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<File> videos;
    private RecyclerView recyclerView;

    private static final String TAG = "VideoAdapter";

    public VideoAdapter(Context context, Activity activity, ArrayList<File> videos, RecyclerView recyclerView) {
        this.context = context;
        this.activity = activity;
        this.videos = videos;
        this.recyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(final VideoHolder holder, int position) {
        try {
            Glide.with(context)
                    .load(videos.get(holder.getAdapterPosition())) // or URI/path
                    .into(holder.videoThumbnail); //imageview to set thumbnail to

            holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Log.i(TAG, "onClick: Video element clicked");

                    Intent videoIntent = new Intent(context, PlayVideoActivity.class);
                    videoIntent.putExtra(VIDEO_INTENT_EXTRA, videos.get(holder.getAdapterPosition()).toString());

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(activity, holder.videoThumbnail, context.getString(R.string.transition_name));
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            context.startActivity(videoIntent, options.toBundle());
                        } else
                            context.startActivity(videoIntent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                                            Log.i(TAG, "onSuccess: Video " + fileName);
                                            if (f.getName().contentEquals(fileName)) {
                                                Log.i(TAG, "onSuccess: Video " + fileName);
                                                f.delete();
                                                recyclerView.removeViewAt(holder.getAdapterPosition());
                                                recyclerView.getAdapter().notifyDataSetChanged();
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
                                            Log.i(TAG, "onSuccess: Video " + fileName);
                                            if (f.getName().equals(fileName)) {
                                                Log.i(TAG, "onSuccess: Video " + fileName);
                                                f.delete();
                                                recyclerView.removeViewAt(holder.getAdapterPosition());
                                                recyclerView.getAdapter().notifyDataSetChanged();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    class VideoHolder extends RecyclerView.ViewHolder {

        ImageView videoThumbnail;

        VideoHolder(View v) {
            super(v);
            videoThumbnail = (ImageView) v.findViewById(R.id.gridVideoView);
        }
    }
}
