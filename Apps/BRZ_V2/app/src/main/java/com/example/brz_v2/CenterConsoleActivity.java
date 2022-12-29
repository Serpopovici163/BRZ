package com.example.brz_v2;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;

import androidx.annotation.Nullable;

public class CenterConsoleActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //locate and populate both displays
        DisplayManager displayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = displayManager.getDisplays();

        //double check both displays work
        if (displays.length != 2)
            Log.e("Displays", "missing displays"); //ideally handle this better but I don't wanna build this now

        //populate media display
        ActivityOptions options = ActivityOptions.makeBasic();
        options.setLaunchDisplayId(displays[1].getDisplayId());
        startActivity(new Intent(this, MediaActivity.class), options.toBundle());
    }
}
