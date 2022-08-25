package com.brz.headunit.service;

public class NavigationService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.NAVIGATION_SERVICE);
    }

}
