package com.brz.headunit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class NavigationFragment extends Fragment {

    //tutorial for google map directions? Won't be proper but close enough
    //https://www.youtube.com/watch?v=KOBJkkhH9QY
    //https://stackoverflow.com/questions/32810495/google-direction-route-from-current-location-to-known-location
    //https://www.dropbox.com/s/tqwc62cn4dcr7wh/MapDemo.zip

    public NavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }
}