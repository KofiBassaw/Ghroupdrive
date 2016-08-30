package com.ghroupdrive.app;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

/**
 * Created by matiyas on 8/12/16.
 */
public class DriverTrips extends AppCompatActivity implements View.OnClickListener {




    private static final int JOIN = 1;


    ViewPager vpPager;
    private static final int NUM_PAGES = 2;
    private PagerAdapter mPagerAdapter;
    RelativeLayout rlJoin;
    RippleView rpJoin;
    String startID,endID,firstID,lastID;
    static public String startOffTime = "";

    static public int seat1=0,seat2=0,seat3=0,seat4=0;
    UserFunctions functions;
    ProgressDialog pDIalogi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_trip);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        rlJoin = (RelativeLayout) findViewById(R.id.rlJoin);
        rpJoin = (RippleView) findViewById(R.id.rpJoin);
        startID = getIntent().getStringExtra("startID");
        endID = getIntent().getStringExtra("endID");
        firstID = getIntent().getStringExtra("firstID");
        lastID = getIntent().getStringExtra("lastID");
        functions = new UserFunctions(this);
        seat1=0;
        seat2=0;
        seat3=0;
        seat4=0;
        startOffTime = "";


        vpPager.setPageMarginDrawable(R.color.grey_color);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mPagerAdapter);

        vpPager.setClipToPadding(false);
        vpPager.setPadding(70, 0, 70, 0);
        vpPager.setCurrentItem(1);






        rlJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rlJoin:
                rpJoin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        if(startOffTime.contentEquals(""))
                        {
                            functions.showMessage("Set off time is not set");
                        }else
                        {
                            JsonObject data = new JsonObject();
                            data.addProperty(StaticVariables.STARTOFFTIME,startOffTime);
                            data.addProperty(StaticVariables.CARTYPE, functions.getPref(StaticVariables.CARTYPE,""));
                            data.addProperty(StaticVariables.ACCESSCODE, functions.getPref(StaticVariables.ACCESSCODE,""));
                            data.addProperty(StaticVariables.STARTING, startID);
                            data.addProperty(StaticVariables.MID1, firstID);
                            data.addProperty(StaticVariables.MID2,lastID);
                            data.addProperty(StaticVariables.ENDING, endID);
                            data.addProperty(StaticVariables.SEAT1,seat1);
                            data.addProperty(StaticVariables.SEAT2,seat2);
                            data.addProperty(StaticVariables.SEAT3,seat3);
                            data.addProperty(StaticVariables.SEAT4,seat4);

                              postRide(data);


                        }









                    }
                });
                break;
        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new DriverTripOthersFragment();
                case 1:
                    return new DriverTripDetailsFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }











    @Override
    protected void onResume() {
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                StaticVariables.TRIPMESSAGE));
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            Log.e("rror", "> " + e.getMessage());
        }
        super.onDestroy();
    }




    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("type");




        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == JOIN && resultCode == RESULT_OK)
        {
            setResult(RESULT_OK);
            onBackPressed();
        }
    }






    private void postRide(JsonObject data)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Posting a ride ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + "JoinRide")
                    .setJsonObjectBody(data)
                    .asString()
                    .setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            try {
                                if (e != null) {
                                    e.printStackTrace();
                                    //System.out.println("---------------------------------- error");
                                }
                                System.out.println("bbbbbb: " + result);
                                pDIalogi.dismiss();
                                if (result != null) {
                                    JSONObject json = new JSONObject(result);
                                    int code = json.getInt(StaticVariables.CODE);
                                    String message = functions.getJsonString(json,StaticVariables.MESSAGE);
                                    if(code ==  200)
                                    {
                                        Intent it = new Intent(DriverTrips.this, DriverJoinActivity.class);
                                        it.putExtra(StaticVariables.BASEPRICE, getTotlaPrice());
                                        startActivityForResult(it, JOIN);
                                    }else {
                                        functions.showMessage(message);

                                    }




                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Unable to post ride please tray again later");

                                }

                            } catch (Exception ex) {
                                pDIalogi.dismiss();
                                ex.printStackTrace();
                            }
                        }
                    });


        } else {
            pDIalogi.dismiss();
            Toast.makeText(this, "No internet Connection Please try again later", Toast.LENGTH_LONG).show();


        }



    }



    private double getTotlaPrice()
    {
        Double basePrice = Double.parseDouble(""+functions.getPref(StaticVariables.BASEPRICE,2));


        return  (((seat1*basePrice)/2) +  ((seat2*basePrice)/2) +  ((seat3*basePrice)/2) +  ((seat4*basePrice)/2));

    }

}
