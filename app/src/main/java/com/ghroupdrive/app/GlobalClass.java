package com.ghroupdrive.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.view.View;
import android.widget.Toast;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by matiyas on 4/6/16.
 */
public class GlobalClass extends Application {

    UserFunctions functions;

    @Override
    public void onCreate() {
        super.onCreate();
       functions = new UserFunctions(this);


        String oldData = "";

        try {
            Database db = new Database(this);
            db.open();
            Cursor c = db.getSampleDetails(StaticVariables.LOCATIONTYPE,"1");
            if(c.getCount()>0)
            {
                c.moveToFirst();
                oldData = c.getString(c.getColumnIndex(Database.JSONSTRING));

                c.close();

                if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
                {
                    new PrepareData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,oldData) ;
                }else {
                    new PrepareData().execute(oldData);
                }

            }


            db.close();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


        getLocation(oldData);




        if(functions.getPref(StaticVariables.HASLOGEDING, false))
        {
            getMyRides(functions.getPref(StaticVariables.ACCESSCODE,""),"2");


            if(functions.getPref(StaticVariables.OFFERRIDE,false))
            {
                getMyRides(functions.getPref(StaticVariables.ACCESSCODE,""),"1");
            }
            getProfile(functions.getPref(StaticVariables.ACCESSCODE,""));
        }


    }





    private void getLocation(final String oldData)
    {

        System.out.println(StaticVariables.BASEURL + "Locations");
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("GET", StaticVariables.BASEURL + "Locations")
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }

                                System.out.println("bbbbbbbbbbbbbbbb: "+    result);
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                    int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {
                                        JSONArray data = functions.getJsonArray(json, StaticVariables.DATA);


                                        if (data != null) {
                                            if(!oldData.contentEquals(data.toString())){

                                                if(data.length()>0)
                                                {
                                                    Database db = new Database(GlobalClass.this);
                                                    db.open();
                                                    db.insertSampleDetails("1",StaticVariables.LOCATIONTYPE,data.toString());
                                                    db.close();


                                                    if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
                                                    {
                                                        new PrepareData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.toString()) ;
                                                    }else {
                                                        new PrepareData().execute(data.toString());
                                                    }

                                                }
                                            }



                                        }

                                    }


                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });


        }



    }





    class PrepareData extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Creating product
         * */
        @Override
        protected String doInBackground(String... args) {


            try
            {
                String jsonArray = args[0];
                JSONArray location = new JSONArray(jsonArray);

                List<String> locIDs = new ArrayList<>();
                List<String> locNames = new ArrayList<>();
                for(int i=0; i<location.length(); i++)
                {
                    JSONObject c = location.getJSONObject(i);
                    locIDs.add(functions.getJsonString(c,StaticVariables.ID));
                    locNames.add(functions.getJsonString(c,StaticVariables.NAME));

                }
                StaticVariables.locIDs = locIDs;
                StaticVariables.locNames = locNames;
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }


            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

            Intent it = new Intent(StaticVariables.LOCATIONINTENT);
            sendBroadcast(it);
        }





    }








    private void getMyRides(String accessCode, final String type)
    {

        System.out.println("bbbbbbbbbbbbbbbb: ");
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("GET", StaticVariables.BASEURL + "UserRides?"+StaticVariables.ACCESSCODE+"="+accessCode+"&Type="+type)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }

                                System.out.println("bbbbbbbbbbbbbbbb: "+    result);
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                    int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {
                                        JSONArray data = functions.getJsonArray(json, StaticVariables.DATA);


                                        if (data != null) {
                                            if(type.contentEquals("2"))
                                            StaticVariables.MYLOC = data.toString();
                                            else
                                            {
                                               StaticVariables.POSTEDRIDES = data.toString();
                                                Intent it = new Intent(StaticVariables.SEARCHMESSAGE);
                                                it.putExtra("type","postedRide");
                                                sendBroadcast(it);
                                            }


                                        }

                                    }


                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });


        }



    }










    private void getProfile(final String accessCode)
    {

        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("GET", StaticVariables.BASEURL + "Profile?"+StaticVariables.ACCESSCODE+"="+accessCode)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }

                                System.out.println("bbbbbbbbbbbbbbbb: "+    result);
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                    int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json, StaticVariables.MESSAGE);
                                    if (code == 200) {
                                        JSONObject data = functions.getJsonObject(json, StaticVariables.DATA);


                                        if (data != null) {

                                            Database db = new Database(GlobalClass.this);
                                            db.open();
                                            db.insertSampleDetails(accessCode,StaticVariables.PROFILETYPE,data.toString());
                                            db.close();

                                        }

                                    }


                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });


        }



    }

}
