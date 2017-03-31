package com.mobithink.carbon.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobithink.carbon.R;

/**
 * Created by mplaton on 31/03/2017.
 */

public class EventRecyclerViewAdapter extends RecyclerView.Adapter<EventRecyclerViewAdapter.ViewHolder>{

    private Context mContext;

    public class ViewHolder extends GenericViewHolder {
        public ImageView mEventImageRecyclerView;
        
        public ViewHolder (View view){
            super(view, true);

            mEventImageRecyclerView = (ImageView) view.findViewById(R.id.eventImageRecyclerView);

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
