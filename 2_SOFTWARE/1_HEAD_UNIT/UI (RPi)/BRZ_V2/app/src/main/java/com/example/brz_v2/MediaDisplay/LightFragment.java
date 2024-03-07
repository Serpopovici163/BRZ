package com.example.brz_v2.MediaDisplay;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.brz_v2.MediaActivity;
import com.example.brz_v2.R;

public class LightFragment extends Fragment {

    MediaActivity parent;

    public LightFragment(MediaActivity mParent) {
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
        return inflater.inflate(R.layout.fragment_light, container, false);
    }
}