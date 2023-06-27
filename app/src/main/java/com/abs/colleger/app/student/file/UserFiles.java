package com.abs.colleger.app.student.file;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abs.colleger.app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

public class UserFiles extends AppCompatActivity {

    private NestedScrollView userFileLayout;
    private RecyclerView clubs,societies;
    private LinearLayout clubsNoData, societiesNoData;
    private UserFilesViewPagerAdapter adapter;

    TabLayout tabLayout;
    ViewPager viewPager;

    MaterialToolbar toolbar;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_files);

        tabLayout=findViewById(R.id.user_fileTabLayout);
        viewPager=findViewById(R.id.user_fileVPager);

        adapter=new UserFilesViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new UserPreviousPapersFragment(),"Questions");
        adapter.AddFragment(new UserNoticesFragment(),"Notices");
        adapter.AddFragment(new UserPerformaFragment(),"Performas");
        adapter.AddFragment(new UserOthersFragment(),"Other");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        toolbar=findViewById(R.id.userFileToolbar);
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