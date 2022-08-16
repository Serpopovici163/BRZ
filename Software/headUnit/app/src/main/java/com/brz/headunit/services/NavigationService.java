package com.brz.headunit.services;

public class NavigationService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.NAVIGATION_SERVICE);
    }

}
