package com.example.brz_v2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.brz_v2.CenterConsole.CenterConsoleConvenienceFragment;
import com.example.brz_v2.CenterConsole.CenterConsoleDiagnosticFragment;
import com.example.brz_v2.CenterConsole.CenterConsoleLauncherFragment;
import com.example.brz_v2.databinding.ActivityCenterConsoleBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class CenterConsoleActivity extends AppCompatActivity {

    private TabLayout masterTabLayout;
    private ViewPager2 masterViewPager;
    private TextView statusTextView;

    private FragmentStateAdapter fragmentStateAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //load layout
        ActivityCenterConsoleBinding binding;
        binding = ActivityCenterConsoleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //hide system UI
        WindowInsetsControllerCompat windowInsetsController =
                WindowCompat.getInsetsController(getWindow(), getWindow().getDecorView());
        // Configure the behavior of the hidden system bars.
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars());

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();

        //locate and populate both displays
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        //double check both displays work
        if (displays.length != 2)
            Log.e("Displays", "missing displays"); //TODO: ideally handle this better but I don't wanna build this now

        //populate media display
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(displays[1].getDisplayId());
        startActivity(new Intent(this, MediaActivity.class), options.toBundle());
    }

    @Override
    protected void onStart() {

        //attach to layout
        masterTabLayout = requireViewById(R.id.center_console_tab_layout);
        masterViewPager = requireViewById(R.id.center_console_parent_viewpager);
        statusTextView = requireViewById(R.id.center_console_status_textview);

        //populate viewpager
        fragmentStateAdapter = new FragmentStateAdapter(getSupportFragmentManager(), getLifecycle()) {
            @NonNull
            @Override
            public Fragment createFragment(int position) {
                if (position == 0)
                    return new CenterConsoleLauncherFragment();
                else if (position == 1)
                    return new CenterConsoleDiagnosticFragment();
                else
                    return new CenterConsoleConvenienceFragment();
            }

            @Override
            public int getItemCount() {
                return 3;
            }
        };
        masterViewPager.setAdapter(fragmentStateAdapter);

        //populate tab layout
        new TabLayoutMediator(masterTabLayout, masterViewPager, (tab, position) -> tab.setText(new String[]{"Apps", "Diagnostics", "Convenience"}[position])).attach();
        Objects.requireNonNull(masterTabLayout.getTabAt(0)).setIcon(R.drawable.apps_icon);
        Objects.requireNonNull(masterTabLayout.getTabAt(1)).setIcon(R.drawable.diagnostics_icon);
        Objects.requireNonNull(masterTabLayout.getTabAt(2)).setIcon(R.drawable.sliders_icon);

        //set diagnostic fragment as default
        masterViewPager.setCurrentItem(1);

        super.onStart();
    }
}
