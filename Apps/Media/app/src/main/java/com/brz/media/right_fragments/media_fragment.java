package com.brz.media.right_fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.brz.media.R;

public class media_fragment extends Fragment {

    private ImageButton skip_back, playpause, skip_forward;

   public media_fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        skip_back = requireView().findViewById(R.id.media_skip_back);
        skip_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle skipping backwards
            }
        });

        playpause = requireView().findViewById(R.id.media_play);
        playpause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle play/pause
            }
        });

        skip_forward = requireView().findViewById(R.id.media_skip_forward);
        skip_forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //handle skipping forward
            }
        });

       super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media_fragment, container, false);
    }
}