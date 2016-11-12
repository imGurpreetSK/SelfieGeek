package gurpreetsk.me.selfiegeek.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import gurpreetsk.me.selfiegeek.R;

/**
 * Created by Gurpreet on 12/11/16.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder>{

    private Context context;

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_image_element, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        holder.imageView.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_launcher));
    }

    @Override
    public int getItemCount() {
        return 8;
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;

        ImageViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.gridImageView);
        }
    }

}
