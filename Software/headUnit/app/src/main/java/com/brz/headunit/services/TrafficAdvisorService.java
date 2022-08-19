package com.brz.headunit.services;

import com.brz.headunit.FullscreenActivity;

public class TrafficAdvisorService {

    NetworkService networkService = new NetworkService();

    private boolean sirenState = false;

    public boolean getSirenState() { return sirenState; }

    public void initiateService() {
        networkService.initiateClient(NetworkService.TRAFFIC_ADVISOR_SERVICE);
    }

    public void setSirenState(boolean state) {

    }

    public void setHornState(boolean state) {

    }

    public void setLightState(int selectedLightCycle, boolean state) {

    }

    public void flashBrakeLight() {
        //TODO: make this a runnable thread that runs all the time?
    }

    public void setLegalMode() {
        //kill all lights here
    }
}
