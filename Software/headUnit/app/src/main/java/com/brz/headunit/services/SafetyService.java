package com.brz.headunit.services;

import com.brz.headunit.FullscreenActivity;

public class SafetyService {

    NetworkService networkService = new NetworkService();
    FullscreenActivity fullscreenActivity;

    public SafetyService(FullscreenActivity fullscreenActivity) {
        this.fullscreenActivity = fullscreenActivity;
    }

    public void initiateService() {
        networkService.initiateClient(NetworkService.SAFETY_SERVICE);
    }

}
