package com.brz.headunit;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;

public class TrafficAdvisorFragment extends Fragment {

    Main main;
    public TrafficAdvisorFragment(Main parent) {this.main = parent;}

    //0 fhc, 1 shc, 2 fpc, 3 spc
    //hazard are on left such that the initial patternID, 0, is legal if accidentally triggered
    private int selectedLightPattern = 0;
    private boolean sirenState = false;
    private boolean lightState = false;
    private boolean hornState = false;

    ImageView fastPoliceHighlight;
    ImageView slowPoliceHighlight;
    ImageView fastHazardHighlight;
    ImageView slowHazardHighlight;

    ImageView legalIcon;
    ImageView hornIcon;
    ImageView sirenIcon;

    private void updateIcons() {
        //siren icon
        if (selectedLightPattern >= 2) {
            legalIcon.setColorFilter(Color.RED);
            sirenIcon.setColorFilter(Color.YELLOW);
        }
        else if (selectedLightPattern == 1) {
            legalIcon.setColorFilter(Color.GREEN);
            sirenIcon.setColorFilter(Color.GREEN);
        } else {
            legalIcon.setColorFilter(Color.YELLOW);
            sirenIcon.setColorFilter(Color.YELLOW);
        }

        //override siren icon if siren active
        if (lightState)
            if (selectedLightPattern <= 1)
                sirenIcon.setColorFilter(Color.YELLOW);
            else
                sirenIcon.setColorFilter(Color.RED);

        //horn icon
        if (sirenState)
            hornIcon.setColorFilter(Color.RED);
        else if (hornState)
            hornIcon.setColorFilter(Color.YELLOW);
        else
            hornIcon.setColorFilter(Color.BLACK);
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
        lightState = state;
        updateIcons();
    }

    void updateSirenState(boolean state) {
        sirenState = state;
        updateIcons();
    }

    public void updateHornState(boolean state) {
        hornState = state;
        updateIcons();
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

        SwitchCompat frontHazardSwitch = requireView().requireViewById(R.id.front_hazard_switch);
        SwitchCompat sideHazardSwitch = requireView().requireViewById(R.id.side_hazard_switch);
        SwitchCompat rearHazardSwitch = requireView().requireViewById(R.id.rear_hazard_switch);
        SwitchCompat frontPoliceSwitch = requireView().requireViewById(R.id.front_police_switch);
        SwitchCompat sidePoliceSwitch = requireView().requireViewById(R.id.side_police_switch);
        SwitchCompat rearPoliceSwitch = requireView().requireViewById(R.id.rear_police_switch);

        ImageView frontHazardHighlight = requireView().requireViewById(R.id.front_hazard_highlight);
        ImageView sideHazardHighlight = requireView().requireViewById(R.id.side_hazard_highlight);
        ImageView rearHazardHighlight = requireView().requireViewById(R.id.rear_hazard_highlight);
        ImageView frontPoliceHighlight = requireView().requireViewById(R.id.front_police_highlight);
        ImageView sidePoliceHighlight = requireView().requireViewById(R.id.side_police_highlight);
        ImageView rearPoliceHighlight = requireView().requireViewById(R.id.rear_police_highlight);

        legalIcon = requireView().requireViewById(R.id.legal_status_imageview);
        hornIcon = requireView().requireViewById(R.id.horn_status_imageview);
        sirenIcon = requireView().requireViewById(R.id.siren_status_imageview);

        //update any views that need updating
        updateIcons();
        setSelectedLightPattern(0);

        //set default switch position
        frontHazardSwitch.setChecked(true);
        sideHazardSwitch.setChecked(true);
        rearHazardSwitch.setChecked(true);
        frontPoliceSwitch.setChecked(true);
        sidePoliceHighlight.setVisibility(View.INVISIBLE);
        rearPoliceHighlight.setVisibility(View.INVISIBLE);

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

        frontHazardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(0, b);
                if (b)
                    frontHazardHighlight.setVisibility(View.VISIBLE);
                else
                    frontHazardHighlight.setVisibility(View.INVISIBLE);
            }
        });

        sideHazardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(1, b);
                if (b)
                    sideHazardHighlight.setVisibility(View.VISIBLE);
                else
                    sideHazardHighlight.setVisibility(View.INVISIBLE);
            }
        });

        rearHazardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(2, b);
                if (b)
                    rearHazardHighlight.setVisibility(View.VISIBLE);
                else
                    rearHazardHighlight.setVisibility(View.INVISIBLE);
            }
        });

        frontPoliceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(3, b);
                if (b)
                    frontPoliceHighlight.setVisibility(View.VISIBLE);
                else
                    frontPoliceHighlight.setVisibility(View.INVISIBLE);
            }
        });

        sidePoliceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(4, b);
                if (b)
                    sidePoliceHighlight.setVisibility(View.VISIBLE);
                else
                    sidePoliceHighlight.setVisibility(View.INVISIBLE);
            }
        });

        rearPoliceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                main.forwardLightSettings(5, b);
                if (b)
                    rearPoliceHighlight.setVisibility(View.VISIBLE);
                else
                    rearPoliceHighlight.setVisibility(View.INVISIBLE);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}