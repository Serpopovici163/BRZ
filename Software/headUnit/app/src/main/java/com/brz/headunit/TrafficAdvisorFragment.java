package com.brz.headunit;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class TrafficAdvisorFragment extends Fragment {

    //0 fhc, 1 shc, 2 fpc, 3 spc
    //hazard are on left such that the initial patternID, 0, is legal if accidentally triggered
    private int selectedLightPattern = 0;
    private boolean sirenState = false;
    private boolean hornState = false;

    ImageView fastPoliceHighlight;
    ImageView slowPoliceHighlight;
    ImageView fastHazardHighlight;
    ImageView slowHazardHighlight;

    ImageView legalIcon;
    ImageView hornIcon;
    ImageView sirenIcon;

    private void updateIcons() {
        //set to green initially which will be overwritten by yellow if necessary
        sirenIcon.setColorFilter(Color.GREEN);
        if (!hornState)
            hornIcon.clearColorFilter(); //reset horn icon

        if (selectedLightPattern >= 2) {
            legalIcon.setColorFilter(Color.RED);
            sirenIcon.setColorFilter(Color.YELLOW);
        }
        else if (selectedLightPattern == 1)
            legalIcon.setColorFilter(Color.GREEN);
        else
            legalIcon.setColorFilter(Color.YELLOW);
    }

    public int getSelectedLightPattern() {return selectedLightPattern;}

    public void setSelectedLightPattern(int requestedPattern) {
        //save request
        selectedLightPattern = requestedPattern;

        //set all to invisible
        fastPoliceHighlight.setVisibility(View.INVISIBLE);
        slowPoliceHighlight.setVisibility(View.INVISIBLE);
        fastHazardHighlight.setVisibility(View.INVISIBLE);
        slowHazardHighlight.setVisibility(View.INVISIBLE);

        //highlight button
        if (requestedPattern == 0)
            fastHazardHighlight.setVisibility(View.VISIBLE);
        else if (requestedPattern == 1)
            slowHazardHighlight.setVisibility(View.VISIBLE);
        else if (requestedPattern == 2)
            fastPoliceHighlight.setVisibility(View.VISIBLE);
        else
            slowPoliceHighlight.setVisibility(View.VISIBLE);

        updateIcons();
    }

    void updateLightState(boolean state) {
        if (state)
            sirenIcon.setColorFilter(Color.RED);
        else //reset colour otherwise
            updateIcons();
    }

    void updateSirenState(boolean state) {
        if (state) {
            hornIcon.setColorFilter(Color.RED);
            sirenState = true;
        } else {
            updateIcons();
            sirenState = false;
        }
    }

    void updateHornState(boolean state) {
        if (sirenState)
            return; //do nothing because siren state is more critical than horn state

        if (hornState && state)
            return; //do nothing because horn is already running and this is desired

        Log.e("horntest","running");

        if (state) {
            hornState = true;
            hornIcon.setColorFilter(Color.YELLOW);
        } else {
            hornState = false;
            updateIcons();
        }
    }

    public TrafficAdvisorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_traffic_advisor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //general initializing
        fastPoliceHighlight = requireView().requireViewById(R.id.fast_police_cycle_highlight_imageview);
        slowPoliceHighlight = requireView().requireViewById(R.id.slow_police_cycle_highlight_imageview);
        fastHazardHighlight = requireView().requireViewById(R.id.fast_hazard_cycle_highlight_imageview);
        slowHazardHighlight = requireView().requireViewById(R.id.slow_hazard_cycle_highlight_imageview);

        Button fastPoliceButton = requireView().requireViewById(R.id.fast_police_cycle_button);
        Button slowPoliceButton = requireView().requireViewById(R.id.slow_police_cycle_button);
        Button fastHazardButton = requireView().requireViewById(R.id.fast_hazard_cycle_button);
        Button slowHazardButton = requireView().requireViewById(R.id.slow_hazard_cycle_button);

        legalIcon = requireView().requireViewById(R.id.legal_status_imageview);
        hornIcon = requireView().requireViewById(R.id.horn_status_imageview);
        sirenIcon = requireView().requireViewById(R.id.siren_status_imageview);

        //update any views that need updating
        updateIcons();
        setSelectedLightPattern(0);

        //button onclicklisteners
        fastPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedLightPattern(2);
            }
        });

        slowPoliceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedLightPattern(3);
            }
        });

        fastHazardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedLightPattern(0);
            }
        });

        slowHazardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelectedLightPattern(1);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}