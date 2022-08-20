package com.brz.headunit.services;

import android.os.SystemClock;
import android.util.Log;

import com.brz.headunit.FullscreenActivity;
import com.brz.headunit.TrafficAdvisorFragment;

public class TrafficAdvisorService {

    FullscreenActivity fullscreenActivity;
    NetworkService networkService = new NetworkService();

    private boolean sirenState = false;

    private boolean hornState = false;
    private Long hornTriggerTime;
    private boolean hornTimerState = false;

    private boolean lightState = false;
    private Long lightTriggerTime;
    private boolean lightTimerState = false;

    private int androidProcessingDelay = 500;

    public TrafficAdvisorService(FullscreenActivity parentActivity) {
        this.fullscreenActivity = parentActivity;
    }

    public void initiateService() {
        networkService.initiateClient(NetworkService.TRAFFIC_ADVISOR_SERVICE);
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
                fullscreenActivity.forwardHornState(true);
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
                fullscreenActivity.forwardHornState(false);
            }
        };
        hornTimer.start();
    }

    //for toggling siren
    public boolean getSirenState() { return sirenState; }

    public void setSirenState(boolean state) {
        sirenState = state;
        fullscreenActivity.forwardSirenState(state);
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
                fullscreenActivity.forwardLightState(true);
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
                fullscreenActivity.forwardLightState(false);
            }
        };
        lightTimer.start();
    }

    public boolean getLightState() { return lightState; }

    public void setLightState(int selectedLightCycle, boolean state) {
        lightState = state;
        fullscreenActivity.forwardLightState(state);
    }

    public void flashBrakeLight() {
        //TODO: make this a runnable thread that runs all the time?
    }

    public void setLegalMode() {
        lightState = false;
        hornState = false;
        sirenState = false;

        fullscreenActivity.forwardLightState(false);
        fullscreenActivity.forwardHornState(false);
        fullscreenActivity.forwardSirenState(false);
    }
}
