package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
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

    Button mConsultButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_line_from_consult);

        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_City_Name_TextInputLayout);
        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_Line_Name_TextInputLayout);
        mWriteDirectionTextInputLayout  = (TextInputLayout) findViewById(R.id.Writing_Direction_TextInputLayout);

        mWriteCityNameTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_City_Name);
        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Line_Name);
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
