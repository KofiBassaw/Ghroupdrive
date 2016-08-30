package com.ghroupdrive.app;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by matiyas on 8/26/16.
 */
public class RideObject {

JSONObject Driver;
JSONArray Passengers;
String RideId;
String AvailableSeats;
String CarType;
String Price;
String SetOffTime;
JSONObject RideStart;
JSONObject Stop1;
JSONObject Stop2;
JSONObject RideEnd;
int Seat1;
int Seat2;
int Seat3;
int Seat4;
int Rate;


    public RideObject(String jsonString, UserFunctions function)
    {

        try
        {
            JSONObject json = new JSONObject(jsonString);
            this.Driver = function.getJsonObject(json,StaticVariables.DRIVER);
            this.Passengers = function.getJsonArray(json, StaticVariables.PASSENGERS);
            this.RideStart = function.getJsonObject(json, StaticVariables.STARTING);
            this.Stop1 = function.getJsonObject(json, StaticVariables.MID1);
            this.Stop2 = function.getJsonObject(json, StaticVariables.MID2);
            this.RideEnd = function.getJsonObject(json, StaticVariables.ENDING);
            this.RideId = function.getJsonString(json, StaticVariables.RIDEID);
            this.AvailableSeats = function.getJsonString(json, StaticVariables.AVAILABLESEAT);
            this.CarType = function.getJsonString(json, StaticVariables.CARTYPE);
            this.Price = function.getJsonString(json, StaticVariables.PRICE);
            this.SetOffTime = function.getJsonString(json, StaticVariables.STARTOFFTIME);
            this.Seat1 = function.getInt(json, StaticVariables.SEAT1);
            this.Seat2 = function.getInt(json, StaticVariables.SEAT2);
            this.Seat3 = function.getInt(json, StaticVariables.SEAT3);
            this.Seat4 = function.getInt(json, StaticVariables.SEAT4);
            this.Rate = function.getInt(json, StaticVariables.RATE);


        }catch (Exception ext)
        {
            ext.printStackTrace();
        }
    }




}
