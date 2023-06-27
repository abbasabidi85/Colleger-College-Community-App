package com.abs.colleger.app.admin.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abs.colleger.app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class LostFound extends AppCompatActivity {

    ConstraintLayout LostFoundLayout;
    FloatingActionButton addLostFoundFab;
    private RecyclerView userLostFoundRecycler;
    private ProgressBar userLostFoundProgressBar;
    private ArrayList<LostFoundData> list;
    private LostFoundViewPagerAdapter adapter;
    private DatabaseReference reference;
    MaterialToolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_found);

        tabLayout=findViewById(R.id.lostFoundTabLayout);
        viewPager=findViewById(R.id.lostFoundVPager);

        adapter=new LostFoundViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new LostFragment(),"Lost");
        adapter.AddFragment(new FoundFragment(),"Found");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        toolbar=findViewById(R.id.admin_lostfoundToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addLostFoundFab = findViewById(R.id.addLostFoundFab);
        addLostFoundFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostFound.this, AddLostFound.class));
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}