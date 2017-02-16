package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.ConsultationActivity;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ChoiceLineFromConsultActivity extends Activity {

    TextInputLayout mWriteCityNameTextInputLayout;
    TextInputLayout mWriteLineTextInputLayout;
    TextInputLayout mWriteDirectionTextInputLayout;

    TextInputEditText mWriteCityNameTextInputEditText;
    TextInputEditText mWriteLineTextInputEditText;
    TextInputEditText mWriteDirectionTextInputEditText;

    Toolbar mConsultLineToolBar;

    Button mConsultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_line_from_consult);

        mConsultLineToolBar = (Toolbar) findViewById(R.id.consultLineToolBar);
        mConsultLineToolBar.setTitle("Choix d'une ligne");
        mConsultLineToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.city_textinputlayout);
        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.line_textinputlayout);
        mWriteDirectionTextInputLayout  = (TextInputLayout) findViewById(R.id.direction_textinputlayout);

        mWriteCityNameTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_City_Name);
        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Line_edtitext);
        mWriteDirectionTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Direction);

        mConsultButton = (Button) findViewById(R.id.consultButton);
        mConsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRestitution();
            }
        });

    }

    public void goToRestitution(){
        Intent goToRestitutionOfResults = new Intent (this, ConsultationActivity.class);
        this.startActivity(goToRestitutionOfResults);
    }
}
