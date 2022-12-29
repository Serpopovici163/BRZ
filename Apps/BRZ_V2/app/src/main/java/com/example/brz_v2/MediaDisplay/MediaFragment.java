package com.example.brz_v2.MediaDisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.brz_v2.MediaActivity;
import com.example.brz_v2.R;

public class MediaFragment extends Fragment {

    MediaActivity parent;

    boolean playState = false; //keeps track of whether music is playing

    public MediaFragment(MediaActivity mParent) {
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
        return inflater.inflate(R.layout.fragment_media, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        //get view items
        TextView songNameTextView = requireView().requireViewById(R.id.song_name_text);
        TextView artistNameTextView = requireView().requireViewById(R.id.artist_name_text);
        ImageView albumCoverImageView = requireView().requireViewById(R.id.album_cover_image);

        TextView songTimeTextView = requireView().requireViewById(R.id.song_time_text);
        TextView songLengthTextView = requireView().requireViewById(R.id.song_length_text);
        ProgressBar songProgressBar = requireView().requireViewById(R.id.song_progress_bar);

        //sample content for UI design purposes
        songNameTextView.setText("Bussin'");
        artistNameTextView.setText("Nicki Minaj feat. Future");
        albumCoverImageView.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.ic_launcher_background));
        songTimeTextView.setText("1:23");
        songLengthTextView.setText("3:42");
        songProgressBar.setProgress(42); //progress here is out of 100

        //initialize buttons
        ImageButton play_pause = requireView().requireViewById(R.id.play_pause_button);
        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //change image
                if (!playState) {
                    play_pause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.pause_button_img));
                    playState = true;
                } else {
                    play_pause.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.play_button_img));
                    playState = false;
                }
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}