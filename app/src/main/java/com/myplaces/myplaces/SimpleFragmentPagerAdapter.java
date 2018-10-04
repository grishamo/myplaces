package com.myplaces.myplaces;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;
    private TabLayout mTabs;
    private ArrayList<Fragment> allFragments = new ArrayList<>();

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm, TabLayout tabs) {
        super(fm);
        mTabs = tabs;
        mContext = context;
    }

    public SimpleFragmentPagerAdapter AddFragment(IPageFragment fm) {
        allFragments.add((Fragment) fm);

        mTabs.addTab(mTabs.newTab().setText(fm.GetPageTitle()).setIcon(fm.GetPageIcon()));
        return this;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        return allFragments.get(position);
    }


    // This determines the number of tabs
    @Override
    public int getCount() {
        return allFragments.size();
    }

    public void SetTabsIcon() {
        int index = 0;
        for(Fragment fm : allFragments)
        {
            mTabs.getTabAt(index).setIcon(((IPageFragment)fm).GetPageIcon());
            index++;
        }
    }

}
