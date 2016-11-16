package gurpreetsk.me.selfiegeek.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.TakeImageActivity;
import gurpreetsk.me.selfiegeek.adapter.ImageAdapter;
import gurpreetsk.me.selfiegeek.service.DownloadService;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.IMAGE_BROADCAST;

public class ImageGridFragment extends Fragment {

    private static final String TAG = "ImageGridFragment";

    RecyclerView recyclerView;
    ImageAdapter adapter;
    FloatingActionButton fab;
    SwipeRefreshLayout swipeRefreshLayout;
    ImageUpdateReceiver imageUpdateReceiver;

    ArrayList<File> imageList = new ArrayList<>();

    public ImageGridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        imageUpdateReceiver = new ImageUpdateReceiver();
        IntentFilter filter = new IntentFilter(IMAGE_BROADCAST);
        getContext().registerReceiver(imageUpdateReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(imageUpdateReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_image_grid, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.imageRecyclerView);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        TextView empty = (TextView) v.findViewById(R.id.empty_imageRV);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_take_image);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), TakeImageActivity.class));
            }
        });
        adapter = new ImageAdapter(getContext(), imageList, getActivity(), recyclerView);

        getImages();

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!imageList.isEmpty())
                    imageList.clear();
                getImages();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryLight));

        if (imageList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!imageList.isEmpty())
            imageList.clear();
        getImages();
        adapter.notifyDataSetChanged();
    }

    private void getImages() {
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                //TODO: make download stuff and store in cache work properly
                Intent downloadIntent = new Intent(getContext(), DownloadService.class);
                getContext().startService(downloadIntent);
                break;
            case R.id.settings:
                Toast toast = Toast.makeText(getContext(), "Created By Gurpreet Singh\nfor SocialCops task", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
        return true;
    }


    private class ImageUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: Inside broadcast receiver");
            //TODO: THE FIRST IMAGE CLICKED NOT SHOWING IN FRAGMENT UNLESS ACTIVITY DESTROYED
            adapter.notifyDataSetChanged();
        }
    }

}
