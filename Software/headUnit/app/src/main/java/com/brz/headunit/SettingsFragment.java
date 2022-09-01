package com.brz.headunit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class SettingsFragment extends Fragment {

    private ScrollView subSystemScrollView;
    private ScrollView settingsScrollView;
    private TextView placeholderTextView;

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

        subSystemScrollView = requireView().requireViewById(R.id.subsystem_list_scrollview);
        settingsScrollView = requireView().requireViewById(R.id.settings_list_scrollview);
        placeholderTextView = requireView().requireViewById(R.id.placeholder_textview);
    }

    public void initialize() {
        //populate subsystem scrollview
    }

    private void changeSettingsView(int subsystemID) {
        //make sure placeholder is hidden
        placeholderTextView.setVisibility(View.INVISIBLE);
    }

    public void setLegalMode() {
        //hide settings to illegal subsystems
    }
}