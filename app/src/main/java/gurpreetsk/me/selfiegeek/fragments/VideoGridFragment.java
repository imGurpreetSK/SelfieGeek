package gurpreetsk.me.selfiegeek.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import gurpreetsk.me.selfiegeek.RecordVideoActivity;
import gurpreetsk.me.selfiegeek.adapter.VideoAdapter;
import gurpreetsk.me.selfiegeek.service.DownloadService;

import static gurpreetsk.me.selfiegeek.utils.KeyConstants.IMAGE_BROADCAST;
import static gurpreetsk.me.selfiegeek.utils.KeyConstants.VIDEO_BROADCAST;

public class VideoGridFragment extends Fragment {

    private static final String TAG = "VideoGridFragment";

    RecyclerView recyclerView;
    VideoAdapter adapter;
    FloatingActionButton fab;
    TextView empty;
    SwipeRefreshLayout swipeRefreshLayout;
    VideoUpdateReceiver videoUpdateReceiver;

    ArrayList<File> videoList = new ArrayList<>();

    public VideoGridFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        videoUpdateReceiver = new VideoUpdateReceiver();
        IntentFilter filter = new IntentFilter(VIDEO_BROADCAST);
        getContext().registerReceiver(videoUpdateReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(videoUpdateReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_grid, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.videoRecyclerView);
        empty = (TextView) v.findViewById(R.id.empty_imageRV);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_record_video);
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.videoSwipeContainer);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RecordVideoActivity.class));
            }
        });
        adapter = new VideoAdapter(getContext(), getActivity(), videoList, recyclerView);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                videoList.clear();
                getVideos();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent),
                getResources().getColor(R.color.colorPrimary),
                getResources().getColor(R.color.colorPrimaryDark),
                getResources().getColor(R.color.colorPrimaryLight));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getVideos();

        if (videoList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            empty.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        videoList.clear();
        getVideos();
    }

    private void getVideos() {
        File dir = new File(getString(R.string.CACHE));
        Log.i(TAG, "getVideos: " + dir.getName());
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                if (f.getName().contains("VID"))
                    videoList.add(f);
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
//                TextView tv = new TextView(getContext());
//                tv.setText("Created By Gurpreet Singh\nfor SocialCops task");
//                tv.setGravity(Gravity.CENTER);
                Toast toast = Toast.makeText(getContext(), "Created By Gurpreet Singh\nfor SocialCops task", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                break;
        }
        return true;
    }


    private class VideoUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: Inside broadcast receiver");
            //TODO: THE FIRST VIDEO SHOT NOT SHOWING IN FRAGMENT UNLESS ACTIVITY DESTROYED
            adapter.notifyDataSetChanged();
        }
    }

}
