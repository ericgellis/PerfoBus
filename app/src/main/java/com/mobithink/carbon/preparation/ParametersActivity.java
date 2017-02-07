package com.mobithink.carbon.preparation;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.mobithink.carbon.R;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ParametersActivity extends Activity {

    SeekBar mFrequencySeekBar;
    SeekBar mRadiusSeekBar;

    TextView mFrequencyNumberTextView;
    TextView mRadiusNumberTextView;

    Toolbar mParametersToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        mParametersToolBar = (Toolbar) findViewById(R.id.parametersToolBar);
        mParametersToolBar.setTitle("Paramètres");
        mParametersToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mFrequencySeekBar = (SeekBar) findViewById(R.id.frequency_seekbar);
        mRadiusSeekBar = (SeekBar) findViewById(R.id.radius_seekbar);

        mFrequencyNumberTextView = (TextView) findViewById(R.id.frequencyNumber);
        mRadiusNumberTextView = (TextView) findViewById(R.id.radiusNumber);

        mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mFrequencyNumberTextView.setText("" + progressChanged);
            }
        });

        mRadiusSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mRadiusNumberTextView.setText("" + progressChanged);
            }
        });


    }
}
