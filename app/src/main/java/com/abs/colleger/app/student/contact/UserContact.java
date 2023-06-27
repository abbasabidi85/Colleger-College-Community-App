package com.abs.colleger.app.student.contact;


import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import com.abs.colleger.app.R;

public class UserContact extends AppCompatActivity {

    ProgressBar progressBar;
    NestedScrollView userContactLayout;
    private RecyclerView biochemDept,botanyDept, computerDept;
    private LinearLayout biochemDeptNoData, botanyDeptNoData, computerDeptNoData;
    private List<UserContactData> list1, list2, list3;
    private UserContactAdapter adapter;

    private DatabaseReference reference, dbRef;

    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_contacts);

        toolbar=findViewById(R.id.facultyContactToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userContactLayout=findViewById(R.id.userContactScrollView);
        progressBar=findViewById(R.id.userContactProgressBar);

        computerDept=findViewById(R.id.userComputerDept);
        computerDeptNoData=findViewById(R.id.userComputerDeptNoData);

        biochemDept=findViewById(R.id.userBiochemDept);
        biochemDeptNoData=findViewById(R.id.userBiochemDeptNoData);

        botanyDept=findViewById(R.id.userBotanyDept);
        botanyDeptNoData=findViewById(R.id.userBotanyDeptNoData);

        reference= FirebaseDatabase.getInstance().getReference().child("Faculty");

        progressBar.setVisibility(View.VISIBLE);

        computerDept();
        biochemDept();
        botanyDept();

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void computerDept() {
        dbRef=reference.child("Department of Computer Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    computerDeptNoData.setVisibility(View.VISIBLE);
                    computerDept.setVisibility(View.GONE);
                }else{

                    computerDeptNoData.setVisibility(View.GONE);
                    computerDept.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserContactData data = snapshot.getValue(UserContactData.class);
                        list1.add(data);
                    }
                    computerDept.setHasFixedSize(true);
                    computerDept.setLayoutManager((new LinearLayoutManager(UserContact.this)));
                    adapter=new UserContactAdapter(list1, UserContact.this);
                    computerDept.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserContact.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void biochemDept() {
        dbRef=reference.child("Department of Biochemistry");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    biochemDeptNoData.setVisibility(View.VISIBLE);
                    biochemDept.setVisibility(View.GONE);
                }else{

                    biochemDeptNoData.setVisibility(View.GONE);
                    biochemDept.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserContactData data = snapshot.getValue(UserContactData.class);
                        list2.add(data);
                    }
                    biochemDept.setHasFixedSize(true);
                    biochemDept.setLayoutManager((new LinearLayoutManager(UserContact.this)));
                    adapter=new UserContactAdapter(list2, UserContact.this);
                    biochemDept.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserContact.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void botanyDept() {
        dbRef=reference.child("Department of Botany");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if(!dataSnapshot.exists()){
                    botanyDeptNoData.setVisibility(View.VISIBLE);
                    botanyDept.setVisibility(View.GONE);
                }else{

                    botanyDeptNoData.setVisibility(View.GONE);
                    botanyDept.setVisibility(View.VISIBLE);

                    for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                        UserContactData data = snapshot.getValue(UserContactData.class);
                        list3.add(data);
                    }
                    botanyDept.setHasFixedSize(true);
                    botanyDept.setLayoutManager((new LinearLayoutManager(UserContact.this)));
                    adapter=new UserContactAdapter(list3, UserContact.this);
                    botanyDept.setAdapter(adapter);
                }
                progressBar.setVisibility(View.GONE);
                userContactLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserContact.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

}