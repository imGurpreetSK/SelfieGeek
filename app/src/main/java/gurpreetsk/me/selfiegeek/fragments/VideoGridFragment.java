package gurpreetsk.me.selfiegeek.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gurpreetsk.me.selfiegeek.R;
import gurpreetsk.me.selfiegeek.RecordVideoActivity;
import gurpreetsk.me.selfiegeek.adapter.VideoAdapter;

public class VideoGridFragment extends Fragment {

    RecyclerView recyclerView;
    VideoAdapter adapter;
    FloatingActionButton fab;

    public VideoGridFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_video_grid, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.videoRecyclerView);
        fab = (FloatingActionButton) v.findViewById(R.id.fab_record_video);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), RecordVideoActivity.class));
            }
        });
        adapter = new VideoAdapter(getContext());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return v;
    }

}
