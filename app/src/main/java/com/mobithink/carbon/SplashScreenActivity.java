package com.mobithink.carbon;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.preparation.ChoiceLineFromAnalyzeActivity;
import com.mobithink.carbon.preparation.ChoiceLineFromConsultActivity;
import com.mobithink.carbon.preparation.ParametersActivity;
import com.mobithink.carbon.webservices.TechnicalService;

import io.fabric.sdk.android.Fabric;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static final int CHANGE_PARAMETER_ACTION = 3;
    private static final int ANALYSE_LINE_ACTION = 2;
    private static final int CONSULT_LINE_ACTION = 1;


    View mRootView;

    Button mAnalyzeButton;
    Button mConsultButton;

    ImageView mParametersSettings;
    View mServerStatusView;

    ImageView mMobiThinkLogo;
    private TextView mAppVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fabric.with(this, new Crashlytics());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_splash_screen);

        mRootView = findViewById(R.id.splashactivity_rootview);

        mAppVersionTextView = (TextView) findViewById(R.id.appversion_textview);

        try {
            String versionName  = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            mAppVersionTextView.setText("Version : " + versionName  );
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mParametersSettings = (ImageView) findViewById(R.id.parameterButton);
        mMobiThinkLogo = (ImageView) findViewById(R.id.mobithinkLogo);
        mServerStatusView = findViewById(R.id.server_status_view);
        mServerStatusView.setBackground(getDrawable(R.drawable.server_pending_circle_status));


        mAnalyzeButton = (Button) findViewById(R.id.analyzeButton);
        mConsultButton = (Button) findViewById(R.id.consultButton);

        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchAnalyse();
            }
        });

        mConsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchConsult();
            }
        });

        mParametersSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchParametersActivity();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkServerStatus();
    }

    private void checkServerStatus() {
        TechnicalService technicalService = RetrofitManager.build().create(TechnicalService.class);

        Call<Void> call = technicalService.checkStatus();

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        Log.d(this.getClass().getName(), "The Serveur status is up");
                        mServerStatusView.setBackground(getDrawable(R.drawable.server_online_circle_status));
                        break;
                    default:
                        mServerStatusView.setBackground(getDrawable(R.drawable.server_offline_circle_status));
                        checkServerStatus();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mServerStatusView.setBackground(getDrawable(R.drawable.server_offline_circle_status));
                checkServerStatus();
            }
        });
    }

    public void launchAnalyse() {
        Intent chooseLineFromAnalyze = new Intent(this, ChoiceLineFromAnalyzeActivity.class);
        this.startActivityForResult(chooseLineFromAnalyze, ANALYSE_LINE_ACTION);
    }

    public void launchConsult() {
        Intent chooseLineFromConsult = new Intent(this, ChoiceLineFromConsultActivity.class);
        this.startActivityForResult(chooseLineFromConsult, CONSULT_LINE_ACTION);
    }

    public void launchParametersActivity() {
        Intent chooseParameters = new Intent(this, ParametersActivity.class);
        this.startActivityForResult(chooseParameters, CHANGE_PARAMETER_ACTION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CHANGE_PARAMETER_ACTION:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mRootView, "Les paramêtres ont été modifiés avec succès", Snackbar.LENGTH_LONG).show();
                }
                break;
            case ANALYSE_LINE_ACTION:
                if (resultCode == Activity.RESULT_OK) {
                    Snackbar.make(mRootView, "La saisie du trajet a été sauvegardée", Snackbar.LENGTH_LONG).show();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    Snackbar.make(mRootView, "La saisie du trajet a été annulée", Snackbar.LENGTH_LONG).show();
                }
                break;
        }


    }

    @Override
    public void onBackPressed() {
     super.onBackPressed();
        Intent i=new Intent(this,SplashScreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
    }
}
