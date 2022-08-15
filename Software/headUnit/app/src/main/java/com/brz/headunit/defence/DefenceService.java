package com.brz.headunit.defence;

import com.brz.headunit.DefenceFragment;

public class DefenceService {
    boolean cellJamState = false; //ID 0
    boolean radioJamState = false; //ID 1
    boolean lidarJamState = false; //ID 2
    boolean radarJamState = false; //ID 3

    public boolean isCellJamState() {
        return cellJamState;
    }

    public void setCellJamState(boolean cellJamState) {
        DefenceFragment.displayJamState(0, cellJamState);
        this.cellJamState = cellJamState;
    }

    public boolean isRadioJamState() {
        return radioJamState;
    }

    public void setRadioJamState(boolean radioJamState) {
        DefenceFragment.displayJamState(1, radioJamState);
        this.radioJamState = radioJamState;
    }

    public boolean isLidarJamState() {
        return lidarJamState;
    }

    public void setLidarJamState(boolean lidarJamState) {
        DefenceFragment.displayJamState(2, lidarJamState);
        this.lidarJamState = lidarJamState;
    }

    public boolean isRadarJamState() {
        return radarJamState;
    }

    public void setRadarJamState(boolean radarJamState) {
        DefenceFragment.displayJamState(3, radarJamState);
        this.radarJamState = radarJamState;
    }
}
