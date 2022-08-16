package com.brz.headunit.services;

public class SafetyService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.SAFETY_SERVICE);
    }

}
