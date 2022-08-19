package com.brz.headunit.services;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkService {

    public static int DEFENCE_SERVICE = 0;
    public static int DIAGNOSTIC_SERVICE = 1;
    public static int MEDIA_SERVICE = 2;
    public static int NAVIGATION_SERVICE = 3;
    public static int SAFETY_SERVICE = 4;
    public static int TRAFFIC_ADVISOR_SERVICE = 5;

    private int clientID = 0;

    //TODO: insert relevant addresses here
    String arduinoURL = "192.168.0.1";
    String diagnosticURL = "192.168.0.2";

    String relevantURL;

    //list of remote IDs to make code more legible. These are used by other classes to specify what computer to communicate with
    public static int ARDUINO_ID = 0;
    public static int DIAGNOSTIC_ID = 1;

    public void initiateClient(int desiredClientID) {
        clientID = desiredClientID;
        //also set relevant url
        if (clientID == ARDUINO_ID)
            relevantURL = arduinoURL;
        else if (clientID == DIAGNOSTIC_ID)
            relevantURL = diagnosticURL;
    }

    public JSONObject getData(JSONObject requestData) {

        //copied from https://stackoverflow.com/questions/34691175/how-to-send-httprequest-and-get-json-response-in-android
        HttpURLConnection urlConnection = null;
        JSONObject returnData;

        try {
            URL url = new URL(relevantURL);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */ );
            urlConnection.setConnectTimeout(15000 /* milliseconds */ );
            urlConnection.setDoOutput(true);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder sb = new StringBuilder();

            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();

            String jsonString = sb.toString();
            System.out.println("JSON: " + jsonString);

            returnData = new JSONObject(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestData;
    }

    public void sendData() {

    }
}
