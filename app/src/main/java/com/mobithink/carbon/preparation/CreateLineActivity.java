package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.webservices.LineService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 31/01/2017.
 */

public class CreateLineActivity extends Activity {

    private static final String TAG = "CreateLineActivity";

    AutoCompleteTextView mCityAutocompleteView;
    ArrayAdapter<CityDTO> cityAdapter;
    CityDTO mSelectedCityDTO;
    CityDTO mChosenCity;
    private TextInputLayout mWriteLineTextInputLayout;
    private TextInputLayout mWriteCityNameTextInputLayout;
    private LinearLayout mStationEditTextContainer;
    private TextInputEditText mWriteLineTextInputEditText;
    private ArrayList<EditText> mListStationEditText = new ArrayList<>();
    private Button mCreateLineButton;

    private Toolbar mNewLineToolBar;

    private CheckBox interUrbanCheckBox;
    private ArrayList<StationDTO> mStationDTOList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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


        cityAdapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item);

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

            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mChosenCity = (CityDTO) extras.getSerializable("chosenCity");
            if(mChosenCity!= null){
                mCityAutocompleteView.setText(mChosenCity.getName());
                mSelectedCityDTO = mChosenCity;
            }
        }

        addTextInputLayout();

        interUrbanCheckBox = (CheckBox) findViewById(R.id.interurbanCheckBox);
        interUrbanCheckBox.setChecked(false);
        interUrbanCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                } else {

                }
            }
        });

        mCreateLineButton = (Button) findViewById(R.id.createLine);

        mCreateLineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controlEditText();
            }
        });

    }

    private void addTextInputLayout() {
        final TextInputLayout textInputLayout = (TextInputLayout) LayoutInflater.from(this).inflate(R.layout.text_input_layout, null);

        final EditText stationEditText = (EditText) textInputLayout.findViewById(R.id.station_field);
        stationEditText.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {

                            if(mListStationEditText.get(mListStationEditText.size()-1).equals(stationEditText)){
                                addTextInputLayout();
                            }

                            return true;
                        }
                        return false;
                    }
                });

        mStationEditTextContainer.addView(textInputLayout);
        mListStationEditText.add(stationEditText);

        if(!mListStationEditText.get(0).equals(stationEditText)){
            stationEditText.requestFocus();
        }
    }

    public void createLine(){

        final BusLineDTO busLineDTO = new BusLineDTO();
        final CityDTO cityDTO;

        if(mSelectedCityDTO != null){
            cityDTO = mSelectedCityDTO;
        }else{
            cityDTO = new CityDTO();
            cityDTO.setName(mCityAutocompleteView.getText().toString());
        }

        Calendar c=Calendar.getInstance();
        c.setTime(new Date());

        busLineDTO.setCityDto(cityDTO);
        busLineDTO.setName(mWriteLineTextInputEditText.getText().toString());
        busLineDTO.setStationDTOList(mStationDTOList);
        busLineDTO.setDateOfCreation(c.getTimeInMillis());

        registerLine(busLineDTO);

    }

    public void registerLine(final BusLineDTO busLineDTO){
        LineService lineService = RetrofitManager.build().create(LineService.class);

        Call<Void> call = lineService.register(busLineDTO);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 201:
                        Log.i(TAG,"lineService.register success with " + busLineDTO.toString());
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("city", busLineDTO.getCityDto().getName());
                        bundle.putSerializable("line", busLineDTO.getName());
                        Intent intent = new Intent(getApplication(), ChoiceLineFromAnalyzeActivity.class);
                        intent.putExtras(bundle);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;

                    default:
                        Log.e(TAG, "lineService.register fail with code " + response.code() +" and body " + response.message());
                        break;
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "lineService.register error", t);
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
        final Context context = this.getApplicationContext();

        call.enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(TAG, "groupService.getCities success : " + response.body().size());
                        cityAdapter.addAll(response.body());
                        cityAdapter.notifyDataSetChanged();
                        break;
                    default:
                        String message = "groupService.getCities fail with code " + response.code() +" and message " + response.message();
                        Log.e(TAG, message);
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                Log.e(TAG, "groupService.getCities error", t);
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void controlEditText() {
        boolean hasError = false;

        if ((mCityAutocompleteView.getText().toString().equals(""))) {
            hasError = true;
            mWriteCityNameTextInputLayout.setErrorEnabled(true);
            mWriteCityNameTextInputLayout.setError("Vous devez sélectionner une ville");
        }

        if ((mWriteLineTextInputEditText.getText().toString().equals(""))) {
            hasError = true;
            mWriteLineTextInputLayout.setErrorEnabled(true);
            mWriteLineTextInputLayout.setError("Vous devez sélectionner une ligne");
        }

        if (!hasError) {

            mStationDTOList = new ArrayList<>();
            int step = 0;

            for(EditText et : mListStationEditText){
                if(! et.getText().toString().isEmpty()){
                    mStationDTOList.add(
                            new StationDTO(et.getText().toString(), step )
                    );
                    step ++;
                }
            }

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(CreateLineActivity.this);
            alertDialogBuilder.setCancelable(true);
            alertDialogBuilder.setTitle("Confirmer les stations");
            alertDialogBuilder.setMessage(mStationDTOList.toString());
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    createLine();

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
}
