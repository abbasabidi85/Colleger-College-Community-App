package com.abs.colleger.app.admin.contact;

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

public class Contact extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView biochemDept,botanyDept, computerDept;
    private LinearLayout biochemDeptNoData, botanyDeptNoData, computerDeptNoData;
    private List<ContactData> list1, list2, list3;
    private ContactAdapter adapter;
    MaterialToolbar toolbar;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        toolbar=findViewById(R.id.contactToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        computerDept=findViewById(R.id.computerDept);
        computerDeptNoData=findViewById(R.id.computerDeptNoData);

        biochemDept=findViewById(R.id.biochemDept);
        biochemDeptNoData=findViewById(R.id.biochemDeptNoData);

        botanyDept=findViewById(R.id.botanyDept);
        botanyDeptNoData=findViewById(R.id.botanyDeptNoData);

        reference= FirebaseDatabase.getInstance().getReference().child("Faculty");

        computerDept();
        biochemDept();
        botanyDept();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Contact.this, AddContact.class));
            }
        });
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
                        ContactData data = snapshot.getValue(ContactData.class);
                        list1.add(data);
                    }
                    computerDept.setHasFixedSize(true);
                    computerDept.setLayoutManager((new LinearLayoutManager(Contact.this)));
                    adapter=new ContactAdapter(list1, Contact.this,"Department of Computer Science");
                    computerDept.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Contact.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
                        ContactData data = snapshot.getValue(ContactData.class);
                        list2.add(data);
                    }
                    biochemDept.setHasFixedSize(true);
                    biochemDept.setLayoutManager((new LinearLayoutManager(Contact.this)));
                    adapter=new ContactAdapter(list2, Contact.this,"Department of Biochemistry");
                    biochemDept.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Contact.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
                        ContactData data = snapshot.getValue(ContactData.class);
                        list3.add(data);
                    }
                    botanyDept.setHasFixedSize(true);
                    botanyDept.setLayoutManager((new LinearLayoutManager(Contact.this)));
                    adapter=new ContactAdapter(list3, Contact.this,"Department of Botany");
                    botanyDept.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Contact.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}