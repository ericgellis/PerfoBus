package com.mobithink.carbon.consultation.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mobithink.carbon.consultation.fragments.CapacityTab4;
import com.mobithink.carbon.consultation.fragments.EventTab3;
import com.mobithink.carbon.consultation.fragments.ProvisionTab1;
import com.mobithink.carbon.consultation.fragments.SpeedTab2;
import com.mobithink.carbon.consultation.fragments.SummaryTab5;


/**
 * Created by mplaton on 03/02/2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    private int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProvisionTab1();
            case 1:
                return new SpeedTab2();
            case 2:
                return new EventTab3();
            case 3:
                return new CapacityTab4();
            case 4:
                return new SummaryTab5();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
