package com.brz.headunit.defence;

public class DefenceService {
    boolean cellJamState = false; //ID 0
    boolean radioJamState = false; //ID 1
    boolean lidarJamState = false; //ID 2
    boolean radarJamState = false; //ID 3

    public boolean isCellJamState() {
        return cellJamState;
    }

    public void setCellJamState(boolean cellJamState) {
        this.cellJamState = cellJamState;
    }

    public boolean isRadioJamState() {
        return radioJamState;
    }

    public void setRadioJamState(boolean radioJamState) {
        this.radioJamState = radioJamState;
    }

    public boolean isLidarJamState() {
        return lidarJamState;
    }

    public void setLidarJamState(boolean lidarJamState) {
        this.lidarJamState = lidarJamState;
    }

    public boolean isRadarJamState() {
        return radarJamState;
    }

    public void setRadarJamState(boolean radarJamState) {
        this.radarJamState = radarJamState;
    }
}
