package com.mobithink.carbon.consultation.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.mobithink.carbon.consultation.fragments.CapacityTab4;
import com.mobithink.carbon.consultation.fragments.EventTab3;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.consultation.fragments.ProvisionTab1;
import com.mobithink.carbon.consultation.fragments.SpeedTab2;
import com.mobithink.carbon.consultation.fragments.SummaryTab5;


/**
 * Created by mplaton on 03/02/2017.
 */

public class TabPagerAdapter extends FragmentPagerAdapter {

    int tabCount;

    public TabPagerAdapter(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.tabCount = numberOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ProvisionTab1 tab1 = new ProvisionTab1();
                return tab1;
            case 1:
                SpeedTab2 tab2 = new SpeedTab2();
                return tab2;
            case 2:
                EventTab3 tab3 = new EventTab3();
                return tab3;
            case 3:
                CapacityTab4 tab4 = new CapacityTab4();
                return tab4;
            case 4:
                SummaryTab5 tab5 = new SummaryTab5();
                return tab5;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }

}
