package com.brz.headunit;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.rarepebble.colorpicker.ColorPickerView;

public class LightFragment extends Fragment {

    Main main;

    private ColorPickerView colorPickerView;
    private ImageView colourPickerDisableView;
    private AppCompatButton colourSubmitButton;
    private AppCompatButton colourCancelButton;
    private ImageView colourAreaDisableView;

    private AppCompatButton solidColorSelectorButton;
    private AppCompatButton toggleSolidColorButton;
    private AppCompatButton patternButton1;
    private AppCompatButton patternButton2;
    private AppCompatButton patternButton3;
    private AppCompatButton patternButton4;
    private AppCompatButton primaryInteriorColorButton;
    private AppCompatButton primaryInteriorColorSelectorButton;
    private AppCompatButton secondaryInteriorColorButton;
    private AppCompatButton secondaryInteriorColorSelectorButton;

    private SwitchCompat underlightSwitch;
    private SwitchCompat interiorSwitch;

    private ImageView solidColourButtonHighlight;
    private ImageView patternButtonHighlight1;
    private ImageView patternButtonHighlight2;
    private ImageView patternButtonHighlight3;
    private ImageView patternButtonHighlight4;
    private ImageView primaryInteriorColourButtonHighlight;
    private ImageView secondaryInteriorColourButtonHighlight;

