package com.abs.colleger.app.student.lostfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.abs.colleger.app.student.club.UserClubFragment;
import com.abs.colleger.app.student.club.UserClubViewPagerAdapter;
import com.abs.colleger.app.student.club.UserSocietyFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.abs.colleger.app.R;

public class UserLostFound extends AppCompatActivity {

    RelativeLayout userLostFoundLayout;
    private RecyclerView userLostFoundRecycler;
    private ProgressBar userLostFoundProgressBar;
    private ArrayList<UserLostFoundData> list;
    private UserLostFoundViewPagerAdapter adapter;
    private DatabaseReference reference;
    MaterialToolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_lost_found);

        tabLayout=findViewById(R.id.user_lostFoundTabLayout);
        viewPager=findViewById(R.id.user_lostFoundVPager);

        adapter=new UserLostFoundViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new UserLostFragment(),"Lost");
        adapter.AddFragment(new UserFoundFragment(),"Found");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        toolbar=findViewById(R.id.lostFoundToolbar);
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