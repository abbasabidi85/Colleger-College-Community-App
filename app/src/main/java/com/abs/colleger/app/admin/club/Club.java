package com.abs.colleger.app.admin.club;

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

public class Club extends AppCompatActivity {

    private RecyclerView clubs,societies;
    private LinearLayout clubsNoData, societiesNoData;
    private List<ClubData> list1, list2;
    private ClubAdapter adapter;
    MaterialToolbar toolbar;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        toolbar=findViewById(R.id.clubToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton addClubFab;

        clubs=findViewById(R.id.clubs);
        clubsNoData=findViewById(R.id.clubsNoData);

        societies=findViewById(R.id.societies);
        societiesNoData=findViewById(R.id.societiesNoData);

        addClubFab=findViewById(R.id.add_fab_club);

        reference= FirebaseDatabase.getInstance().getReference().child("Club & Society");

        clubs();
        societies();

        addClubFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Club.this, AddClub.class));
            }
        });

    }

    private void clubs() {
        dbRef=reference.child("Club");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    clubsNoData.setVisibility(View.VISIBLE);
                    clubs.setVisibility(View.GONE);
                }else{

                    clubsNoData.setVisibility(View.GONE);
                    clubs.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        ClubData data = snapshot.getValue(ClubData.class);
                        list1.add(data);
                    }
                    clubs.setHasFixedSize(true);
                    clubs.setLayoutManager((new LinearLayoutManager(Club.this)));
                    adapter=new ClubAdapter(list1, Club.this, "UserClub");
                    clubs.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Club.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void societies() {
        dbRef=reference.child("Society");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    societiesNoData.setVisibility(View.VISIBLE);
                    societies.setVisibility(View.GONE);
                }else{

                    societiesNoData.setVisibility(View.GONE);
                    societies.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        ClubData data = snapshot.getValue(ClubData.class);
                        list2.add(data);
                    }
                    societies.setHasFixedSize(true);
                    societies.setLayoutManager((new LinearLayoutManager(Club.this)));
                    adapter=new ClubAdapter(list2, Club.this, "Societies");
                    societies.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Club.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

}