package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kinvey.android.Client;
import com.kinvey.android.callback.KinveyDeleteCallback;
import com.kinvey.java.model.FileMetaData;
import com.kinvey.java.model.KinveyDeleteResponse;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.ShowImageActivity;
import gurpreetsk.me.selfiegeek.ui.CustomImageView;
import id.zelory.compressor.Compressor;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.IMAGE_INTENT_EXTRA;

/**
 * Created by Gurpreet on 12/11/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private Activity activity;
    private ArrayList<File> images;

    public ImageAdapter(Context context, ArrayList<File> images, Activity activity) {
        this.context = context;
        this.images = images;
        this.activity = activity;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_element, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        try {
            File file = images.get(position);
            if(file.exists()) {
                holder.imageView.setImageURI(Uri.fromFile(Compressor.getDefault(context).compressToFile(file)));
                holder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent imageIntent = new Intent(context, ShowImageActivity.class);

                        imageIntent.putExtra(IMAGE_INTENT_EXTRA, images.get(holder.getAdapterPosition()).toString());
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation(activity, holder.imageView, context.getString(R.string.transition_name));
                        try {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                context.startActivity(imageIntent, options.toBundle());
                            } else
                                context.startActivity(imageIntent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true);
                        builder.setTitle(context.getString(R.string.dialog_delete_title));
                        builder.setMessage(context.getString(R.string.dialog_delete_desc));
                        builder.setPositiveButton(context.getString(R.string.delete_everywhere), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                                final String fileName = images.get(holder.getAdapterPosition()).getName();
                                FileMetaData metadata = new FileMetaData(fileName);
                                metadata.setPublic(false);  //set the file not to be publicly accessible
                                metadata.setFileName(fileName);

                                new Client.Builder(context).build().file().delete(metadata, new KinveyDeleteCallback() {
                                    @Override
                                    public void onSuccess(KinveyDeleteResponse kinveyDeleteResponse) {
                                        File dir = new File(context.getString(R.string.CACHE));
                                        if (dir.exists()) {
                                            for (File f : dir.listFiles()) {
                                                if (f.getName().contentEquals(fileName)) {
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
                                .setNegativeButton(context.getString(R.string.delete_from_device), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        final String fileName = images.get(holder.getAdapterPosition()).getName();
                                        File dir = new File(context.getString(R.string.CACHE));
                                        if (dir.exists()) {
                                            for (File f : dir.listFiles()) {
                                                if (f.getName().equals(fileName)) {
                                                    f.delete();
                                                }
                                            }
                                        }
                                        Toast.makeText(context, "File deleted from device!", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                });
                        builder.create().show();

                        return true;
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class ImageViewHolder extends RecyclerView.ViewHolder {

        CustomImageView imageView;

        ImageViewHolder(View v) {
            super(v);
            imageView = (CustomImageView) v.findViewById(R.id.gridImageView);
        }
    }

}
