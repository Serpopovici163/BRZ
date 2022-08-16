package com.brz.headunit.services;

import org.json.JSONObject;

public class NetworkService {

    public static int DEFENCE_SERVICE = 0;
    public static int DIAGNOSTIC_SERVICE = 1;
    public static int MEDIA_SERVICE = 2;
    public static int NAVIGATION_SERVICE = 3;
    public static int SAFETY_SERVICE = 4;
    public static int TRAFFIC_ADVISOR_SERVICE = 5;

    private int clientID = 0;

    //list of remote IDs to make code more legible. These are used by other classes to specify what computer to communicate with
    public static int ARDUINO_ID = 0;
    public static int DIAGNOSTIC_ID = 1;

    public void initiateClient(int desiredClientID) {
        clientID = desiredClientID;
    }

    public JSONObject getData(JSONObject requestData, int remoteID) {

        return null;
    }
}
