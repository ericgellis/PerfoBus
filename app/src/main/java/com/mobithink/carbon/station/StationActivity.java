package com.mobithink.carbon.station;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mobithink.carbon.R;

/**
 * Created by mplaton on 02/02/2017.
 */

public class StationActivity extends Activity {

    private Button mDecreaseNumberOfAddedPeopleButton;
    private Button mDecreaseNumberOfRemovedPeopleButton;
    private Button mAddPeopleButton;
    private Button mRemovePeopleButton;
    private Button mChooseEventButton;
    private Button mUnrealizedStopButton;
    private Button mStopTimeButton;

    private TextView mBoardingPeopleTextView;
    private TextView mExitPeopleTextView;

    int nStartingPersonNumber = 0;
    int nEndingPersonNumber = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);

        mDecreaseNumberOfAddedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfAddedPeopleButton);
        mDecreaseNumberOfRemovedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfRemovedPeopleButton);
        mAddPeopleButton = (Button) findViewById(R.id.addPeopleButton);
        mRemovePeopleButton = (Button) findViewById(R.id.removePeopleButton);
        mChooseEventButton = (Button) findViewById(R.id.chooseEventButton);
        mUnrealizedStopButton = (Button) findViewById(R.id.unrealizedStopButton);
        mStopTimeButton = (Button) findViewById(R.id.stopTimeButton);

        mBoardingPeopleTextView = (TextView) findViewById(R.id.boardingPeopleTextView);
        mBoardingPeopleTextView.setText("0");
        mExitPeopleTextView = (TextView) findViewById(R.id.exitPeopleTextView);
        mExitPeopleTextView.setText("0");
    }


    //Select number of boarding people
    public void countBoardingPeople(View v) {
        String getString = String.valueOf(mBoardingPeopleTextView.getText());
        int current = Integer.parseInt(getString);
        if (v == mAddPeopleButton) {
            if (current < nEndingPersonNumber) {
                current++;
                mBoardingPeopleTextView.setText(String.valueOf(current));
            }
        }
        if (v == mDecreaseNumberOfAddedPeopleButton) {
            if (current > nStartingPersonNumber) {
                current--;
                mBoardingPeopleTextView.setText(String.valueOf(current));
            }
        }
    }

    //Select number of exit people
    public void countExitPeople(View v) {
        String getString = String.valueOf(mExitPeopleTextView.getText());
        int current = Integer.parseInt(getString);
        if (v == mRemovePeopleButton) {
            if (current < nEndingPersonNumber) {
                current++;
                mExitPeopleTextView.setText(String.valueOf(current));
            }
        }
        if (v == mDecreaseNumberOfRemovedPeopleButton) {
            if (current > nStartingPersonNumber) {
                current--;
                mExitPeopleTextView.setText(String.valueOf(current));
            }
        }
    }

}
