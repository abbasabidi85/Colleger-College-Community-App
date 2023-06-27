package com.abs.colleger.app.admin.event;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class Events extends AppCompatActivity {

    private RecyclerView eventRecycler;
    private ProgressBar progressBar;
    private ArrayList<EventData> list;
    private NewsfeedAdapter adapter;
    private FloatingActionButton addEventFab;
    MaterialToolbar toolbar;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        toolbar=findViewById(R.id.eventsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addEventFab = findViewById(R.id.addEventFab);

        eventRecycler = findViewById(R.id.eventRecycler);
        progressBar =findViewById(R.id.eventProgressBar);

        reference = FirebaseDatabase.getInstance().getReference().child("Events");

        eventRecycler.setLayoutManager(new LinearLayoutManager(this));
        eventRecycler.setHasFixedSize(true);
        
        getEvent();

        addEventFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Events.this, AddEvent.class));
            }
        });
    }

    private void getEvent() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    EventData data = snapshot.getValue(EventData.class);
                    list.add(0,data);
                }
                adapter=new NewsfeedAdapter(Events.this, list);
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
                eventRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);

                Snackbar.make(Events.this, findViewById(R.id.eventLayout), databaseError.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}