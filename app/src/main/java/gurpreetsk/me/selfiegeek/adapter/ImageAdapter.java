package gurpreetsk.me.selfiegeek.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.ui.CustomImageView;
import id.zelory.compressor.Compressor;

/**
 * Created by Gurpreet on 12/11/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context context;
    private ArrayList<File> images;

    public ImageAdapter(Context context, ArrayList<File> images) {
        this.context = context;
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_element, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageURI(Uri.fromFile(Compressor.getDefault(context).compressToFile(images.get(position))));
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
