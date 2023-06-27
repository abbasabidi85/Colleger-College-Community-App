package com.abs.colleger.app.student.complaint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.abs.colleger.app.R;
import com.google.android.material.appbar.MaterialToolbar;

public class UserComplaint extends AppCompatActivity {
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_complaint);

        toolbar=findViewById(R.id.userComplaintToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}