package com.brz.headunit;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MergingFragment extends Fragment {

    private ImageView topLeftIcon;
    private ImageView topRightIcon;
    private ImageView bottomLeftIcon;
    private ImageView bottomRightIcon;

    private TextView topLeftText;
    private TextView topRightText;
    private TextView bottomLeftText;
    private TextView bottomRightText;

    private ImageView highlight;

    public MergingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_merging, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        topLeftIcon = requireView().requireViewById(R.id.merging_top_left_icon);
        topRightIcon = requireView().requireViewById(R.id.merging_top_right_icon);
        bottomLeftIcon = requireView().requireViewById(R.id.merging_bottom_left_icon);
        bottomRightIcon = requireView().requireViewById(R.id.merging_bottom_right_icon);

        topLeftText = requireView().requireViewById(R.id.merging_top_left_text);
        topRightText = requireView().requireViewById(R.id.merging_top_right_text);
        bottomLeftText = requireView().requireViewById(R.id.merging_bottom_left_text);
        bottomRightText = requireView().requireViewById(R.id.merging_bottom_right_text);

        highlight = requireView().requireViewById(R.id.merging_highlight);
    }

    private int distanceToColour(float distance) {
        if (distance > 3.0)
            return Color.GREEN;
        else if (distance > 1.0)
            return Color.YELLOW;
        else
            return Color.RED;
    }

    public void updateFragment(float[] distanceData) {

        //distance data comes in as organized distances in m from sensors
        //order is front left, front right, back left, back right

        topLeftText.setText(String.valueOf(distanceData[0]) + getString(R.string.meter_unit)); //TODO replace with 'placeholders'
        topLeftIcon.setColorFilter(distanceToColour(distanceData[0]));
        topRightText.setText("");
        topRightIcon.setColorFilter(distanceToColour(distanceData[1]));
        bottomLeftText.setText("");
        bottomLeftIcon.setColorFilter(distanceToColour(distanceData[2]));
        bottomRightText.setText("");
        bottomRightIcon.setColorFilter(distanceToColour(distanceData[3]));
    }
}