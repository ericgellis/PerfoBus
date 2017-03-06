package com.mobithink.carbon.consultation.fragments;


import android.support.v4.app.Fragment;

import com.mobithink.carbon.consultation.ConsultationActivity;
import com.mobithink.carbon.database.model.TripDTO;

/**
 * Created by mplaton on 01/03/2017.
 */

public abstract class GenericTabFragment extends Fragment {

    protected TripDTO getTripDTO() {
        return ((ConsultationActivity) getActivity()).getmTripDTO();
    }

}
