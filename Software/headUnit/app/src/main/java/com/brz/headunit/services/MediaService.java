package com.brz.headunit.services;

import android.util.Log;

public class MediaService {

    NetworkService networkService = new NetworkService();

    public void initiateService() {
        networkService.initiateClient(NetworkService.MEDIA_SERVICE);
    }

    public void skipSong(boolean direction) { //true is forward and false is backward

    }

    public void togglePlay() { //toggle play state of media

    }

    public void toggleSource() { //toggle between phone and car's internal audio source

    }

    public void changeVolume(boolean up) { //true means volume up and false is volume down

    }
}
