package com.mobithink.carbon.preparation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.consultation.ConsultationActivity;
import com.mobithink.carbon.database.model.TripDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mplaton on 27/02/2017.
 */

public class TripResultListViewAdapter extends ArrayAdapter<TripDTO> {

    private ArrayList<TripDTO> mTripList;

    public TripResultListViewAdapter(Context context, List<TripDTO> tripDTO) {
        super(context, 0, tripDTO);
    }

    public void setData(ArrayList<TripDTO> tripList){
        mTripList = tripList;
    }
    public void updateData(ArrayList<TripDTO> data){
        mTripList = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemview_trip_result_listview,parent, false);
        }

        TripResultListViewAdapter.TripViewHolder viewHolder = (TripResultListViewAdapter.TripViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new TripResultListViewAdapter.TripViewHolder();
            viewHolder.tripDateTextView = (TextView) convertView.findViewById(R.id.recordedTripOnDateTextView);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.item_view_result_listView_linear_layout);

            convertView.setTag(viewHolder);
        }

        final TripDTO tripDTO;
        tripDTO = mTripList.get(position);
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/mm/yyyy HHhmm");
        viewHolder.tripDateTextView.setText(dateTimeFormat.format(tripDTO.getStartTime()));

        viewHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToRestitutionOfResults = new Intent (getContext(), ConsultationActivity.class);
                getContext().startActivity(goToRestitutionOfResults);
            }
        });

        return convertView;
    }

    private class TripViewHolder {
        public TextView tripDateTextView;
        public LinearLayout linearLayout;

    }

    public int getItemCount() {
        if(mTripList != null){
            return mTripList.size();
        }else{
            return 0;
        }
    }
}
