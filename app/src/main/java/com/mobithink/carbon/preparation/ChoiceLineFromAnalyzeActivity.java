package com.mobithink.carbon.preparation;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.BusLineDTO;
import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.database.model.StationDTO;
import com.mobithink.carbon.database.model.TripDTO;
import com.mobithink.carbon.driving.DrivingActivity;
import com.mobithink.carbon.managers.DatabaseManager;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.webservices.LineService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mplaton on 31/01/2017.
 */

public class ChoiceLineFromAnalyzeActivity extends Activity {

    public static final int CREATE_LINE = 1;
    public static final int ANALYSE_LINE = 2;

    private static final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 101;

    private View mRootView;
    private Button mCreateNewLineButton;
    private Button mStartButton;

    private TextInputLayout mCityTextInputLayout;
    private TextInputLayout mLineTextInputLayout;
    private TextInputLayout mDirectionTextInputLayout;
    private TextInputLayout mCapacityTextInputLayout;

    private AutoCompleteTextView mCityAutocompleteView;
    private TextInputEditText mLineEditText;
    private TextInputEditText mDirectionEditText;
    private TextInputEditText mCapacityEditText;

    private ArrayAdapter<CityDTO> cityAdapter;
    private ArrayAdapter<BusLineDTO> lineAdapter;
    private ArrayAdapter<StationDTO> directionAdapter;
    private CityDTO mSelectedCityDTO;
    private BusLineDTO mSelectedLineDTO;

    private Toolbar mAnalyzeLineToolBar;
    private StationDTO mSelectedDirection;
    private ArrayList<StationDTO> listStation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_choice_line_from_analyze);

        mRootView = findViewById(R.id.choicelineactivity_rootview);

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
        listStation = new ArrayList<>();

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
                createNewLine();
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

                                    if(mSelectedDirection.equals(directionAdapter.getItem(0))){
                                        Collections.reverse(listStation);
                                    }
                                }
                            })
                            .create()
                            .show();
                }else{
                    mLineTextInputLayout.setErrorEnabled(true);
                    mLineTextInputLayout.setError("Vous devez sélectionner une ligne");
                }
            }
        });

        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            String mCity = (String) bundle.getSerializable("city");
            String mBusLine = (String) bundle.getSerializable("line");
            mCityAutocompleteView.setText(mCity);
            mLineEditText.setText(mBusLine);
        }

        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
            }
        });

    }


    private void checkPermission() {

        List<String> permissionNeeded = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
            permissionNeeded.add(android.Manifest.permission.VIBRATE);
        }

        if (permissionNeeded.size() == 0) {
            startDriving();
        } else {
            askPermissions(permissionNeeded.toArray(new String[permissionNeeded.size()]));
        }
    }

    private void askPermissions(String[] permissionNeeded) {
        ActivityCompat.requestPermissions(this,
                permissionNeeded,
                ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
    }


    private void getLineStations() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<StationDTO>> call = groupService.getLineStations(mSelectedLineDTO.getId());

        call.enqueue(new Callback<List<StationDTO>>() {
            @Override
            public void onResponse(Call<List<StationDTO>> call, Response<List<StationDTO>> response) {
                switch (response.code()) {
                    case 200:
                        listStation.clear();
                        listStation.addAll(response.body());

                        directionAdapter.clear();
                        if(listStation.size()>= 2) {
                            directionAdapter.add(listStation.get(0));
                            directionAdapter.add(listStation.get(listStation.size() - 1));
                            directionAdapter.notifyDataSetChanged();
                        }
                        break;
                    default:
                        Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur, Erreur : " + response.code(), Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<StationDTO>> call, Throwable t) {
                Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur", Snackbar.LENGTH_LONG).show();
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
                        lineAdapter.clear();
                        lineAdapter.addAll(response.body());
                        lineAdapter.notifyDataSetChanged();

                        break;
                    default:
                        Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur, Erreur : " + response.code(), Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<BusLineDTO>> call, Throwable t) {
                Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCities();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull final String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDriving();

                } else {
                    Snackbar.make(mRootView, "Vous devez accepter cette authorisation pour continuer", Snackbar.LENGTH_LONG)
                            .setAction("Recommencer", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    checkPermission();
                                }
                            }).show();
                }
                break;
            }
        }
    }

    private void getCities() {
        LineService groupService = RetrofitManager.build().create(LineService.class);

        Call<List<CityDTO>> call = groupService.getCities();

        call.enqueue(new Callback<List<CityDTO>>() {
            @Override
            public void onResponse(Call<List<CityDTO>> call, Response<List<CityDTO>> response) {
                switch (response.code()) {
                    case 200:
                        cityAdapter.clear();
                        cityAdapter.addAll(response.body());
                        cityAdapter.notifyDataSetChanged();

                        break;
                    default:
                        Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur, Erreur : " + response.code(), Snackbar.LENGTH_LONG).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<List<CityDTO>> call, Throwable t) {
                Snackbar.make(mRootView, "Erreur lors de la communication avec le serveur", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    public void createNewLine() {

        if (mSelectedCityDTO == null){
            Intent createLine = new Intent(this, CreateLineActivity.class);
            this.startActivity(createLine);
        } else {
            Bundle createNewlineBundle = new Bundle();
            createNewlineBundle.putSerializable("chosenCity", mSelectedCityDTO);
            Intent createNewLineIntent = new Intent(this, CreateLineActivity.class);
            createNewLineIntent.putExtras(createNewlineBundle);
            this.startActivityForResult(createNewLineIntent, CREATE_LINE);

        }

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

            TripDTO tripDTO = new TripDTO();
            tripDTO.setStartTime(System.currentTimeMillis());
            tripDTO.setVehicleCapacity(Integer.parseInt(mCapacityEditText.getText().toString()));
            tripDTO.setBusLineId(mSelectedLineDTO.getId());

            DatabaseManager.getInstance().startNewTrip(mSelectedLineDTO.getId(), tripDTO);

            Intent startDriving = new Intent(this, DrivingActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("listStation",listStation);
            bundle.putSerializable("city",mSelectedCityDTO);
            bundle.putSerializable("direction",mSelectedDirection);
            bundle.putSerializable("line",mSelectedLineDTO);
            startDriving.putExtras(bundle);
            this.startActivityForResult(startDriving, ANALYSE_LINE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CREATE_LINE:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mRootView, "Ligne créée avec succès", Snackbar.LENGTH_LONG).show();
                    getCityLines();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(mRootView, "La création d'une nouvelle ligne a été annulée", Snackbar.LENGTH_LONG).show();
                }
                break;

            case ANALYSE_LINE:
                if (resultCode == Activity.RESULT_OK) {
                    setResult(Activity.RESULT_OK);
                    finish();

                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(mRootView, "La saisie du trajet a été annulée", Snackbar.LENGTH_LONG).show();
                }
                break;
        }

    }
}
