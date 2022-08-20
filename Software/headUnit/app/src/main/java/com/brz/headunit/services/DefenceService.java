package com.brz.headunit.services;

import com.brz.headunit.DefenceFragment;
import com.brz.headunit.FullscreenActivity;

public class DefenceService {

    NetworkService networkService = new NetworkService();

    FullscreenActivity fullscreenActivity;

    public DefenceService(FullscreenActivity fullscreenActivity) {
        this.fullscreenActivity = fullscreenActivity;
    }

    boolean cellJamState = false; //ID 0
    boolean radioJamState = false; //ID 1
    boolean lidarJamState = false; //ID 2
    boolean radarJamState = false; //ID 3

    boolean legalJamState = false;

    public void initiateService() {
        networkService.initiateClient(NetworkService.DEFENCE_SERVICE);
    }

    public void setLegalMode() {
        cellJamState = false;
        radarJamState = false;
        radioJamState = false;
        lidarJamState = false;

        //broadcast to network
        networkService.sendData();
    }

    public void resetJamState() { //required to make sure legal jammers are reset once legalMode is disabled
        cellJamState = false;
        radarJamState = false;
        radioJamState = false;
        lidarJamState = false;
    }

    //TODO: make thread to handle incoming data

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

    public void toggleLegalJammers() {
        legalJamState = !legalJamState;
        cellJamState = true;
        radioJamState = true;

        //handle jammer startup here
    }

    void triggerAlert(int countermeasureID) { //red highlight

    }

    void cancelAlert(int countermeasureID) {

    }

    void triggerWarning(int countermeasureID) { //yellow highlight

    }

    void cancelWarning(int countermeasureID) {

    }
}
