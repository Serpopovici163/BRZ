package com.brz.headunit;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.FragmentTransaction;

import com.brz.headunit.databinding.ActivityFullscreenBinding;

public class FullscreenActivity extends AppCompatActivity {

    private View mControlsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityFullscreenBinding binding;
        binding = ActivityFullscreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mControlsView = binding.fullscreenContentControls;

        hide();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //get fragment highlight views
        ImageView bigFragmentHighlight = requireViewById(R.id.big_fragment_highlight_view);
        ImageView smallFragmentHighlight = requireViewById(R.id.small_fragment_highlight_view);

        //initialize fragments now that activity is created
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        //add fragments to their containers
        DefenseFragment defenseFragment = new DefenseFragment();
        PopUpDefenseFragment popUpDefenseFragment = new PopUpDefenseFragment();
        DiagnosticFragment diagnosticFragment = new DiagnosticFragment();
        MediaFragment mediaFragment = new MediaFragment();
        NavigationFragment navigationFragment = new NavigationFragment();

        fragmentTransaction.add(R.id.big_fragment, navigationFragment);
        fragmentTransaction.add(R.id.big_fragment, diagnosticFragment);
        fragmentTransaction.add(R.id.big_fragment, defenseFragment);
        fragmentTransaction.add(R.id.small_fragment, mediaFragment);
        fragmentTransaction.add(R.id.small_fragment, popUpDefenseFragment);

        //show specific fragments
        fragmentTransaction.hide(diagnosticFragment);
        fragmentTransaction.hide(navigationFragment);
        fragmentTransaction.hide(mediaFragment);

        fragmentTransaction.commit();

        //initialize keyboard listener here

    }

    private void hide() {
        // Hide app UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        //test stuff
        WindowInsetsControllerCompat windowInsetsController =
                ViewCompat.getWindowInsetsController(getWindow().getDecorView());
        if (windowInsetsController == null) {
            return;
        }
        // Configure the behavior of the hidden system bars
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());
    }
}