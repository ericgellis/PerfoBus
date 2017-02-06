package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import com.mobithink.carbon.R;
import com.rey.material.widget.Slider;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ParametersActivity extends Activity {

    Slider mFrequencySeekBar;
    SeekBar mRadiusSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

        mFrequencySeekBar = (Slider) findViewById(R.id.frequency_seekbar);
        mRadiusSeekBar = (SeekBar) findViewById(R.id.radius_seekbar);

        /*mFrequencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
                Toast.makeText(ParametersActivity.this,"Frequency seekbar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

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
                Toast.makeText(ParametersActivity.this,"Radius seekbar progress:"+progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });


    }
}
