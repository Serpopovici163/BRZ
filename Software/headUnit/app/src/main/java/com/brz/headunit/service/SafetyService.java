package com.brz.headunit.service;

import com.brz.headunit.Main;

public class SafetyService {

    NetworkService networkService = new NetworkService();
    Main main;

    public SafetyService(Main main) {
        this.main = main;
    }

    public void initiateService() {
        networkService.initiateClient(NetworkService.SAFETY_SERVICE);
    }

}
