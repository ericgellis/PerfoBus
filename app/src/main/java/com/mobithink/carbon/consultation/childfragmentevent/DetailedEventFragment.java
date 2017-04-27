package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.utils.PerformanceExplanations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by mplaton on 26/04/2017.
 */

public class DetailedEventFragment extends GenericTabFragment {

    public static final int MY_REQUEST_CODE = 0;

    EventDTO mEventDTO;

    private TextView mEventNameTextView;
    private TextView mEventTypeTextView;
    private TextView mTimeTextView;
    private TextView mAudioRetranscriptionTextView;
    private TextView mEventExplanations;

    private ImageView mEventPhoto;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    public DetailedEventFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.child_fragment_detailed_event, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mEventNameTextView = (TextView) rootView.findViewById(R.id.eventNameTextView);
        mEventTypeTextView = (TextView) rootView.findViewById(R.id.eventTypeTextView);
        mTimeTextView = (TextView) rootView.findViewById(R.id.timeTextView);
        mAudioRetranscriptionTextView = (TextView) rootView.findViewById(R.id.audioRetranscriptionTextView);
        mEventExplanations = (TextView) rootView.findViewById(R.id.eventExplanations);

        mEventPhoto = (ImageView) rootView.findViewById(R.id.eventPhoto);

        Bundle extras = getArguments();
        mEventDTO = (EventDTO) extras.getSerializable("eventDTO");

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        mEventNameTextView.setText(mEventDTO.getEventName());
        mEventTypeTextView.setText(mEventDTO.getEventType());
        double eventTime = mEventDTO.getEndTime()-mEventDTO.getStartTime();
        mTimeTextView.setText(timeFormat.format(eventTime));
        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
        mEventExplanations.setText(performanceExplanations.performanceExplanations(mEventDTO));

        final String picturePath = mEventDTO.getPicture() + ".jpg" ;
        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkPhoto/"+picturePath)).exists();
        if(mEventDTO.getPicture() != null && existsImage){
            Bitmap bitmap = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);
            mEventPhoto.setImageBitmap(bitmap);

        }

    }
}
