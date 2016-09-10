package com.ghroupdrive.app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ptrack on 8/31/16.
 */
public class PostedRideDetails extends AppCompatActivity {


    String jsonString;
    TextView tvStartingPost,tvEndingPost,tvMid1Post,tvMid2Post;
    ImageView profiles[] = new ImageView[4];
    UserFunctions functions;
    TextView tvTime,tvPrice,tvReserved;
    RideObject rb;
    RelativeLayout rlSetOff;
    RippleView rpSetOff;
    ProgressDialog pDIalogi;
    int status = 0;
    TextView tvSetOff;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.posted_ride_details);
        functions = new UserFunctions(this);
        profiles[0] = (ImageView) findViewById(R.id.ivProfile1);
        profiles[1] = (ImageView) findViewById(R.id.ivProfile2);
        profiles[2] = (ImageView) findViewById(R.id.ivProfile3);
        profiles[3] = (ImageView) findViewById(R.id.ivProfile4);
        tvStartingPost = (TextView) findViewById(R.id.tvStarting);
        tvEndingPost = (TextView) findViewById(R.id.tvEndPoint);
        tvMid1Post = (TextView) findViewById(R.id.tvPoint1);
        tvMid2Post = (TextView) findViewById(R.id.tvPoint2);
        tvTime = (TextView) findViewById(R.id.tvTime);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        tvSetOff = (TextView) findViewById(R.id.tvSetOff);
        tvReserved = (TextView) findViewById(R.id.tvReserved);
        rlSetOff = (RelativeLayout) findViewById(R.id.rlSetOff);
        rpSetOff = (RippleView) findViewById(R.id.rpSetOff);
        jsonString = getIntent().getStringExtra(StaticVariables.JSONSTRING);




        postedRide();




        rlSetOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rpSetOff.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        JsonObject json = new JsonObject();
                        json.addProperty(StaticVariables.ACCESSCODE,functions.getPref(StaticVariables.ACCESSCODE,""));
                        json.addProperty(StaticVariables.RIDEID,rb.RideId);
                        json.addProperty(StaticVariables.STATUS,(status+1));

                        setOff(json,"RideUpdate");


                    }
                });
            }
        });
    }





    private void setOff(JsonObject data, String function)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Setting off ...");
        pDIalogi.show();
        ConnectionDetector cd=new ConnectionDetector(this);
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("POST", StaticVariables.BASEURL + function)
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
                                        setResult(RESULT_OK);
                                        onBackPressed();

                                    }else {
                                        functions.showMessage(message);

                                    }




                                } else {
                                    pDIalogi.dismiss();
                                    functions.showMessage("Unable to verify invite please tray again later");

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




    private void postedRide()
    {
        try {


            tvPrice.setText("GHS "+functions.getPref(StaticVariables.BASEPRICE,"2"));
                JSONObject oneJson = new JSONObject(jsonString);

                 rb = new RideObject(oneJson.toString(),functions);

            status = rb.Status;


            if(status == 1)
            {
                tvSetOff.setText("Trip Completed");
            }
            String seat = "";

            if(rb.Seat1 == 2)
            {
                if(seat.length()>0)
                    seat+=",";

                seat+="Seat 1";

            }



            if(rb.Seat2 == 2)
            {
                if(seat.length()>0)
                    seat+=",";

                seat+="Seat 2";

            }


            if(rb.Seat3 == 2)
            {
                if(seat.length()>0)
                    seat+=",";

                seat+="Seat 3";

            }

            if(rb.Seat4 == 2)
            {
                if(seat.length()>0)
                    seat+=",";

                seat+="Seat 4";

            }


            if(!seat.contentEquals(""))
            tvReserved.setText(seat);


                tvTime.setText(rb.SetOffTime);

                if(rb.RideStart != null)
                {
                    tvStartingPost.setText(functions.getJsonString(rb.RideStart, StaticVariables.NAME));
                }





                if(rb.RideEnd != null)
                {
                    tvEndingPost.setText(functions.getJsonString(rb.RideEnd, StaticVariables.NAME));
                }



                if(rb.Stop1 != null)
                {
                    tvMid1Post.setText(functions.getJsonString(rb.Stop1, StaticVariables.NAME));
                }else{
                    tvMid1Post.setVisibility(View.GONE);
                }


                if(rb.Stop2 != null)
                {
                    tvMid2Post.setText(functions.getJsonString(rb.Stop2, StaticVariables.NAME));
                }else{
                    tvMid2Post.setVisibility(View.GONE);
                }


                JSONArray passengers = rb.Passengers;




                AQuery aq = new AQuery(this);
                ImageOptions op=new ImageOptions();
                op.fileCache = true;
                op.memCache=true;
                op.targetWidth = 0;
                op.fallback = R.drawable.ovaldashes;

                for(int i=0; i<passengers.length(); i++)
                {
                    final JSONObject oneC =  passengers.getJSONObject(i);
                    ProfileObject po = new ProfileObject(oneC.toString(),functions);
                    String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+po.ProfileUrl+".jpg";
                    aq.id(profiles[i]).image(url, op);

                    profiles[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //oneC
                            UserFunctions functions = new UserFunctions(PostedRideDetails.this);
                            functions.showUserProfileDialog();
                        }
                    });

                }



        }catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }


}
