package com.abs.colleger.app.student.career;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abs.colleger.app.R;
import com.abs.colleger.app.student.club.UserClubAdapter;
import com.abs.colleger.app.student.club.UserClubData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserSkillsFragment extends Fragment {

    private final static String TAG = "JobsFragment";
    View view;
    private RecyclerView getSkills;
    private List<UserCareerData> list1;
    private UserCareerAdapter adapter;
    private DatabaseReference reference, dbRef;

    public UserSkillsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_skills,container, false);


        getSkills=view.findViewById(R.id.fragmentSkills);

        reference= FirebaseDatabase.getInstance().getReference().child("Career");
        getJobs();
        return view;
    }

    private void getJobs() {
        dbRef=reference.child("Certificate Courses");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    getSkills.setVisibility(View.GONE);
                }else{
                    getSkills.setVisibility(View.VISIBLE);
                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserCareerData data = snapshot.getValue(UserCareerData.class);
                        list1.add(data);
                    }
                    getSkills.setHasFixedSize(true);
                    getSkills.setLayoutManager((new LinearLayoutManager(getContext())));
                    adapter=new UserCareerAdapter(list1, getContext());
                    getSkills.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
