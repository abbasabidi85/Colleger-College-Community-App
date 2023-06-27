package com.abs.colleger.app.student.timetable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abs.colleger.app.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

public class UserTimetableFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    UserViewPagerAdapter adapter;
    MaterialToolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_timetable, container, false);
        tabLayout=view.findViewById(R.id.user_timetableTabLayout);
        viewPager=view.findViewById(R.id.user_vPager);

        Calendar calendar = Calendar.getInstance();
        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        adapter=new UserViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragment(new UserFragmentMonday(),"Monday");
        adapter.AddFragment(new UserFragmentTuesday(),"Tuesday");
        adapter.AddFragment(new UserFragmentWednesday(),"Wednesday");
        adapter.AddFragment(new UserFragmentThursday(),"Thursday");
        adapter.AddFragment(new UserFragmentFriday(),"Friday");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


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
        return view;
    }

}