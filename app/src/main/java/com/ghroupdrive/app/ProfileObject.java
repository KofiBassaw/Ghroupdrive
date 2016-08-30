package com.ghroupdrive.app;

import org.json.JSONObject;

/**
 * Created by matiyas on 8/26/16.
 */
public class ProfileObject {

    String Fullname;
    String MobileNo;
    String ProfileUrl;
    String BannerUrl;
    String Car1Url;
    String Car2Url;
    String Car3Url;
    String Car4Url;
    String AccessCode;
    int InvitesSent;
    int RidesCount;
    int Seat;
    int  BadgesCount;
    int PointsCount;





    public ProfileObject(String jsonObjec, UserFunctions function)
    {
       try
       {
           JSONObject json = new JSONObject(jsonObjec);
           this.Fullname = function.getJsonString(json, StaticVariables.FULLNAME);
           this.MobileNo = function.getJsonString(json, StaticVariables.MOBILENUMBER);
           this.ProfileUrl = function.getJsonString(json, StaticVariables.PROFILE);
           this.BannerUrl = function.getJsonString(json, StaticVariables.PROFILEBANNER);
           this.AccessCode = function.getJsonString(json, StaticVariables.ACCESSCODE);
           this.Car1Url = function.getJsonString(json, StaticVariables.IMAGE1);
           this.Car2Url = function.getJsonString(json, StaticVariables.IMAGE2);
           this.Car3Url = function.getJsonString(json, StaticVariables.IMAGE3);
           this.Car4Url = function.getJsonString(json, StaticVariables.IMAGE4);
           this.InvitesSent = function.getInt(json, StaticVariables.INVITESSENT);
           this.RidesCount = function.getInt(json, StaticVariables.RIDESCOUNT);
           this.BadgesCount = function.getInt(json, StaticVariables.BADGESCOUNT);
           this.PointsCount = function.getInt(json, StaticVariables.POINTCOUNT);
           this.Seat = function.getInt(json, StaticVariables.SEAT);


       }catch (Exception ex)
       {
         ex.printStackTrace();
       }
    }










}
