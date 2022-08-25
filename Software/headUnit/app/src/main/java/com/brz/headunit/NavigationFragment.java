package com.brz.headunit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.brz.headunit.R;

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        WebView webView = requireView().requireViewById(R.id.navigation_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setBackgroundColor(0);
        webView.setClickable(false);
        webView.loadUrl("https://www.google.com/maps");

    }
}