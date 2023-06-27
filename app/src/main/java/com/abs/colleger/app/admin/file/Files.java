package com.abs.colleger.app.admin.file;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class Files extends AppCompatActivity {

    private FloatingActionButton addFileFab;
    private RecyclerView questionPapersRecycler, noticesRecycler, timetablesRecycler, othersRecycler;
    private DatabaseReference reference, dbRef;
    private List<FileData> list1, list2, list3, list4;
    private FileAdapter adapter;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files);

        toolbar=findViewById(R.id.filesToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addFileFab=findViewById(R.id.addfileFab);
        questionPapersRecycler=findViewById(R.id.questionPapersRecycler);
        noticesRecycler=findViewById(R.id.noticesRecycler);
        timetablesRecycler=findViewById(R.id.timetablesRecycler);
        othersRecycler=findViewById(R.id.othersRecycler);

        reference=FirebaseDatabase.getInstance().getReference().child("pdf");

        addFileFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Files.this, AddFile.class));
            }
        });

        getDataQuestionPapers();
        getDataNotices();
        getDataTimetables();
        getDataOthers();

    }



    private void getDataQuestionPapers() {
        dbRef=reference.child("Previous Question Paper");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    FileData data =snapshot.getValue(FileData.class);
                    list1.add(data);
                }
                adapter=new FileAdapter(Files.this, list1);
                questionPapersRecycler.setLayoutManager(new LinearLayoutManager(Files.this));
                questionPapersRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Files.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataNotices() {
        dbRef=reference.child("Notice");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    FileData data =snapshot.getValue(FileData.class);
                    list2.add(data);
                }
                adapter=new FileAdapter(Files.this, list2);
                noticesRecycler.setLayoutManager(new LinearLayoutManager(Files.this));
                noticesRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Files.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDataTimetables() {
        dbRef=reference.child("Performa");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    FileData data =snapshot.getValue(FileData.class);
                    list3.add(data);
                }
                adapter=new FileAdapter(Files.this, list3);
                timetablesRecycler.setLayoutManager(new LinearLayoutManager(Files.this));
                timetablesRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Files.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDataOthers() {
        dbRef=reference.child("Other");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    FileData data =snapshot.getValue(FileData.class);
                    list4.add(data);
                }
                adapter=new FileAdapter(Files.this, list4);
                othersRecycler.setLayoutManager(new LinearLayoutManager(Files.this));
                othersRecycler.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Files.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}