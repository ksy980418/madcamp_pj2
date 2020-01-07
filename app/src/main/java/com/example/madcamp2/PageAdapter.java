package com.example.madcamp2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class PageAdapter extends FragmentPagerAdapter {

    private int numoftabs;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numoftabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentOne();
            case 1:
                return new FragmentTwo();
            case 2:
                return new FragmentThree();

        }
        return null;
    }

    @Override
    public int getCount(){
        return numoftabs;
    }

    public int getItemPosition(@NonNull Object object){
        return POSITION_NONE;
    }

}