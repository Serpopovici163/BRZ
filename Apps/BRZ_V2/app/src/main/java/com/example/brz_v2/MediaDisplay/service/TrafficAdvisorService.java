package com.example.brz_v2.MediaDisplay.service;

import android.os.SystemClock;

import com.example.brz_v2.MediaActivity;

public class TrafficAdvisorService {

    MediaActivity parent;

    private boolean sirenState = false;

    private boolean hornState = false;
    private Long hornTriggerTime;
    private boolean hornTimerState = false;

    private boolean lightState = false;
    private Long lightTriggerTime;
    private boolean lightTimerState = false;

    private boolean[] lightSettings = new boolean[]{true, true, true, true, false, false}; //keeps track of which lights are active

    private int androidProcessingDelay = 500;

    public TrafficAdvisorService(MediaActivity mParent) {
        parent = mParent;
    }

    public void affirmHornState() {
        hornState = true;
        hornTriggerTime = System.currentTimeMillis();
        if (!hornTimerState)
            hornTimer();
    }

    public void hornTimer() {
        Thread hornTimer = new Thread() {
            @Override
            public void run() {
                hornTimerState = true;
                parent.forwardHornState(true);
                while (hornState) { //set horn on for n arbitrary amount of time
                    SystemClock.sleep(10);

                    //check how long from last trigger
                    //TODO: adjust time based on processing speed of head unit computer
                    //had a lot of issues here since it takes a lot of time to get to this if statement
                    //from the initial time that hornTriggerTime is set
                    if (hornTriggerTime < (System.currentTimeMillis() - androidProcessingDelay))
                        hornState = false;
                }
                hornTimerState = false;
                parent.forwardHornState(false);
            }
        };
        hornTimer.start();
    }

    //for toggling siren
    public boolean getSirenState() { return sirenState; }

    public void setSirenState(boolean state) {
        sirenState = state;
        parent.forwardSirenState(state);
    }

    //for momentary siren
    public void affirmLightState() {
        lightState = true;
        lightTriggerTime = System.currentTimeMillis();
        if (!lightTimerState)
            lightTimer();
    }

    public void lightTimer() {
        Thread lightTimer = new Thread() {
            @Override
            public void run() {
                lightTimerState = true;
                parent.forwardLightState(true);
                while (lightState) {
                    SystemClock.sleep(10); //keep siren on for an arbitrary amount of time

                    //check how long from last trigger
                    //TODO: adjust time based on processing speed of head unit computer
                    //had a lot of issues here since it takes a lot of time to get to this if statement
                    //from the initial time that hornTriggerTime is set
                    if (lightTriggerTime < (System.currentTimeMillis() - androidProcessingDelay))
                        lightState = false;
                }
                lightTimerState = false;
                parent.forwardLightState(false);
            }
        };
        lightTimer.start();
    }

    public boolean getLightState() { return lightState; }

    public void setLightState(int selectedLightCycle, boolean state) {
        lightState = state;
        parent.forwardLightState(state);
    }

    public void updateLightSettings(int lightID, boolean state) {
        lightSettings[lightID] = state;
    }

    public void setLegalMode() {
        lightState = false;
        hornState = false;
        sirenState = false;

        parent.forwardLightState(false);
        parent.forwardHornState(false);
        parent.forwardSirenState(false);
    }
}
