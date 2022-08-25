package com.brz.headunit.service;

import android.os.SystemClock;

import com.brz.headunit.Main;

public class TrafficAdvisorService {

    Main main;
    NetworkService networkService = new NetworkService();

    private boolean sirenState = false;

    private boolean hornState = false;
    private Long hornTriggerTime;
    private boolean hornTimerState = false;

    private boolean lightState = false;
    private Long lightTriggerTime;
    private boolean lightTimerState = false;

    private int androidProcessingDelay = 500;

    public TrafficAdvisorService(Main parentActivity) {
        this.main = parentActivity;
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
                main.forwardHornState(true);
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
                main.forwardHornState(false);
            }
        };
        hornTimer.start();
    }

    //for toggling siren
    public boolean getSirenState() { return sirenState; }

    public void setSirenState(boolean state) {
        sirenState = state;
        main.forwardSirenState(state);
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
                main.forwardLightState(true);
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
                main.forwardLightState(false);
            }
        };
        lightTimer.start();
    }

    public boolean getLightState() { return lightState; }

    public void setLightState(int selectedLightCycle, boolean state) {
        lightState = state;
        main.forwardLightState(state);
    }

    public void flashBrakeLight() {
        //TODO: make this a runnable thread that runs all the time?
    }

    public void setLegalMode() {
        lightState = false;
        hornState = false;
        sirenState = false;

        main.forwardLightState(false);
        main.forwardHornState(false);
        main.forwardSirenState(false);
    }
}
