package com.mobithink.carbon.consultation.childfragmentevent;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.utils.PerformanceExplanations;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okio.ByteString;

import static android.app.Activity.RESULT_OK;


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
    private ImageView mEventMap;


    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);

    private String STATIC_MAP_API_ENDPOINT = "http://maps.googleapis.com/maps/api/staticmap?size=230x200&path=";

    private String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

    private final int REQ_CODE_SPEECH_INPUT = 100;


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
        mEventMap = (ImageView) rootView.findViewById(R.id.eventMap);

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

        LatLng eventCoor = new LatLng(mEventDTO.getGpsLat(), mEventDTO.getGpsLong());
        try {
            String marker_dest = "color:orange|label:1|eventCoord";
            marker_dest = URLEncoder.encode(marker_dest, "UTF-8");

            STATIC_MAP_API_ENDPOINT = STATIC_MAP_API_ENDPOINT + "&markers=" + marker_dest;

            Log.d("STATICMAPS", STATIC_MAP_API_ENDPOINT);
            Picasso.with(getContext()).load(STATIC_MAP_API_ENDPOINT).resize(200, 200).centerCrop().into(mEventMap);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //speechRetranscription();

    }

//    public void speechRetranscription() {
//
//        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
//        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
//        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speech_prompt));
//        try {
//            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
//        } catch (ActivityNotFoundException a) {
//            Toast.makeText(getContext(), getString(R.string.speech_not_supported), Toast.LENGTH_SHORT).show();
//        }
//
//        disp();
//
//    }
//
//    public void disp()
//    {
//        //code for playing the audio file which you wish to give as an input
//
//        String audioFileName = mEventDTO.getVoiceMemo() + ".3gp" ;
//        String fileName = "android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkAudio/"+audioFileName";
//        MediaPlayer mp = new MediaPlayer();
//        try {
//            mp.setDataSource(fileName);
//            mp.prepare();
//            mp.start();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        switch (requestCode) {
//            case REQ_CODE_SPEECH_INPUT: {
//                if (resultCode == RESULT_OK && null != data) {
//
//                    ArrayList<String> result = data
//                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
//                    mAudioRetranscriptionTextView.setText(result.get(0));
//                }
//                break;
//            }
//
//        }
//    }

//    public void speechtranscreiption(){
//        SpeechClient speechClient = SpeechClient.create();
//
//        //  The path of the audio file to exploit
//
//        String audioFileName = mEventDTO.getVoiceMemo() + ".3gp";
//        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkAudio/"+audioFileName)).exists();
//        byte[] dataRead = MediaStore.Files.readAllBytes(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkAudio/"+audioFileName);
//        ByteString speecBytes = ByteString.copyFrom(dataRead);
//
//        // Requesting for the sync regnonitizer
//        RecognitionConfig recognitionConfig = RecognitionConfig.newBuilder()
//                .setEncoding(AudioEncoding.LINEAR16)
//                .setLanguageCode("en-US")
//                .setSampleRateHertz(16000)
//                .build();
//
//        //Recognition Audio builder
//        RecognitionAudio recognitionAudio = RecognitionAudio.newBuilder()
//                .setContent(speecBytes)
//                .build();
//
//        // Translates the speech from the input file
//        RecognizeResponse recognizeResponse = speechClient.recognize(recognitionConfig, recognitionAudio);
//        List<SpeechRecognitionResult> outputLists = recognizeResponse.getResultsList();
//
//        for (SpeechRecognitionResult result: outputLists) {
//            List<SpeechRecognitionAlternative> alternativesLists = result.getAlternativesList();
//            for (SpeechRecognitionAlternative alternativesList: alternativesLists) {
//                System.out.printf("Transcription: %s%n", alternativesList.getTranscript());
//            }
//        }
//        //Ending the Speech Client
//        speechClient.close();
//    }

}
