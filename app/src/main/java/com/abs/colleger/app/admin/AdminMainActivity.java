package com.abs.colleger.app.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abs.colleger.app.R;
import com.abs.colleger.app.admin.profile.AdminProfile;
import com.abs.colleger.app.admin.timetable.Timetable;
import com.abs.colleger.app.student.UserMainActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.elevation.SurfaceColors;
import com.google.firebase.auth.FirebaseAuth;

import com.abs.colleger.app.admin.career.Career;
import com.abs.colleger.app.admin.club.Club;
import com.abs.colleger.app.admin.contact.Contact;
import com.abs.colleger.app.admin.event.Events;
import com.abs.colleger.app.admin.file.Files;
import com.abs.colleger.app.admin.lostfound.LostFound;

public class AdminMainActivity extends AppCompatActivity implements View.OnClickListener{

    MaterialCardView eventCardView, fileCardView, contactCardView,lostFoundCardView, careerCardView, clubCardView, timetableCardView;


    MaterialToolbar toolbar;
    ActionBar a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        toolbar=findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if (isDarkModeEnabled()) { // check if dark mode is enabled
                actionBar.setLogo(R.drawable.colleger_logo_word_night);
            } else {
                actionBar.setLogo(R.drawable.colleger_logo_word);
            }
        }

        eventCardView=findViewById(R.id.addEvent);
        eventCardView.setOnClickListener(this);

        fileCardView=findViewById(R.id.addFile);
        fileCardView.setOnClickListener(this);

        contactCardView=findViewById(R.id.addContact);
        contactCardView.setOnClickListener(this);

        lostFoundCardView=findViewById(R.id.addLost);
        lostFoundCardView.setOnClickListener(this);

        careerCardView=findViewById(R.id.addCareer);
        careerCardView.setOnClickListener(this);

        clubCardView=findViewById(R.id.addClub);
        clubCardView.setOnClickListener(this);

        timetableCardView=findViewById(R.id.addTimetable);
        timetableCardView.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.navigation_drawer_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int res_id= item.getItemId();

        if (res_id==R.id.action_profile){
            Intent intentSignout =new Intent(AdminMainActivity.this, AdminProfile.class);
            startActivity(intentSignout);
        }
        return true;
    }

    private boolean isDarkModeEnabled() {
        int nightModeFlags = getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.addEvent:
                intent = new Intent(AdminMainActivity.this, Events.class);
                startActivity(intent);
                break;
            case R.id.addFile:
                intent =new Intent(AdminMainActivity.this, Files.class);
                startActivity(intent);
                break;
            case R.id.addContact:
                intent =new Intent(AdminMainActivity.this, Contact.class);
                startActivity(intent);
                break;
            case R.id.addLost:
                intent =new Intent(AdminMainActivity.this, LostFound.class);
                startActivity(intent);
                break;
            case R.id.addCareer:
                intent =new Intent(AdminMainActivity.this, Career.class);
                startActivity(intent);
                break;
            case R.id.addClub:
                intent =new Intent(AdminMainActivity.this, Club.class);
                startActivity(intent);
                break;
            case R.id.addTimetable:
                intent =new Intent(AdminMainActivity.this, Timetable.class);
                startActivity(intent);
                break;
        }

    }
}