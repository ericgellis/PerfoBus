package com.mobithink.carbon.consultation;


import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.adapter.TabPagerAdapter;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.mobithink.carbon.preparation.ChoiceLineFromConsultActivity;


/**
 * Created by mplaton on 03/02/2017.
 */

public class ConsultationActivity extends AppCompatActivity {

    private static final String TAG = ConsultationActivity.class.getName();

    private TripDTO mTripDTO;
    private BusLineDTO mBusLineDTO;
    private CityDTO mCityDTO;
    private StationDTO mSelectedDirection;

    public static final int MY_REQUEST_CODE = 0;
    public static final int CONSULT_LINE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consultation);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Tracé"));
        tabLayout.addTab(tabLayout.newTab().setText("Vitesse"));
        tabLayout.addTab(tabLayout.newTab().setText("Evènement"));
        tabLayout.addTab(tabLayout.newTab().setText("Charge"));
        tabLayout.addTab(tabLayout.newTab().setText("Synthèse"));

        tabLayout.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.mobiThinkBlue));

        final ViewPager viewPager =(ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        Bundle extras = getIntent().getExtras();
        mTripDTO = (TripDTO) extras.getSerializable("tripDTO");
        mBusLineDTO = (BusLineDTO) extras.getSerializable("busLineName");
        mCityDTO = (CityDTO) extras.getSerializable("cityName");
        mSelectedDirection = (StationDTO) extras.getSerializable("directionName");
    }

    public TripDTO getmTripDTO() {
        return mTripDTO;
    }

    public void setmTripDTO(TripDTO mTripDTO) {
        this.mTripDTO = mTripDTO;
    }

    @Override
    public void onBackPressed() {

        Intent toConsultPage = new Intent (this, ChoiceLineFromConsultActivity.class);
        this.startActivityForResult(toConsultPage, CONSULT_LINE);
    }
}
