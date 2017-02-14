package com.mobithink.carbon.station;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobithink.carbon.R;

/**
 * Created by mplaton on 14/02/2017.
 */

public class StationEventDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
        View rootView = inflater.inflate(R.layout.station_event_dialog_fragment, container, false);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
