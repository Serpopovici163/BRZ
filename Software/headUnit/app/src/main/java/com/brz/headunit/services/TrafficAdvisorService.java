package com.brz.headunit.services;

public class TrafficAdvisorService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.TRAFFIC_ADVISOR_SERVICE);
    }



    public void toggleLightCycle(int cycleID) {
        if (cycleID == 0) { //fast police cycle

        } else if (cycleID == 1) { //slow police cycle

        } else if (cycleID == 2) { //fast hazard cycle

        } else if (cycleID == 3) { //slow hazard cycle

        }
    }

    public void setHornState(boolean state) {

    }

    public void flashBrakeLight() {
        //TODO: make this a runnable thread that runs all the time?
    }

    public void toggleSiren() {

    }

    public void killLights() {

    }

    public void killSiren() {

    }

    public void setLegalMode() {
        killLights();
        killSiren();
    }
}
