package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.mobithink.carbon.R;

import java.util.ArrayList;

/**
 * Created by mplaton on 31/01/2017.
 */

public class CreateLineActivity extends Activity {

    TextInputLayout mWriteLineTextInputLayout;
    TextInputLayout mWriteCityNameTextInputLayout;
    TextInputLayout mWriteDepartureStationTextInputLayout;

    LinearLayout mStationEditTextContainer;

    TextInputEditText mWriteLineTextInputEditText;
    TextInputEditText mWriteCityNameTextInputEditText;
    TextInputEditText mWriteDepartureStationTextInputEditText;

    ArrayList<TextInputLayout> mListStationTextInputEditText = new ArrayList<>();

    Button mCreateLineButton;

    Toolbar mNewLineToolBar;

    CheckBox interUrbanCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_line);

        mStationEditTextContainer = (LinearLayout) findViewById(R.id.stationEditTextContainer);

        mNewLineToolBar = (Toolbar) findViewById(R.id.newLineToolBar);
        mNewLineToolBar.setTitle("Nouvelle ligne");
        mNewLineToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.Line_Name_TextInputLayout);
        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.City_Name_TextInputLayout);

        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Line_Name);
        mWriteCityNameTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_City_Name);

        addTextInputLayout();

        interUrbanCheckBox = (CheckBox) findViewById(R.id.interurbanCheckBox);

        mCreateLineButton = (Button) findViewById(R.id.createLine);

        mCreateLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLine();
            }
        });
    }

    private void addTextInputLayout() {
        final TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(this).inflate(R.layout.text_input_layout, null);

        EditText stationEditText = (EditText) textInputLayout.findViewById(R.id.station_field);
        stationEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            if(mListStationTextInputEditText.get(mListStationTextInputEditText.size()-1).equals(textInputLayout)){
                                addTextInputLayout();
                            }

                            return true;
                        }
                        return false;
                    }
                });

        mStationEditTextContainer.addView(textInputLayout);
        mListStationTextInputEditText.add(textInputLayout);

        if(!mListStationTextInputEditText.get(0).equals(textInputLayout)){
            stationEditText.requestFocus();
        }
    }

    public void createLine(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setTitle("Confirmer les stations");
        alertDialogBuilder.setMessage("");
        alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
