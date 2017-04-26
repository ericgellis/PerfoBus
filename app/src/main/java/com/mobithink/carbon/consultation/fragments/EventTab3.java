package com.mobithink.carbon.consultation.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.managers.PreferenceManager;
import com.mobithink.carbon.utils.Mathematics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;


public class EventTab3 extends GenericTabFragment implements OnChartValueSelectedListener {

    private RelativeLayout mainRelativeLayout;

    private TextView mTotalTrip;
    private ListView mGeneralListView;
    String[] generalities = new String[]{"Evènement section courante", "Evènement carrefours", "Evènement stations" };

    private TextView mTotalTripTimeTextView;
    private TextView mAverageSpeedTextView;
    private TextView mLossOfTotalTimeTextView;
    private TextView mSavingOfPossibleTimeTextView;
    private TextView mSavingInEuroTextView;

    private PieChart mDecompositionPieChart;

    public static final int MY_REQUEST_CODE = 0;

    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);
    Mathematics mathematics = new Mathematics();

    private long interStationObjective = 600;
    private long timeInStation;
    private long totalTimeInStation = 0;
    private long averageTimeInStation;

    private double tripBetweenStationsDistance = 0;
    private long tripDistance = 0;

    public EventTab3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");

        View rootView = inflater.inflate(R.layout.fragment_event_tab3, container, false);

        if (ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_REQUEST_CODE);
        }

        mainRelativeLayout = (RelativeLayout) rootView.findViewById(R.id.mainRelativeLayout);

        mTotalTrip = (TextView) rootView.findViewById(R.id.totalTrip);
        mTotalTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTotalTripInformations();
            }
        });

        mGeneralListView = (ListView) rootView.findViewById(R.id.generalListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, generalities);
        mGeneralListView.setAdapter(adapter);
        mGeneralListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventGenerality();
            }
        });

        mTotalTripTimeTextView = (TextView) rootView.findViewById(R.id.totalTripTimeTextView);
        mAverageSpeedTextView = (TextView) rootView.findViewById(R.id.averageSpeedTextView);
        mLossOfTotalTimeTextView = (TextView) rootView.findViewById(R.id.lossOfTotalTimeTextView);
        mSavingOfPossibleTimeTextView = (TextView) rootView.findViewById(R.id.savingOfPossibleTimeTextView);
        mSavingInEuroTextView = (TextView) rootView.findViewById(R.id.savingInEuroTextView);

        mDecompositionPieChart = (PieChart) rootView.findViewById(R.id.decompositionPieChart);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        getTripDTO();

        showTotalTripInformations();

