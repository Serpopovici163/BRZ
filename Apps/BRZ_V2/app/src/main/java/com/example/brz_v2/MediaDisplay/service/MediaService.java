package com.example.brz_v2.MediaDisplay.service;

import com.example.brz_v2.MediaActivity;

public class MediaService {

    MediaActivity parent;

    public MediaService(MediaActivity mParent) {
        parent = mParent;
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
