package com.example.brz_v2.MediaDisplay;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.brz_v2.R;

import java.util.Map;

public class SettingsFragment extends Fragment {

    private LinearLayout subSystemLinearLayout;
    private LinearLayout settingsLinearLayout;
    private TextView placeholderTextView;

    private String preferenceFile = "HEAD_UNIT_SETTINGS";

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        subSystemLinearLayout = requireView().requireViewById(R.id.subsystem_list_linearlayout);
        settingsLinearLayout = requireView().requireViewById(R.id.settings_list_linearlayout);
        placeholderTextView = requireView().requireViewById(R.id.placeholder_textview);

        //generate subsystem buttons
        String[] subsystems = {"Navigation", "Safety", "Vehicle", "Defence", "Lighting", "Audio"};
        for (int i = 0; i < subsystems.length; i++) {
            Button buttonBuffer = new Button(requireContext());

            //set style
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 75); //change height of buttons here
            params.setMargins(5, 2, 0, 2);
            buttonBuffer.setLayoutParams(params);
            buttonBuffer.setGravity(Gravity.CENTER);
            buttonBuffer.setTextSize(24);
            buttonBuffer.setText(subsystems[i]);

            //set onclick
            int finalI = i;
            buttonBuffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeSettingsView(finalI);
                }
            });

            subSystemLinearLayout.addView(buttonBuffer);
        }
    }

    private void changeSettingsView(int subsystemID) {
        //make sure placeholder is hidden
        placeholderTextView.setVisibility(View.INVISIBLE);

        //now get all settings
        //settings are stored using the following key format: XXX_TITLE
        //here title is a string containing setting name whereas XXX are the three first letters of the relevant subsystem
        SharedPreferences preferences = requireContext().getSharedPreferences(preferenceFile, 0);

        Map<String,?> keys = preferences.getAll();

        for(Map.Entry<String,?> entry : keys.entrySet())
        {
            String settingName = entry.getKey();
            LinearLayout setting = new LinearLayout(requireContext());
            LinearLayout.LayoutParams parentParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 75); //change height of settings here
            parentParams.setMargins(5, 2, 0, 2);
            setting.setLayoutParams(parentParams);
            setting.setWeightSum(5);
            setting.setOrientation(LinearLayout.HORIZONTAL);

            //add setting textview
            TextView settingTitle = new TextView(requireContext());
            LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
            titleParams.weight = 3;
            settingTitle.setLayoutParams(titleParams);
            settingTitle.setText(settingName);
            settingTitle.setTextSize(24); //TODO: adjust text size
            setting.addView(settingTitle);

            //add interactive view based on data type
            if (entry.getValue().getClass().equals(String.class)) {
                EditText settingField = new EditText(requireContext());
                LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                fieldParams.weight = 2;
                settingField.setLayoutParams(fieldParams);
                settingField.setText(entry.getValue().getClass().toString());
                settingField.setTextSize(24); //TODO: adjust text size
                settingField.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                setting.addView(settingField);
            } else if (entry.getValue().getClass().equals(Integer.class)) {
                //is this necessary? ints can be stored in strings
            } else { //must be boolean
                SwitchCompat settingField = new SwitchCompat(requireContext());
                LinearLayout.LayoutParams fieldParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                fieldParams.weight = 2;
                settingField.setLayoutParams(fieldParams);
                settingField.setChecked((boolean) entry.getValue());
            }

            settingsLinearLayout.addView(setting);
        }
    }

    public void setLegalMode() {
        //hide settings to illegal subsystems
    }
}