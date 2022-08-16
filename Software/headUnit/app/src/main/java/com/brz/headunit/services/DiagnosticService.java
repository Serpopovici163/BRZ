package com.brz.headunit.services;

public class DiagnosticService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.DIAGNOSTIC_SERVICE);
    }

}
