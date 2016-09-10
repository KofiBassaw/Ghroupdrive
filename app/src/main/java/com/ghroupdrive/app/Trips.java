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
import android.widget.TextView;
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
public class Trips extends AppCompatActivity implements View.OnClickListener {




    private SlidingUpPanelLayout mLayout;
    private static final int JOIN = 1;
    RelativeLayout rlStart,rlPoint1,rlPoint2,rlEndPoint;


    ViewPager vpPager;
    private static final int NUM_PAGES = 3;
    private PagerAdapter mPagerAdapter;
    RelativeLayout rlJoin;
    RippleView rpJoin;
    String jsonSTring = "";
    UserFunctions functions;
    TextView tvStart,tvPoint1,tvPoint2,tvEndPoint;
    public static int pos = 0;
    public static LocationObj selectedLoc;
    RideObject rb;
    ProgressDialog pDIalogi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trip);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        rlJoin = (RelativeLayout) findViewById(R.id.rlJoin);
        rlStart = (RelativeLayout) findViewById(R.id.rlStart);
        rlPoint1 = (RelativeLayout) findViewById(R.id.rlPoint1);
        rlPoint2 = (RelativeLayout) findViewById(R.id.rlPoint2);
        rlEndPoint = (RelativeLayout) findViewById(R.id.rlEndPoint);
        tvEndPoint = (TextView) findViewById(R.id.tvEndPoint);
        tvStart = (TextView) findViewById(R.id.tvStart);
        tvPoint1 = (TextView) findViewById(R.id.tvPoint1);
        tvPoint2 = (TextView) findViewById(R.id.tvPoint2);
        rpJoin = (RippleView) findViewById(R.id.rpJoin);
        jsonSTring = getIntent().getStringExtra(StaticVariables.JSONSTRING);

        vpPager.setPageMarginDrawable(R.color.grey_color);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mPagerAdapter);

        vpPager.setClipToPadding(false);
        vpPager.setPadding(70, 0, 70, 0);
        vpPager.setCurrentItem(1);
        functions = new UserFunctions(this);

         rb = new RideObject(jsonSTring,functions);




        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        mLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelExpanded(View panel) {

            }

            @Override
            public void onPanelCollapsed(View panel) {

            }

            @Override
            public void onPanelAnchored(View panel) {
            }

            @Override
            public void onPanelHidden(View panel) {

            }
        });


        rlJoin.setOnClickListener(this);
        rlStart.setOnClickListener(this);
        rlPoint1.setOnClickListener(this);
        rlPoint2.setOnClickListener(this);
        rlEndPoint.setOnClickListener(this);

        bindData();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rlJoin:
                rpJoin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        if( selectedLoc == null)
                        {
                            functions.showMessage("Select a Seat");
                        }else {

                                JsonObject json = new JsonObject();
                            json.addProperty(StaticVariables.ACCESSCODE,functions.getPref(StaticVariables.ACCESSCODE,""));
                            json.addProperty(StaticVariables.RIDEID, rb.RideId);
                            json.addProperty(StaticVariables.SEAT, pos);

                            joinRide(json);


                        }




                    }
                });
                break;
            case R.id.rlStart:
                //start point clicked
                Intent it = new Intent(StaticVariables.TRIPSELECTION);
                it.putExtra("pos",pos);
                it.putExtra("type","select");
                it.putExtra("point","start");
                sendBroadcast(it);

                closePanel();
                break;
            case R.id.rlPoint1:
                //mid point clicked 1
                Intent it1 = new Intent(StaticVariables.TRIPSELECTION);
                it1.putExtra("pos",pos);
                it1.putExtra("type","select");
                it1.putExtra("point","point1");
                sendBroadcast(it1);
                closePanel();
                break;

            case R.id.rlPoint2:
                //mid point clicked 2
                Intent it2 = new Intent(StaticVariables.TRIPSELECTION);
                it2.putExtra("pos",pos);
                it2.putExtra("type","select");
                it2.putExtra("point","point2");
                sendBroadcast(it2);
                closePanel();
                break;
            case R.id.rlEndPoint:
                //end point clicked
                Intent it3 = new Intent(StaticVariables.TRIPSELECTION);
                it3.putExtra("pos",pos);
                it3.putExtra("type","select");
                it3.putExtra("point","end");
                sendBroadcast(it3);
                closePanel();
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
                    return new TripOthersFragment();
                case 1:
                    return new TripDetailsFragment();
                case 2:
                    return new TripSubDetailsFragment();

            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }








    private void showPanel()
    {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);

    }


    private void closePanel()
    {
        mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

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

            if (type.contentEquals("open"))
            {
                pos = intent.getExtras().getInt("pos");
                showPanel();
            }


        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == JOIN && resultCode == RESULT_OK)
        {
            onBackPressed();
        }
    }



    private void bindData()
    {
          try
          {




                  if(rb.RideStart !=null)
                  {
                      tvStart.setText(functions.getJsonString(rb.RideStart , StaticVariables.NAME));
                  }



                  if(rb.RideEnd != null)
                  {
                      tvEndPoint.setText(functions.getJsonString(rb.RideEnd, StaticVariables.NAME));
                  }


;

                  if(rb.Stop1 != null)
                  {
                      tvPoint1.setText(functions.getJsonString(rb.Stop1 , StaticVariables.NAME));
                  }else{
                      rlPoint1.setVisibility(View.GONE);
                  }


                  if(rb.Stop2 != null)
                  {
                      tvPoint2.setText(functions.getJsonString(rb.Stop2, StaticVariables.NAME));
                  }else{
                      rlPoint2.setVisibility(View.GONE);
                  }






          }catch (Exception ex)
          {
              ex.printStackTrace();
          }
    }





    private void joinRide(JsonObject data)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Joining ...");
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
                                        functions.setPref(rb.RideId,pos);
                                        Intent it = new Intent(Trips.this,RiderJoinActivity.class);
                                        it.putExtra(StaticVariables.JSONSTRING,jsonSTring);
                                        it.putExtra(StaticVariables.SEAT,selectedLoc.Name);
                                        it.putExtra("from","trip");
                                        startActivityForResult(it, JOIN);
                                    }else {
                                        functions.showMessage(message);

                                    }




                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Unable to join the ride please tray again later");

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

}
