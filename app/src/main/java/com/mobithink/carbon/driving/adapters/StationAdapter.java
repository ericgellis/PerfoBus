package com.mobithink.carbon.driving.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.StationDTO;

import java.util.List;

/**
 * Created by jpaput on 09/02/2017.
 */

public class StationAdapter extends  RecyclerView.Adapter<StationAdapter.ViewHolder>{

    private List<StationDTO> mData;
    private int step = 1;

    public void setData(List<StationDTO> listStation){
        mData = listStation;
    }

    public void setStep(int step){
        this.step = step;
    }

    @Override
    public StationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itemview_station, parent, false);
        return new StationAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StationAdapter.ViewHolder holder, int position) {
        holder.stationNameTextView.setText(mData.get(position + step).getStationName());
    }

    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }else{
            return mData.size() - step;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView stationNameTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            stationNameTextView = (TextView) itemView.findViewById(R.id.itemview_stationname);
        }
    }
}
