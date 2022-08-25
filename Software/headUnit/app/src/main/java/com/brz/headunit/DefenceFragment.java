package com.brz.headunit;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.brz.headunit.R;

public class DefenceFragment extends Fragment {

    static ImageButton cellJamImageButton; //ID 0
    static ImageButton radioJamImageButton; //ID 1
    static ImageButton lidarJamImageButton; //ID 2
    static ImageButton radarJamImageButton; //ID 3

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
        return inflater.inflate(R.layout.fragment_defence, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //add transponder view here
        TextView aircraftDistanceTextView = requireView().requireViewById(R.id.aircraft_list_textview);

        //jammer image buttons
        cellJamImageButton = requireView().requireViewById(R.id.cell_jammer_button);
        radioJamImageButton = requireView().requireViewById(R.id.radio_jammer_button);
        lidarJamImageButton = requireView().requireViewById(R.id.lidar_jammer_button);
        radarJamImageButton = requireView().requireViewById(R.id.radar_jammer_button);

        super.onViewCreated(view, savedInstanceState);
    }

    public static void displayJamState(int jammerID, boolean jamState) {
        if (jammerID == 0) //cell
            if (jamState)
                cellJamImageButton.setColorFilter(Color.RED);
            else
                cellJamImageButton.setColorFilter(Color.BLACK);
        else if (jammerID == 1) //radio
            if (jamState)
                radioJamImageButton.setColorFilter(Color.RED);
            else
                radioJamImageButton.setColorFilter(Color.BLACK);
        else if (jammerID == 2)
            if (jamState)
                lidarJamImageButton.setColorFilter(Color.RED);
            else
                lidarJamImageButton.setColorFilter(Color.BLACK);
        else
            if (jamState)
                radarJamImageButton.setColorFilter(Color.RED);
            else
                radarJamImageButton.setColorFilter(Color.BLACK);
    }
}