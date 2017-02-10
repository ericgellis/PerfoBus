package com.mobithink.carbon;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.database.model.CityDTO;
import com.mobithink.carbon.managers.RetrofitManager;
import com.mobithink.carbon.preparation.ChoiceLineFromAnalyzeActivity;
import com.mobithink.carbon.preparation.ChoiceLineFromConsultActivity;
import com.mobithink.carbon.preparation.ParametersActivity;
import com.mobithink.carbon.webservices.LineService;
import com.mobithink.carbon.webservices.TechnicalService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    Button mAnalyzeButton;
    Button mConsultButton;
    ImageView mParametersSettings;
    View mServerStatusView;

    ImageView mMobiThinkLogo;
    private TextView mAppVersionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_splash_screen);

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
        mServerStatusView.setBackground(getDrawable(R.drawable.server_offline_circle_status));


        mAnalyzeButton = (Button) findViewById(R.id.analyzeButton);
        mConsultButton = (Button) findViewById(R.id.consultButton);

        mAnalyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToChooseLine();
            }
        });

        mConsultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToChooseLineFromConsult();
            }
        });

        mParametersSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePageToParameters();
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
                        mServerStatusView.setBackground(getDrawable(R.drawable.server_online_circle_status));
                        break;
                    default:
                        mServerStatusView.setBackground(getDrawable(R.drawable.server_offline_circle_status));
                        break;
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                mServerStatusView.setBackground(getDrawable(R.drawable.server_offline_circle_status));
            }
        });
    }

    public void changePageToChooseLine(){
        Intent chooseLineFromAnalyze = new Intent(this, ChoiceLineFromAnalyzeActivity.class);
        this.startActivity(chooseLineFromAnalyze);
    }

    public void changePageToChooseLineFromConsult(){
        Intent chooseLineFromConsult = new Intent(this, ChoiceLineFromConsultActivity.class);
        this.startActivity(chooseLineFromConsult);
    }

    public void changePageToParameters(){
        Intent chooseParameters = new Intent(this, ParametersActivity.class);
        this.startActivity(chooseParameters);
    }
}
