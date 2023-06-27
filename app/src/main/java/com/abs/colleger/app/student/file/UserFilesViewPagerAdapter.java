package com.abs.colleger.app.student.file;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class UserFilesViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fileFragmentList = new ArrayList<>();
        private final List<String> fileFragmentTitleList = new ArrayList<>();

        public UserFilesViewPagerAdapter(@NonNull FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fileFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fileFragmentTitleList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fileFragmentTitleList.get(position);
        }

        public void AddFragment(Fragment clubfragment, String clubtitle){
            fileFragmentList.add(clubfragment);
            fileFragmentTitleList.add(clubtitle);
        }
    }
