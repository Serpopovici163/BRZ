package com.brz.headunit.services;

import com.brz.headunit.FullscreenActivity;

public class DiagnosticService {

    NetworkService networkService = new NetworkService();
    FullscreenActivity fullscreenActivity;

    public DiagnosticService(FullscreenActivity fullscreenActivity) {
        this.fullscreenActivity = fullscreenActivity;
    }

    public void initiateService() {
        networkService.initiateClient(NetworkService.DIAGNOSTIC_SERVICE);
    }

}
