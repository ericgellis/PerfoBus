package com.mobithink.carbon.consultation.adapter;

import com.mobithink.carbon.consultation.fragments.GenericTabFragment;
import com.mobithink.carbon.database.model.EventDTO;
import com.mobithink.carbon.database.model.StationDataDTO;
import com.mobithink.carbon.database.model.TripDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by mplaton on 06/03/2017.
 */

public class ExpandableEventStationListHelper  {



    public static HashMap<String, List<String>> getData(TripDTO tripDTO) {

        LinkedHashMap<String, List<String>> expandableListDetail = new LinkedHashMap<>();
        List<String> listDataHeader  = new ArrayList<String>();
        List<String> stationNameList = new ArrayList<String>();


        for(EventDTO eventDTO : tripDTO.getEventDTOList()){
            for (int i = 0; i < tripDTO.getEventDTOList().size(); i++) {
                if (eventDTO != null && eventDTO.getStationName() != null) {

                    listDataHeader.add(eventDTO.getEventName());

                    long eventDuration = eventDTO.getEndTime()- eventDTO.getStartTime();
                    SimpleDateFormat timeFormat = new SimpleDateFormat("mm:ss", Locale.FRANCE);
                    String timeString = timeFormat.format(eventDuration);
                    stationNameList.add(eventDTO.getStationName() + " - " + timeString);
                    expandableListDetail.put(listDataHeader.get(i),stationNameList);
                }

            }
        }

        return expandableListDetail;
    }
}
