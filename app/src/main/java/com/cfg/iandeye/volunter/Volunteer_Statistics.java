package com.cfg.iandeye.volunter;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cfg.iandeye.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Volunteer_Statistics extends Fragment {
    View holder;


    public Volunteer_Statistics() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        holder=inflater.inflate(R.layout.fragment_volunteer__statistics, container, false);



        return holder;
    }

}
