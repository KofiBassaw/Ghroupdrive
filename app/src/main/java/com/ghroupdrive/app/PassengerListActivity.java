package com.ghroupdrive.app;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ptrack on 9/3/16.
 */
public class PassengerListActivity extends AppCompatActivity {

    UserFunctions functions;
    ProgressDialog pDIalogi;
    RelativeLayout rlArrived;
    RippleView rpArrived;
    RideObject rb;
    RecyclerView recyclerView;
    ArrayList<GettersAndSetters> details;
    PassengerAdapter adapter;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_list);
        functions = new UserFunctions(this);
        rlArrived = (RelativeLayout) findViewById(R.id.rlArrived);
        rpArrived = (RippleView) findViewById(R.id.rpArrived);
        rb = new RideObject(getIntent().getStringExtra(StaticVariables.JSONSTRING),functions);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        initRecyclerView();


        rlArrived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpArrived.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        JsonObject json = new JsonObject();
                        json.addProperty(StaticVariables.ACCESSCODE,functions.getPref(StaticVariables.ACCESSCODE,""));
                        json.addProperty(StaticVariables.RIDEID,rb.RideId);
                        json.addProperty(StaticVariables.STATUS,(rb.Status+1));

                        setOff(json,"RideUpdate");
                    }
                });
            }
        });

        bindData();
    }









    private void initRecyclerView() {



        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecorationSam(spacingInPixels));

        int paddingTop = Utils.getToolbarHeight(this) + Utils.getTabsHeight(this);
        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new GridLayoutManager(this,2));









        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );
    }








    private void bindData()
    {
        details = new ArrayList<>();
        try
        {
            JSONArray passengers = rb.Passengers;

            GettersAndSetters Details;
            for(int i=0; i< passengers.length(); i++)
            {
                Details = new GettersAndSetters();
                JSONObject onPass = passengers.getJSONObject(i);
                ProfileObject oneProf = new ProfileObject(onPass.toString(),functions);
                Details.setName(oneProf.Fullname);
                Details.setPhone(oneProf.MobileNo);
                JSONObject locJson = oneProf.Location;
                LocationObj locObj = new LocationObj(locJson.toString(),functions);
                Details.setLocName(locObj.Name);
                Details.setJsonString(onPass.toString());
                details.add(Details);

            }


            for(int i=(passengers.length()-1); i<4; i++)
            {
                Details = new GettersAndSetters();
                Details.setJsonString("");
                details.add(Details);

            }
            adapter = new PassengerAdapter(details, this);
            recyclerView.setAdapter(adapter);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }









    private void setOff(JsonObject data, String function)
    {

        pDIalogi = new ProgressDialog(this);
        pDIalogi.setCancelable(true);
        pDIalogi.setMessage("Updating ride information ...");
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

}