////        if (getTripDTO().getEventDTOList() != null) {
////            long eventTotalDuration = 0;
////            long eventInStationTotalDuration = 0;
////
////            for(EventDTO eventDTO : getTripDTO().getEventDTOList()){
////                eventNameMainList.add(eventDTO);
////
////                if (eventDTO.getStationName() == null) {
////                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
////                    String timeString = timeFormat.format(eventDuration);
////                    eventInDrivingList.add(eventDTO.getEventName() + " - " + timeString) ;
////                    eventTotalDuration+=eventDuration;
////
////                } else {
////                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
////                    eventInStationTotalDuration+=eventDuration;
////                }
////                eventTotalDurationTextView.setText(" - " +timeFormat.format(eventTotalDuration) + " - ");
////                eventInStationTotalDurationTextView.setText(" - " +timeFormat.format(eventInStationTotalDuration) + " - ");
////            }
////        }
//
//        eventMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                showDetailedInformations(position);
//            }
//        });
//
//        adapter.notifyDataSetChanged();

    }

    public void showTotalTripInformations(){
        mainRelativeLayout.setVisibility(View.VISIBLE);
        //totalTrip.setBackgroundResource(R.color.lightBlue);
        //totalTrip.setTextColor(ContextCompat.getColor(getContext(),R.color.white));

        long tripTime = getTripDTO().getEndTime() - getTripDTO().getStartTime();
        mTotalTripTimeTextView.setText(timeFormat.format(tripTime));

        ArrayList<Double> distanceTab = new ArrayList<>() ;
        if (getTripDTO().getStationDataDTOList() != null) {
            for (int i = 0; i + 1 < getTripDTO().getStationDataDTOList().size(); i++) {
                timeInStation = getTripDTO().getStationDataDTOList().get(i).getEndTime() - getTripDTO().getStationDataDTOList().get(i).getStartTime();
                totalTimeInStation += timeInStation;
                tripBetweenStationsDistance = Math.round(Mathematics.calculateGPSDistance(getTripDTO().getStationDataDTOList().get(i).getGpsLat(), getTripDTO().getStationDataDTOList().get(i).getGpsLong(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLat(), getTripDTO().getStationDataDTOList().get(i + 1).getGpsLong()));
                distanceTab.add(tripBetweenStationsDistance);
                tripDistance += tripBetweenStationsDistance;
            }
        }
        double averageTripSpeed = (tripDistance*0.001)/(tripTime*0.00000027778);
        mAverageSpeedTextView.setText(String.valueOf(averageTripSpeed)+ " km/h");

        averageTimeInStation = totalTimeInStation/getTripDTO().getStationDataDTOList().size();
        long timeSavingResult = totalTimeInStation-(((tripDistance/interStationObjective)+ 1)*averageTimeInStation);

        int timeSavingInMinutes = (int) (((timeSavingResult / 1000)/60) % 60);

        mSavingOfPossibleTimeTextView.setText(timeSavingInMinutes+ " min");
        mSavingInEuroTextView.setText(Math.round(timeSavingInMinutes* PreferenceManager.getInstance().getCostOfProductionByMinute())  + " euro");


        ArrayList<Double> eventInStationTimeTab = new ArrayList<>() ;
        ArrayList<Double> eventInCrossroadTimeTab = new ArrayList<>() ;
        ArrayList<Double> eventInDrivingTimeTab = new ArrayList<>();
        long eventInStationTotalTime = 0;
        long eventInCrossroadTotalTime = 0;
        long eventInDrivingTotalTime = 0;
        if (getTripDTO().getEventDTOList() != null){
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()){
                if (Objects.equals(eventDTO.getEventType(), "Evènement en station")){
                    double eventInStationTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInStationTimeTab.add(eventInStationTime);
                    eventInStationTotalTime += eventInStationTime;
                } else if (eventDTO.getEventType().equals("Evènement en carrefour")){
                    double eventInCrossroadTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInCrossroadTimeTab.add(eventInCrossroadTime);
                    eventInCrossroadTotalTime += eventInCrossroadTime;
                } else if (eventDTO.getEventType().equals("Evènement en section courante")){
                    double eventInDrivingTime = eventDTO.getEndTime()-eventDTO.getStartTime();
                    eventInDrivingTimeTab.add(eventInDrivingTime);
                    eventInDrivingTotalTime += eventInDrivingTime;
                } else {
                    Log.e(TAG, "Impossible case");
                }
            }
        }

        long onlyTripTime = tripTime-totalTimeInStation-eventInDrivingTotalTime-eventInCrossroadTotalTime-eventInStationTotalTime;

        mDecompositionPieChart.setUsePercentValues(true);
        ArrayList<PieEntry> yvalues = new ArrayList<>();
        yvalues.add(new PieEntry(onlyTripTime, 0));
        yvalues.add(new PieEntry(totalTimeInStation, 1));
        yvalues.add(new PieEntry(eventInDrivingTotalTime, 2));
        yvalues.add(new PieEntry(eventInCrossroadTotalTime, 3));
        yvalues.add(new PieEntry(eventInStationTotalTime, 4));

        PieDataSet dataSet = new PieDataSet(yvalues, "Décomposition");

        ArrayList<String> xVals = new ArrayList<>();
        xVals.add("Trajet");
        xVals.add("Station");
        xVals.add("Evènement section courante");
        xVals.add("Evènement carrefours");
        xVals.add("Evènement station");

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());

        mDecompositionPieChart.setHoleRadius(58f);
        dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

        data.setValueTextSize(13f);
        data.setValueTextColor(Color.DKGRAY);

        Legend l = mDecompositionPieChart.getLegend();
        l.setEnabled(false);

        mDecompositionPieChart.setData(data);
        mDecompositionPieChart.setDrawHoleEnabled(true);
        mDecompositionPieChart.setTransparentCircleRadius(58f);
        mDecompositionPieChart.setEntryLabelColor(Color.WHITE);
        mDecompositionPieChart.setEntryLabelTextSize(12f);
        mDecompositionPieChart.setRotationEnabled(false);
        mDecompositionPieChart.setHighlightPerTapEnabled(false);
        mDecompositionPieChart.getDescription().setEnabled(false);

        mDecompositionPieChart.setOnChartValueSelectedListener(this);

        ArrayList<Double> eventTime = new ArrayList<>();
        Double eventTotalTime= 0.0;
        if (getTripDTO().getEventDTOList()!= null) {
            for (EventDTO eventDTO : getTripDTO().getEventDTOList()) {
                double eventTimeCalculation = eventDTO.getEndTime()-eventDTO.getStartTime();
                eventTime.add(eventTimeCalculation);
                eventTotalTime += eventTimeCalculation;
            }
        }
        mLossOfTotalTimeTextView.setText(timeFormat.format(eventTotalTime));

    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    public void eventGenerality(){


    }

    public void showDetailedInformations(int position){
        mainRelativeLayout.setVisibility(View.GONE);


//        if (eventNameMainList.get(position).getStationName() != null){
//            eventName.setText(eventNameMainList.get(position).getEventName()+ " - " + eventNameMainList.get(position).getStationName());
//        } else {
//            eventName.setText(eventNameMainList.get(position).getEventName()+ " - En roulage");
//        }
//        eventTimeSaving.setText("Gain de temps : " + eventNameMainList.get(position).getTimeSaving()+ " min");
//        PerformanceExplanations performanceExplanations = new PerformanceExplanations();
//        eventExplanations.setText(performanceExplanations.performanceExplanations(eventNameMainList.get(position)));
//
//
//        final String picturePath = eventNameMainList.get(position).getPicture() + ".jpg" ;
//        boolean existsImage = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+ "/mobithinkPhoto/"+picturePath)).exists();
//        if(eventNameMainList.get(position).getPicture() != null && existsImage){
//            Bitmap bitmap = BitmapFactory.decodeFile(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkPhoto/" +picturePath);
//            eventImageView.setImageBitmap(bitmap);
//
//        }
//
//
//        final String audioPath = eventNameMainList.get(position).getVoiceMemo()+".3gp";
//        boolean existsAudio = (new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkAudio/"+audioPath)).exists();
//        if (eventNameMainList.get(position).getVoiceMemo() != null && existsAudio){
//            eventAudioView.setVisibility(View.VISIBLE);
//            eventAudioView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    MediaPlayer mediaPlayer = new MediaPlayer();
//                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//                    try {
//                        mediaPlayer.setDataSource(android.os.Environment.getExternalStorageDirectory().getAbsolutePath()+"/mobithinkAudio/"+audioPath);
//                        mediaPlayer.prepare();
//                        mediaPlayer.start();
//                    } catch (IOException ioe){
//                        ioe.printStackTrace();
//                    }
//                }
//            });
//
//        }
    }

}
