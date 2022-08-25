package com.brz.headunit.service;

import com.brz.headunit.Main;

public class DiagnosticService {

    NetworkService networkService = new NetworkService();
    Main main;

    public DiagnosticService(Main main) {
        this.main = main;
    }

    public void initiateService() {
        networkService.initiateClient(NetworkService.DIAGNOSTIC_SERVICE);
    }

}
