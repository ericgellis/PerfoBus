package com.mobithink.carbon.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobithink.carbon.R;
import com.mobithink.carbon.database.model.EventDTO;

import java.util.List;

/**
 * Created by mplaton on 31/03/2017.
 */

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private Context mContext;
    private List<EventDTO> eventDTOList;

    public class ViewHolder extends GenericViewHolder {
        public ImageView mEventImageView;

        public ViewHolder (View view){
            super(view, true);

            mEventImageView = (ImageView) view.findViewById(R.id.eventImageRecyclerView);

        }
    }

    public EventRecyclerViewAdapter(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event_recyclerview, parent, false);
        return new EventRecyclerViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(EventRecyclerViewAdapter.ViewHolder holder, int position) {
        final EventDTO eventDTO = eventDTOList.get(position);

        //Picasso.with(mContext).load(eventDTO.getPictureNameList()).resize(150, 150).centerCrop().into(holder.mEventImageView);

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
