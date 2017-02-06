package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameters);

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
