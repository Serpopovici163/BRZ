package com.brz.headunit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brz.headunit.R;

public class SoundBoardFragment extends Fragment {

    private TextView selectedSoundTextView;

    public SoundBoardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sound_board, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedSoundTextView = requireView().requireViewById(R.id.selected_sound_textview);

    }

    //get list of available sounds from arduino horn manager and generate as many buttons as necessary
    void generateSoundButton() {

    }
}