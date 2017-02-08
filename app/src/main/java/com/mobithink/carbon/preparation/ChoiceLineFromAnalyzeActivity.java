package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.City;
import com.mobithink.carbon.driving.DrivingActivity;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.webservices.LineService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ChoiceLineFromAnalyzeActivity extends Activity {

    Button mCreateNewLineButton;
    Button mStartButton;

    TextInputLayout mWriteCityNameTextInputLayout;
    TextInputLayout mWriteLineTextInputLayout;
    TextInputLayout mWriteDirectionTextInputLayout;
    TextInputLayout mWriteVehicleCapacityTextInputLayout;

    AutoCompleteTextView mWriteCityNameTextInputEditText;
    TextInputEditText mWriteLineTextInputEditText;
    TextInputEditText mWriteDirectionTextInputEditText;
    TextInputEditText mWriteVehicleCapacityTextInputEditText;

    ArrayAdapter<String> cityNameAdapter;

    Toolbar mAnalyzeLineToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_line_from_analyze);

        mAnalyzeLineToolBar = (Toolbar) findViewById(R.id.analyzeLineToolBar);
        mAnalyzeLineToolBar.setTitle("Choix d'une ligne");
        mAnalyzeLineToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cityNameAdapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_item);

        mWriteCityNameTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_City_Name_TextInputLayout);
        mWriteLineTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_Line_Name_TextInputLayout);
        mWriteDirectionTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_Direction_TextInputLayout);
        mWriteVehicleCapacityTextInputLayout = (TextInputLayout) findViewById(R.id.Writing_Vehicle_Capacity_TextInputLayout);

        mWriteCityNameTextInputEditText = (AutoCompleteTextView) findViewById(R.id.Writing_City_Name);
        mWriteCityNameTextInputEditText.setThreshold(1);//will start working from first character
        mWriteCityNameTextInputEditText.setAdapter(cityNameAdapter);
        mWriteLineTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Line_Name);
        mWriteDirectionTextInputEditText = (TextInputEditText) findViewById(R.id.Writing_Direction);
        mWriteVehicleCapacityTextInputEditText =(TextInputEditText) findViewById(R.id.Writing_Vehicle_Capacity);

        mCreateNewLineButton = (Button) findViewById(R.id.createNewLine);
        mStartButton = (Button) findViewById(R.id.start_button);

        mCreateNewLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToCreateNewLine();
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDriving();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCities();
    }

    private void getCities() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<City>> call = groupService.getCities();

        call.enqueue(new Callback<List<City>>() {
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                switch (response.code()) {
                    case 200:

                        for(City city : response.body()){
                            cityNameAdapter.add(city.getName());
                        }
                        cityNameAdapter.notifyDataSetChanged();

                        break;
                    default :
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }

    public void changePageToCreateNewLine(){
        Intent createLine = new Intent(this, CreateLineActivity.class);
        this.startActivity(createLine);
    }

    public void startDriving(){
        Intent startDriving = new Intent(this, DrivingActivity.class);
        this.startActivity(startDriving);
    }
}
