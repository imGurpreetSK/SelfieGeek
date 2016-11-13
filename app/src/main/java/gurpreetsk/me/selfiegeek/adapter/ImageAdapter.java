package gurpreetsk.me.selfiegeek.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.ShowImageActivity;
import gurpreetsk.me.selfiegeek.ui.CustomImageView;
import id.zelory.compressor.Compressor;

import static gurpreetsk.me.selfiegeek.utils.Constants.IMAGE_INTENT_EXTRA;

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
        holder.imageView.setImageURI(Uri.fromFile(Compressor.getDefault(context).compressToFile(images.get(position))));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageIntent = new Intent(context, ShowImageActivity.class);

                //TODO: Image not showing after transaction
                imageIntent.putExtra(IMAGE_INTENT_EXTRA, images.get(holder.getAdapterPosition()).toString());
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(activity, holder.imageView , context.getString(R.string.transition_name));
                context.startActivity(imageIntent, options.toBundle());
            }
        });
        holder.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                Toast.makeText(context, "Ask to delete!", Toast.LENGTH_SHORT).show();

                return true;
            }
        });
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
