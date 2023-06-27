package com.abs.colleger.app.admin.lostfound;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;
import com.abs.colleger.app.admin.lostfound.LostFoundAdapter;
import com.abs.colleger.app.admin.lostfound.LostFoundData;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LostFragment extends Fragment {
    LinearLayout LostFoundLayout;
    private final static String TAG = "LostFragment";
    View view;
    private RecyclerView getLostRecycler;
    private ArrayList<LostFoundData> list;
    private LostFoundAdapter adapter;
    private DatabaseReference reference;

    public LostFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_lost,container, false);

        reference = FirebaseDatabase.getInstance().getReference().child("Lost & Found").child("Lost");

        getLostRecycler=view.findViewById(R.id.fragmentLost);
        getLostRecycler();
        return view;
    }

    private void getLostRecycler() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    LostFoundData data = snapshot.getValue(LostFoundData.class);
                    list.add(0,data);
                }

                getLostRecycler.setHasFixedSize(true);
                getLostRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
                adapter=new LostFoundAdapter(getContext(), list);
                getLostRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Snackbar.make(LostFoundLayout, databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });
    }
}
