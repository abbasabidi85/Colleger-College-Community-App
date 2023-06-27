package com.abs.colleger.app.admin.career;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.abs.colleger.app.R;

public class Career extends AppCompatActivity {

    private RecyclerView placementInfo,certificateCourses;
    private LinearLayout placementInfoNoData, certificateCoursesNoData;
    private List<PlacementData> list1, list2;
    private PlacementAdapter adapter;
    MaterialToolbar toolbar;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career);

        toolbar=findViewById(R.id.careerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addCareerFab;

        placementInfo=findViewById(R.id.placementInfo);
        placementInfoNoData=findViewById(R.id.placementInfoNoData);

        certificateCourses=findViewById(R.id.certificateCourses);
        certificateCoursesNoData=findViewById(R.id.certificateCoursesNoData);

        addCareerFab=findViewById(R.id.add_fab_career);

        reference= FirebaseDatabase.getInstance().getReference().child("Career");

        placementInfo();
        certificateCourse();

        addCareerFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Career.this, AddCareerPlacement.class));
            }
        });

        }


    private void placementInfo() {
        dbRef=reference.child("Placements");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    placementInfoNoData.setVisibility(View.VISIBLE);
                    placementInfo.setVisibility(View.GONE);
                }else{

                    placementInfoNoData.setVisibility(View.GONE);
                    placementInfo.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        PlacementData data = snapshot.getValue(PlacementData.class);
                        list1.add(data);
                    }
                    placementInfo.setHasFixedSize(true);
                    placementInfo.setLayoutManager((new LinearLayoutManager(Career.this)));
                    adapter=new PlacementAdapter(list1, Career.this, "Placements");
                    placementInfo.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Career.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void certificateCourse() {
        dbRef=reference.child("Certificate Courses");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    certificateCoursesNoData.setVisibility(View.VISIBLE);
                    certificateCourses.setVisibility(View.GONE);
                }else{

                    certificateCoursesNoData.setVisibility(View.GONE);
                    certificateCourses.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        PlacementData data = snapshot.getValue(PlacementData.class);
                        list2.add(data);
                    }
                    certificateCourses.setHasFixedSize(true);
                    certificateCourses.setLayoutManager((new LinearLayoutManager(Career.this)));
                    adapter=new PlacementAdapter(list2, Career.this, "Certificate Courses");
                    certificateCourses.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Career.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}