    public int selectedUnderlightPattern = 0;
    private int selectedLightSystem = 0;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public LightFragment(Main parent) {
        // Required empty public constructor
        main = parent;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        preferences = context.getSharedPreferences(getResources().getString(R.string.preference_file_name), 0);
        editor = preferences.edit();
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_light, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //initialize views
        //TODO: add views around car icon
        colorPickerView = requireView().requireViewById(R.id.color_picker);
        colourPickerDisableView = requireView().requireViewById(R.id.color_picker_disable_view);
        colourSubmitButton = requireView().requireViewById(R.id.colour_picker_submit_button);
        colourCancelButton = requireView().requireViewById(R.id.colour_picker_cancel_button);
        colourAreaDisableView = requireView().requireViewById(R.id.colour_area_disable_imageview);

        //initialize buttons
        solidColorSelectorButton = requireView().requireViewById(R.id.solid_colour_selector);
        toggleSolidColorButton = requireView().requireViewById(R.id.solid_underlight_button);
        patternButton1 = requireView().requireViewById(R.id.pattern_button_1);
        patternButton2 = requireView().requireViewById(R.id.pattern_button_2);
        patternButton3 = requireView().requireViewById(R.id.pattern_button_3);
        patternButton4 = requireView().requireViewById(R.id.pattern_button_4);
        primaryInteriorColorButton = requireView().requireViewById(R.id.primary_interior_colour_button);
        primaryInteriorColorSelectorButton = requireView().requireViewById(R.id.primary_interior_colour_selector);
        secondaryInteriorColorButton = requireView().requireViewById(R.id.secondary_interior_colour_button);
        secondaryInteriorColorSelectorButton = requireView().requireViewById(R.id.secondary_interior_colour_selector);

        //initialize highlights
        solidColourButtonHighlight = requireView().requireViewById(R.id.solid_underlight_highlight);
        patternButtonHighlight1 = requireView().requireViewById(R.id.pattern_button_1_highlight);
        patternButtonHighlight2 = requireView().requireViewById(R.id.pattern_button_2_highlight);
        patternButtonHighlight3 = requireView().requireViewById(R.id.pattern_button_3_highlight);
        patternButtonHighlight4 = requireView().requireViewById(R.id.pattern_button_4_highlight);
        primaryInteriorColourButtonHighlight = requireView().requireViewById(R.id.primary_interior_colour_button_highlight);
        secondaryInteriorColourButtonHighlight = requireView().requireViewById(R.id.secondary_interior_colour_button_highlight);

        //initialize switches
        underlightSwitch = requireView().requireViewById(R.id.underlight_light_status_switch);
        interiorSwitch = requireView().requireViewById(R.id.interior_light_status_switch);


        //set listeners for color selectors
        solidColorSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColor(0);
            }
        });

        primaryInteriorColorSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColor(1);
            }
        });

        secondaryInteriorColorSelectorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColor(2);
            }
        });

        //set listeners for pattern buttons
        toggleSolidColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUnderlightPattern(0);
            }
        });

        patternButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUnderlightPattern(1);
            }
        });

        patternButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUnderlightPattern(2);
            }
        });

        patternButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUnderlightPattern(3);
            }
        });

        patternButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUnderlightPattern(4);
            }
        });

        primaryInteriorColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInteriorColour(true);
            }
        });

        secondaryInteriorColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInteriorColour(false);
            }
        });

        //listener for colour submission
        colourSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateColor(-1);
            }
        });

        colourCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //forget about color selection
                colourPickerDisableView.setVisibility(View.VISIBLE);
                colourAreaDisableView.setVisibility(View.INVISIBLE);
            }
        });

        //update colourSelector buttons
        solidColorSelectorButton.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(preferences.getInt("LIG_System_Color_0", 16777215))));
        primaryInteriorColorSelectorButton.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(preferences.getInt("LIG_System_Color_1", 16777215))));
        secondaryInteriorColorSelectorButton.setBackgroundColor(Color.parseColor("#" + Integer.toHexString(preferences.getInt("LIG_System_Color_2", 16777215))));

        super.onViewCreated(view, savedInstanceState);
    }

    //TODO: dispatch commands to Arduino

    void updateColor(int systemID) {
        //if systemID is provided as -1, ignore this step since it means we are ending colour selection
        if (systemID >= 0) { //initiate colour selection
            selectedLightSystem = systemID;
            colourPickerDisableView.setVisibility(View.INVISIBLE);
            colourAreaDisableView.setVisibility(View.VISIBLE);
        } else { //terminate colour selection
            //get colour from colorpicker
            String colour = "#" + Integer.toHexString(colorPickerView.getColor());
            Integer colourInt = Color.parseColor(colour);

            //save colour
            String preferenceKey = "LIG_System_Color_" + String.valueOf(selectedLightSystem);
            editor.putInt(preferenceKey, colourInt);
            editor.apply();

            if (selectedLightSystem == 0) //update solid colour button
                solidColorSelectorButton.setBackgroundColor(colourInt);
            else if (selectedLightSystem == 1) //primary interior
                primaryInteriorColorSelectorButton.setBackgroundColor(colourInt);
            else
                secondaryInteriorColorSelectorButton.setBackgroundColor(colourInt);

            colourPickerDisableView.setVisibility(View.VISIBLE);
            colourAreaDisableView.setVisibility(View.INVISIBLE);
        }

    }

    void updateUnderlightPattern(int patternID) {
        //clear all highlights first
        solidColourButtonHighlight.setVisibility(View.INVISIBLE);
        patternButtonHighlight1.setVisibility(View.INVISIBLE);
        patternButtonHighlight2.setVisibility(View.INVISIBLE);
        patternButtonHighlight3.setVisibility(View.INVISIBLE);
        patternButtonHighlight4.setVisibility(View.INVISIBLE);

        if (patternID == 0) //solid
            solidColourButtonHighlight.setVisibility(View.VISIBLE);
        else if (patternID == 1)
            patternButtonHighlight1.setVisibility(View.VISIBLE);
        else if (patternID == 2)
            patternButtonHighlight2.setVisibility(View.VISIBLE);
        else if (patternID == 3)
            patternButtonHighlight3.setVisibility(View.VISIBLE);
        else
            patternButtonHighlight4.setVisibility(View.VISIBLE);

        selectedUnderlightPattern = patternID;
    }

    void updateInteriorColour(boolean isPrimary) {
        if (isPrimary) {
            primaryInteriorColourButtonHighlight.setVisibility(View.VISIBLE);
            secondaryInteriorColourButtonHighlight.setVisibility(View.INVISIBLE);
        } else {
            primaryInteriorColourButtonHighlight.setVisibility(View.INVISIBLE);
            secondaryInteriorColourButtonHighlight.setVisibility(View.VISIBLE);
        }
    }
}