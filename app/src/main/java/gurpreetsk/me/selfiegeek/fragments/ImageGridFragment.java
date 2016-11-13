package gurpreetsk.me.selfiegeek.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.TakeImageActivity;
import gurpreetsk.me.selfiegeek.adapter.ImageAdapter;

public class ImageGridFragment extends Fragment {

    private static final String TAG = "ImageGridFragment";

    RecyclerView recyclerView;
    ImageAdapter adapter;
    FloatingActionButton fab;

    ArrayList<File> imageList = new ArrayList<>();

    public ImageGridFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_grid, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.imageRecyclerView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_take_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TakeImageActivity.class));
            }
        });
        adapter = new ImageAdapter(getContext(), imageList, getActivity());

        getImages();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        imageList.clear();
        getImages();
    }

    private void getImages() {
        //TODO: un-hardcode string
        File dir = new File(getString(R.string.CACHE));
        Log.i(TAG, "getImages: " + dir.getName());
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                if (f.getName().contains("IMG"))
                    imageList.add(f);
            }
            adapter.notifyDataSetChanged();
        }
    }

}
