package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.StationDTO;
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

    TextInputLayout mCityTextInputLayout;
    TextInputLayout mLineTextInputLayout;
    TextInputLayout mDirectionTextInputLayout;
    TextInputLayout mCapacityTextInputLayout;

    AutoCompleteTextView mCityAutocompleteView;
    TextInputEditText mLineEditText;
    TextInputEditText mDirectionEditText;
    TextInputEditText mCapacityEditText;

    ArrayAdapter<CityDTO> cityAdapter;
    ArrayAdapter<BusLineDTO> lineAdapter;
    ArrayAdapter<StationDTO> directionAdapter;
    CityDTO mSelectedCityDTO;
    BusLineDTO mSelectedLineDTO;

    Toolbar mAnalyzeLineToolBar;
    private StationDTO mSelectedDirection;


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

        cityAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        lineAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);
        directionAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);

        mCityTextInputLayout = (TextInputLayout) findViewById(R.id.city_textinputlayout);
        mLineTextInputLayout = (TextInputLayout) findViewById(R.id.line_textinputlayout);
        mDirectionTextInputLayout = (TextInputLayout) findViewById(R.id.direction_textinputlayout);
        mCapacityTextInputLayout = (TextInputLayout) findViewById(R.id.capacity_textinputlayout);

        mCityAutocompleteView = (AutoCompleteTextView) findViewById(R.id.Writing_City_Name);
        mCityAutocompleteView.setThreshold(1);//will start working from first character
        mCityAutocompleteView.setAdapter(cityAdapter);

        mCityAutocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long arg3) {
                //hide keyboard
                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(parent.getApplicationWindowToken(), 0);

                mSelectedCityDTO = (CityDTO) parent.getAdapter().getItem(position);
                getCityLines();

            }
        });

        mLineEditText = (TextInputEditText) findViewById(R.id.Line_edtitext);
        mDirectionEditText = (TextInputEditText) findViewById(R.id.Writing_Direction);
        mCapacityEditText = (TextInputEditText) findViewById(R.id.Writing_Vehicle_Capacity);

        mCreateNewLineButton = (Button) findViewById(R.id.createNewLine);
        mStartButton = (Button) findViewById(R.id.start_button);

        mCreateNewLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToCreateNewLine();
            }
        });

        mLineEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedCityDTO != null) {
                    new AlertDialog.Builder(ChoiceLineFromAnalyzeActivity.this)
                            .setCancelable(true)
                            .setAdapter(lineAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedLineDTO = lineAdapter.getItem(which);
                                    mLineEditText.setText(mSelectedLineDTO.getName());
                                    getLineStations();
                                }
                            })
                            .create()
                            .show();
                }else{
                    mCityTextInputLayout.setErrorEnabled(true);
                    mCityTextInputLayout.setError("Vous devez sélectionner une ville");
                }
            }
        });

        mDirectionEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mSelectedLineDTO != null) {

                    new AlertDialog.Builder(ChoiceLineFromAnalyzeActivity.this)
                            .setCancelable(true)
                            .setAdapter(directionAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mSelectedDirection = directionAdapter.getItem(which);
                                    mDirectionEditText.setText(mSelectedDirection.getStationName());
                                }
                            })
                            .create()
                            .show();
                }else{
                    mLineTextInputLayout.setErrorEnabled(true);
                    mLineTextInputLayout.setError("Vous devez sélectionner une Ligne");
                }
            }
        });

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDriving();
            }
        });
    }

    private void getLineStations() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<StationDTO>> call = groupService.getLineStations(mSelectedLineDTO.getId());

        call.enqueue(new Callback<List<StationDTO>>() {
            @Override
            public void onResponse(Call<List<StationDTO>> call, Response<List<StationDTO>> response) {
                switch (response.code()) {
                    case 200:

                        directionAdapter.add(response.body().get(0));
                        directionAdapter.add(response.body().get(response.body().size()-1));

                        directionAdapter.notifyDataSetChanged();

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<StationDTO>> call, Throwable t) {
                //TODO
            }
        });
    }

    private void getCityLines() {

        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<BusLineDTO>> call = groupService.getCityLines(mSelectedCityDTO.getName());

        call.enqueue(new Callback<List<BusLineDTO>>() {
            @Override
            public void onResponse(Call<List<BusLineDTO>> call, Response<List<BusLineDTO>> response) {
                switch (response.code()) {
                    case 200:

                        lineAdapter.addAll(response.body());
                        lineAdapter.notifyDataSetChanged();

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusLineDTO>> call, Throwable t) {
                //TODO
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

        Call<List<CityDTO>> call = groupService.getCities();

        call.enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                switch (response.code()) {
                    case 200:

                        cityAdapter.addAll(response.body());
                        cityAdapter.notifyDataSetChanged();

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                //TODO
            }
        });
    }

    public void changePageToCreateNewLine() {

        Intent createLine = new Intent(this, CreateLineActivity.class);
        this.startActivity(createLine);

    }

    public void startDriving() {
        boolean hasError = false;

        if ((mCityAutocompleteView.getText().toString().equals(""))) {
            hasError = true;
            mCityTextInputLayout.setErrorEnabled(true);
            mCityTextInputLayout.setError("Vous devez sélectionner une ville");
        }

        if ((mLineEditText.getText().toString().equals(""))) {
            hasError = true;
            mLineTextInputLayout.setErrorEnabled(true);
            mLineTextInputLayout.setError("Vous devez sélectionner une ligne");
        }

        if ((mDirectionEditText.getText().toString().equals(""))) {
            hasError = true;
            mDirectionTextInputLayout.setErrorEnabled(true);
            mDirectionTextInputLayout.setError("Vous devez sélectionner une direction");
        }

        if ((mCapacityEditText.getText().toString().equals(""))) {
            hasError = true;
            mCapacityTextInputLayout.setErrorEnabled(true);
            mCapacityTextInputLayout.setError("Vous devez inscrire une capacité pour le véhicule");
        }

        if (!hasError) {
            Intent startDriving = new Intent(this, DrivingActivity.class);
            this.startActivity(startDriving);
        }
    }
}
