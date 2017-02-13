package com.mobithink.carbon.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.driving.DrivingActivity;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mplaton on 13/02/2017.
 */

public class EventActivity extends Activity {

    ImageView mEventMap;
    String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=230x200&path=";

    Button mCancelledTimecodeButton;
    Button mVoiceMemoButton;
    Button mEventPhotoButton;
    Button mConfirmPositionEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        mEventMap = (ImageView) findViewById(R.id.event_map_imageview);
        try {
            String marker_dest = "color:orange|label:7|San Francisco,USA";
            marker_dest = URLEncoder.encode(marker_dest, "UTF-8");

            STATIC_MAP_API_ENDPOINT = STATIC_MAP_API_ENDPOINT + "&markers=" + marker_dest;

            Log.d("STATICMAPS", STATIC_MAP_API_ENDPOINT);
            Picasso.with(getApplication()).load(STATIC_MAP_API_ENDPOINT).fit().centerCrop().into(mEventMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        mCancelledTimecodeButton = (Button) findViewById(R.id.cancelled_timecode_button);
        mVoiceMemoButton = (Button) findViewById(R.id.voice_memo_button);
        mEventPhotoButton = (Button) findViewById(R.id.event_photo_button);
        mConfirmPositionEventButton = (Button) findViewById(R.id.confirm_position_event_button);

        mCancelledTimecodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelTimeCode();
            }
        });

    }

    public void cancelTimeCode(){

        //TO DO cancel timecode in database

        Intent toDrivingPage = new Intent (this, DrivingActivity.class);
        this.startActivity(toDrivingPage);
    }
}
