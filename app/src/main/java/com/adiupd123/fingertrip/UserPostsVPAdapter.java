package com.adiupd123.fingertrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class UserPostsVPAdapter extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
    private final ArrayList<String> fragmentTitleArrayList = new ArrayList<>();

    public UserPostsVPAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    // Gets the position of tabbedItem selected from Tabbed Layout and
    // returns the fragment corresponding to the position from the arraylist
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    //Gets the no. of fragments
    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    public void addFragment(Fragment fragment, String title){
        fragmentArrayList.add(fragment);
        fragmentTitleArrayList.add(title);
    }
    // Gets the position of the tabbedItem selected from Tabbed Layout and
    // returns the fragment's title corresponding to the position from the arraylist
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleArrayList.get(position);
    }
}
