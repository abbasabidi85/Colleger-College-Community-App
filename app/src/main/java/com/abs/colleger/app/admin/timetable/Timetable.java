package com.abs.colleger.app.admin.timetable;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.abs.colleger.app.R;
import com.abs.colleger.app.admin.contact.AddContact;
import com.abs.colleger.app.admin.contact.Contact;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.sql.Time;

public class Timetable extends AppCompatActivity {

    FloatingActionButton fabTimetable;
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    MaterialToolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        toolbar=findViewById(R.id.timetableToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.timetableTabLayout);
        viewPager=findViewById(R.id.vPager);

        adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentMonday(),"Monday");
        adapter.AddFragment(new FragmentTuesday(),"Tuesday");
        adapter.AddFragment(new FragmentWednesday(),"Wednesday");
        adapter.AddFragment(new FragmentThursday(),"Thursday");
        adapter.AddFragment(new FragmentFriday(),"Friday");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        //automatically select current day tab
        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        int tabIndex = currentDayOfWeek - Calendar.MONDAY; // Assuming Monday is the first day (1) in your tabs

        if (tabIndex >= 0 && tabIndex < tabLayout.getTabCount()) {
            TabLayout.Tab tab = tabLayout.getTabAt(tabIndex);
            tab.select();
        } else {
            // Handle the case when the current day is outside the range of tabs (e.g., Saturday or Sunday)
            // You can choose to select the first tab or perform a different action as needed
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
        }



        fabTimetable = findViewById(R.id.addTimetableFab);

        fabTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Timetable.this, AddTimetable.class));
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}