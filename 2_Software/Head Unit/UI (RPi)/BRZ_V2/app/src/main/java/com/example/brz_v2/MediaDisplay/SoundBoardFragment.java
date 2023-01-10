package com.example.brz_v2.MediaDisplay;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.brz_v2.MediaActivity;
import com.example.brz_v2.R;

public class SoundBoardFragment extends Fragment {

    MediaActivity parent;

    private TextView selectedSoundTextView;

    public SoundBoardFragment(MediaActivity mParent) {
        // Required empty public constructor
        parent = mParent;
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