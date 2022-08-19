package com.brz.headunit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PopUpDefenceFragment extends Fragment {

    TextView counterMeasureTextView1;
    TextView counterMeasureTextView2;
    TextView counterMeasureTextView3;
    TextView counterMeasureTextView4;

    public PopUpDefenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pop_up_defence, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //initialize views
        counterMeasureTextView1 = requireView().requireViewById(R.id.radar_state_textview);
        counterMeasureTextView2 = requireView().requireViewById(R.id.transponder_state_textview);
        counterMeasureTextView3 = requireView().requireViewById(R.id.lidar_state_textview);
        counterMeasureTextView4 = requireView().requireViewById(R.id.cell_sniffer_state_textview);

        //set initial state to offline
        toggleCounterMeasureViewState(counterMeasureTextView1, -1);
        toggleCounterMeasureViewState(counterMeasureTextView2, -1);
        toggleCounterMeasureViewState(counterMeasureTextView3, -1);
        toggleCounterMeasureViewState(counterMeasureTextView4, -1);

        super.onViewCreated(view, savedInstanceState);
    }

    void toggleCounterMeasureViewState(TextView view, int state) {
        //here state is -2 for error, -1 for offline, 0 for clear, 1 for warn, and 2 for alert

        if (state == -2)
            view.setText(R.string.error_caps);
        else if (state == -1)
            view.setText(R.string.offline_caps);
        else if (state == 0)
            view.setText(R.string.clear_caps);
        else if (state == 1)
            view.setText(R.string.warn_caps);
        else
            view.setText(R.string.alert_caps);
    }
}