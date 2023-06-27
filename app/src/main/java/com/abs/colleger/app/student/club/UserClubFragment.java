package com.abs.colleger.app.student.club;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abs.colleger.app.R;
import com.abs.colleger.app.student.timetable.UserLecture;
import com.abs.colleger.app.student.timetable.UserLectureAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserClubFragment extends Fragment {

    private final static String TAG = "ClubFragment";
    View view;
    private RecyclerView getClubs;
    private List<UserClubData> list1;
    private UserClubAdapter adapter;

    private DatabaseReference reference, dbRef;

    public UserClubFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_club,container, false);

        getClubs=view.findViewById(R.id.userClub);

        reference= FirebaseDatabase.getInstance().getReference().child("Club & Society");
        getClubs();
        return view;
    }

    private void getClubs() {
        dbRef=reference.child("Club");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    getClubs.setVisibility(View.GONE);
                }else{
                    getClubs.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserClubData data = snapshot.getValue(UserClubData.class);
                        list1.add(data);
                    }
                    getClubs.setHasFixedSize(true);
                    getClubs.setLayoutManager((new LinearLayoutManager(getContext())));
                    adapter=new UserClubAdapter(list1, getContext());
                    getClubs.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
