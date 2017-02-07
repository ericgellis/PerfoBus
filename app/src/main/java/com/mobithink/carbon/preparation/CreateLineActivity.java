package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

import com.mobithink.carbon.R;

/**
 * Created by mplaton on 31/01/2017.
 */

public class CreateLineActivity extends Activity {

    TextInputLayout mWriteLineTextInputLayout;
    TextInputLayout mWriteCityNameTextInputLayout;
    TextInputLayout mWriteDepartureStationTextInputLayout;
    TextInputLayout mAddFirstStationTextInputLayout;
    TextInputLayout mAddSecondStationTextInputLayout;

    TextInputEditText mWriteLineTextInputEditText;
    TextInputEditText mWriteCityNameTextInputEditText;
    TextInputEditText mWriteDepartureStationTextInputEditText;
    TextInputEditText mAddFirstStationTextInputEditText;
    TextInputEditText mAddSecondStationTextInputEditText;

    Button mCreateLineButton;

    Toolbar mNewLineToolBar;

    CheckBox interUrbanCheckBox;

    RelativeLayout mActivityCreateLineRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_line);

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
        mWriteDepartureStationTextInputLayout = (TextInputLayout) findViewById(R.id.Departure_Station_TextInputLayout);
        mAddFirstStationTextInputLayout = (TextInputLayout) findViewById(R.id.Adding_Station_TextInputLayout);
        mAddSecondStationTextInputLayout = (TextInputLayout) findViewById(R.id.Adding_Second_Station_TextInputLayout);

        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Line_Name);
        mWriteCityNameTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_City_Name);
        mWriteDepartureStationTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Departure_Station);
        mAddFirstStationTextInputEditText = (TextInputEditText) findViewById(R.id.Adding_Station);
        mAddSecondStationTextInputEditText = (TextInputEditText) findViewById(R.id.Adding_second_Station);

        mActivityCreateLineRelativeLayout = (RelativeLayout) findViewById(R.id.activity_create_line_relative_layout);

        interUrbanCheckBox = (CheckBox) findViewById(R.id.interurbanCheckBox);

        mCreateLineButton = (Button) findViewById(R.id.createLine);

        mCreateLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createLine();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(mAddSecondStationTextInputLayout != null) {
            mActivityCreateLineRelativeLayout.addView(createNewEditText());
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

    private TextInputLayout createNewEditText(){

        TextInputLayout textInputLayout = new TextInputLayout(this);
        textInputLayout.setErrorTextAppearance(R.style.MyErrorText);
        textInputLayout.isErrorEnabled();
        TextInputEditText textInputEditText = new TextInputEditText(this);
        textInputEditText.setHint(R.string.addStation);
        textInputEditText.setTextSize(R.dimen.input_text_size);
        textInputEditText.setTextColor(getResources().getColor(R.color.primary_text));

        textInputLayout.addView(textInputEditText);

        return textInputLayout;
    }
}
