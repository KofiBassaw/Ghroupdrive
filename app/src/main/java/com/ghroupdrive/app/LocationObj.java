package com.ghroupdrive.app;

import org.json.JSONObject;

/**
 * Created by matiyas on 8/26/16.
 */
public class LocationObj {

    String Id;
    String Name;
    String Longitude;
    String Latitude;



    public LocationObj(String jsonString, UserFunctions function)
    {
      try
      {
          JSONObject json = new JSONObject(jsonString);
          this.Id = function.getJsonString(json,StaticVariables.ID);
          this.Name = function.getJsonString(json,StaticVariables.NAME);
          this.Longitude = function.getJsonString(json,StaticVariables.LONGITUDE);
          this.Latitude = function.getJsonString(json, StaticVariables.LATITUDE);

      }catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }
}
