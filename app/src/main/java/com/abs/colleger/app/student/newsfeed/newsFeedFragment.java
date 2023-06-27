package com.abs.colleger.app.student.newsfeed;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class newsFeedFragment extends Fragment {

    private RecyclerView userEventRecycler;
    private ProgressBar userEventProgressBar;
    private ArrayList<UserEventData> list;
    private UserNewsfeedAdapter adapter;
    private DatabaseReference reference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_news_feed, container, false);
        userEventRecycler = view.findViewById(R.id.userEventRecycler);
        userEventProgressBar =view.findViewById(R.id.userEventProgressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Events");

        userEventRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        userEventRecycler.setHasFixedSize(true);

        getEvent();

        return view;

    }

    private void getEvent() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserEventData data = snapshot.getValue(UserEventData.class);
                    list.add(0,data);
                }
                adapter=new UserNewsfeedAdapter(getContext(), list);
                adapter.notifyDataSetChanged();
                userEventProgressBar.setVisibility(View.GONE);
                userEventRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                userEventProgressBar.setVisibility(View.GONE);

                Snackbar.make(getContext(), getView(),  databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });
    }
}