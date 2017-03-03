package com.mobithink.carbon.consultation.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDataDTO;

import java.util.List;

/**
 * Created by mplaton on 03/03/2017.
 */

public class StationListViewAdapter extends ArrayAdapter<String> {

    public StationListViewAdapter(Context context, List<String> stations) {
        super(context, 0, stations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_station_listview,parent, false);
        }

        StationViewHolder viewHolder = (StationViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new StationViewHolder();
            viewHolder.stationName = (TextView) convertView.findViewById(R.id.stationNameTextView);

            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        String station = getItem(position);


        return convertView;
    }

    private class StationViewHolder{
        public TextView stationName;
        public LinearLayout itemviewStationListviewLinearLayout;
    }

}
