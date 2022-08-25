package com.brz.headunit;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brz.headunit.R;

public class DiagnosticFragment extends Fragment {

    public DiagnosticFragment() {
        // Required empty public constructor
    }

    TextView[] textViews = new TextView[14];
    ImageView[] imageViews = new ImageView[14];
    int[] statusList = new int[14];

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnostic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //initialize fields
        textViews[0] = requireView().requireViewById(R.id.maintenance_textview);
        textViews[1] = requireView().requireViewById(R.id.stability_textview);
        textViews[2] = requireView().requireViewById(R.id.engine_textview);
        textViews[3] = requireView().requireViewById(R.id.fuel_textview);
        textViews[4] = requireView().requireViewById(R.id.main_power_textview);
        textViews[5] = requireView().requireViewById(R.id.network_textview);
        textViews[6] = requireView().requireViewById(R.id.vision_textview);
        textViews[7] = requireView().requireViewById(R.id.sensor_textview);
        textViews[8] = requireView().requireViewById(R.id.serial_textview);
        textViews[9] = requireView().requireViewById(R.id.defence_textview);
        textViews[10] = requireView().requireViewById(R.id.compute_textview);
        textViews[11] = requireView().requireViewById(R.id.vehicle_textview);
        textViews[12] = requireView().requireViewById(R.id.safety_textview);
        textViews[13] = requireView().requireViewById(R.id.aux_power_textview);

        imageViews[0] = requireView().requireViewById(R.id.maintenance_highlight);
        imageViews[1] = requireView().requireViewById(R.id.stability_highlight);
        imageViews[2] = requireView().requireViewById(R.id.engine_highlight);
        imageViews[3] = requireView().requireViewById(R.id.fuel_highlight);
        imageViews[4] = requireView().requireViewById(R.id.main_power_highlight);
        imageViews[5] = requireView().requireViewById(R.id.network_highlight);
        imageViews[6] = requireView().requireViewById(R.id.vision_highlight);
        imageViews[7] = requireView().requireViewById(R.id.sensor_highlight);
        imageViews[8] = requireView().requireViewById(R.id.serial_highlight);
        imageViews[9] = requireView().requireViewById(R.id.defence_highlight);
        imageViews[10] = requireView().requireViewById(R.id.compute_highlight);
        imageViews[11] = requireView().requireViewById(R.id.vehicle_highlight);
        imageViews[12] = requireView().requireViewById(R.id.safety_highlight);
        imageViews[13] = requireView().requireViewById(R.id.aux_power_highlight);

        updateIcons();
    }

    void updateIcons() {
        //TODO: implement network calls
        //for now set all to green
        for (int i = 0; i < 14; i++)
            setIconStatus(i, 0);
    }

    void setIconStatus(int iconID, int status) {
        //status --> 0/ok 1/warn 2/alert

        //save status for thread generated below
        statusList[iconID] = status;

        if (status == 0) {
            textViews[iconID].setBackgroundColor(Color.GREEN);
            imageViews[iconID].setVisibility(View.INVISIBLE);
        } else if (status == 1) {
            textViews[iconID].setBackgroundColor(Color.YELLOW);
            imageViews[iconID].setVisibility(View.INVISIBLE);
        } else {
            textViews[iconID].setBackgroundColor(Color.RED);

            //start alert thread
            Thread thread = new Thread() {
                @Override
                public void run() {
                    //do alert animation
                    while (statusList[iconID] == 2) {
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[iconID].setVisibility(View.VISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                        requireActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageViews[iconID].setVisibility(View.INVISIBLE);
                            }
                        });
                        SystemClock.sleep(250);
                    }
                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            imageViews[iconID].setVisibility(View.INVISIBLE);
                        }
                    });
                }
            };
            thread.start();
        }
    }
}