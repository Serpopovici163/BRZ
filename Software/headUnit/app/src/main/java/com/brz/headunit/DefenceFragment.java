package com.brz.headunit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brz.headunit.defence.DefenceService;

public class DefenceFragment extends Fragment {

    public DefenceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_defense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //add transponder view here
        TextView aircraftDistanceTextView = requireView().requireViewById(R.id.aircraft_list_textview);

        //jammer image buttons
        ImageButton cellJamImageButton = requireView().requireViewById(R.id.cell_jammer_button);
        ImageButton radioJamImageButton = requireView().requireViewById(R.id.radio_jammer_button);
        ImageButton lidarJamImageButton = requireView().requireViewById(R.id.lidar_jammer_button);
        ImageButton radarJamImageButton = requireView().requireViewById(R.id.radar_jammer_button);

        super.onViewCreated(view, savedInstanceState);
    }

    public void displayJamState(int jammerID, boolean jamState) {
        //TODO left off here aug 15
    }
}