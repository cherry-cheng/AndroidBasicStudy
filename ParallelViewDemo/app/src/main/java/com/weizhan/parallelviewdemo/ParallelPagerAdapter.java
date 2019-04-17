package com.weizhan.parallelviewdemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2019/4/17.
 */

public class ParallelPagerAdapter extends FragmentPagerAdapter {
    private List<ParallelFragment> fragments;

    public ParallelPagerAdapter(FragmentManager fm,
                                List<ParallelFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
