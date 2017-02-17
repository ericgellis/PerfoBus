package com.mobithink.carbon.station;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.mobithink.carbon.driving.DrivingActivity;
import com.mobithink.carbon.managers.CarbonApplicationManager;
import com.mobithink.carbon.managers.DatabaseManager;

import static android.content.ContentValues.TAG;


/**
 * Created by mplaton on 02/02/2017.
 */

public class StationActivity extends Activity implements IEventSelectedListener{

    private Button mDecreaseNumberOfAddedPeopleButton;
    private Button mDecreaseNumberOfRemovedPeopleButton;
    private Button mAddPeopleButton;
    private Button mRemovePeopleButton;
    private Button mChooseEventButton;
    private Button mUnrealizedStopButton;
    private Button mStopTimeButton;

    private TextView mBoardingPeopleTextView;
    private TextView mExitPeopleTextView;

    private ListView mStationEventCustomListView;
    private StationEventCustomListViewAdapter mStationEventCustomListViewAdapter;
    private String [] eventTypeList;

    private Toolbar mStationToolBar;
    private Button mChangeStationNameButton;
    private Button mDeleteTimeCodeButton;
    private TextView mStationNameTextView;
    private Chronometer mTimeCodeChronometer;

    FragmentManager fm = getFragmentManager();

    TripDTO tripDTO;
    StationDTO stationDTO;
    StationDataDTO stationDataDTO;

    int nStartingPersonNumber = 0;
    int nEndingPersonNumber = 50;

    int numberOfPeopleIn = 0;
    int numberOfPeopleOut = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.station);

        mStationToolBar = (Toolbar) findViewById(R.id.stationToolBar);
        mChangeStationNameButton = (Button) findViewById(R.id.changeStationNameButton);
        mDeleteTimeCodeButton = (Button) findViewById(R.id.deleteTimeCodeButton);
        mStationNameTextView = (TextView) findViewById(R.id.stationNameTextView);
        mStationNameTextView.setText("Jean Jaures");
        mTimeCodeChronometer = (Chronometer) findViewById(R.id.timeCodeChronometer);
        mStationEventCustomListView = (ListView) findViewById(R.id.station_event_custom_listview);

        mDecreaseNumberOfAddedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfAddedPeopleButton);
        mDecreaseNumberOfAddedPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBoardingPeople(v);
            }
        });
        mDecreaseNumberOfRemovedPeopleButton = (Button) findViewById(R.id.decreaseNumberOfRemovedPeopleButton);
        mDecreaseNumberOfRemovedPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countExitPeople(v);
            }
        });
        mAddPeopleButton = (Button) findViewById(R.id.addPeopleButton);
        mAddPeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countBoardingPeople(v);
            }
        });
        mRemovePeopleButton = (Button) findViewById(R.id.removePeopleButton);
        mRemovePeopleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countExitPeople(v);
            }
        });
        mChooseEventButton = (Button) findViewById(R.id.chooseEventButton);
        mUnrealizedStopButton = (Button) findViewById(R.id.unrealizedStopButton);
        mStopTimeButton = (Button) findViewById(R.id.stopTimeButton);

        mBoardingPeopleTextView = (TextView) findViewById(R.id.boardingPeopleTextView);
        mBoardingPeopleTextView.setText("0");
        mExitPeopleTextView = (TextView) findViewById(R.id.exitPeopleTextView);
        mExitPeopleTextView.setText("0");

        mChangeStationNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStationName();
            }
        });

        mDeleteTimeCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToDrivingPage();
            }
        });

        mChooseEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTochooseStationEvent();
            }
        });

        mUnrealizedStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stationSkip();
            }
        });

        mStopTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerStationData();
            }
        });

        mStationEventCustomListViewAdapter = new StationEventCustomListViewAdapter(this);
        mStationEventCustomListView.setAdapter(mStationEventCustomListViewAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        mTimeCodeChronometer.start();

    }

    public void changeStationName(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alertDialog.setTitle("Ce n'est pas "+ mStationNameTextView.getText().toString()+"?");
        alertDialog.setMessage("Entrer le nouveau nom de la station");
        alertDialog.setView(edittext);

        alertDialog.setPositiveButton("Confirmer", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String item = edittext.getEditableText().toString();
                mStationNameTextView.setText(item);
            }
        });

        alertDialog.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alertDialog.show();

    }

    public void goToDrivingPage(){

        DatabaseManager.getInstance().deleteStationData(stationDTO.getId(),stationDataDTO );

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);
    }

    public void stationSkip(){

    }

    public void registerStationData(){

        stationDataDTO = new StationDataDTO();
        stationDataDTO.setStationName(mStationNameTextView.getText().toString());
        stationDataDTO.setNumberOfComeIn(numberOfPeopleIn);
        stationDataDTO.setNumberOfGoOut(numberOfPeopleOut);
        stationDataDTO.setStartTime(System.currentTimeMillis());
        stationDataDTO.setEndTime(System.currentTimeMillis());
        stationDataDTO.setGpsLat(null);
        stationDataDTO.setGpsLong(null);
        stationDataDTO.setStationStep(1);

        DatabaseManager.getInstance().updateStationData(CarbonApplicationManager.getInstance().getCurrentStationId(), CarbonApplicationManager.getInstance().getCurrentTripId(), stationDataDTO);

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);

        Log.i(TAG, "registerStationData: Station has been updated");
    }

    //Select number of boarding people
    public void countBoardingPeople(View v) {

        if (v == mAddPeopleButton) {
            if (numberOfPeopleIn < nEndingPersonNumber) {
                numberOfPeopleIn++;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleIn));
            }
        }
        if (v == mDecreaseNumberOfAddedPeopleButton) {
            if (numberOfPeopleIn > nStartingPersonNumber) {
                numberOfPeopleIn--;
                mBoardingPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleIn));
            }
        }
    }

    //Select number of exit people
    public void countExitPeople(View v) {
        if (v == mRemovePeopleButton) {
            if (numberOfPeopleOut < nEndingPersonNumber) {
                numberOfPeopleOut++;
                mExitPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleOut));
            }
        }
        if (v == mDecreaseNumberOfRemovedPeopleButton) {
            if (numberOfPeopleOut > nStartingPersonNumber) {
                numberOfPeopleOut--;
                mExitPeopleTextView.setText(java.lang.String.valueOf(numberOfPeopleOut));
            }
        }
    }

    public void goTochooseStationEvent(){
        StationEventDialogFragment dialogFragment = new  StationEventDialogFragment();
        dialogFragment.setListener(this);
        dialogFragment.show(fm, "Choisir un évènement");
    }

    @Override
    public void onEventSelected(String eventType) {
        mStationEventCustomListViewAdapter.addData(eventType);
        mStationEventCustomListViewAdapter.notifyDataSetChanged();
    }
}
