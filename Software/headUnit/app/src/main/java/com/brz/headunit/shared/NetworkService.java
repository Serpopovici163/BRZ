package com.brz.headunit.shared;

import org.json.JSONObject;

public class NetworkService {

    //list of remote IDs to make code more legible. These are used by other classes to specify what computer to communicate with
    public static int ARDUINO_ID = 0;
    public static int DIAGNOSTIC_ID = 1;

    public JSONObject getData(JSONObject requestData, int remoteID) {
        return null;
    }
}
