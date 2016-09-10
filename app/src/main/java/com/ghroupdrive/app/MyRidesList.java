package com.ghroupdrive.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by ptrack on 9/2/16.
 */
public class MyRidesList extends AppCompatActivity {


    private static final int MYRIDESDETAILS = 1;
    boolean setResultOk = false;
    UserFunctions functions;
    ArrayList<GettersAndSetters> details;
    ProgressBar pbBar;
    Button bReload;
    TextView tvRides;
    MyRidesAdapter recyclerAdapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initToolbar();
        functions = new UserFunctions(this);
        pbBar = (ProgressBar) findViewById(R.id.pbBar);
        bReload = (Button) findViewById(R.id.bReload);
        tvRides = (TextView) findViewById(R.id.tvRides);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();







        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
        {
            new BindDataAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ;
        }else {
            new BindDataAsync().execute();
        }

        bReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bReload.setVisibility(View.GONE);
                pbBar.setVisibility(View.VISIBLE);
                getMyRides(functions.getPref(StaticVariables.ACCESSCODE,""),"2");
            }
        });

    }





    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));











        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == MYRIDESDETAILS && resultCode == RESULT_OK)
        {
            setResultOk = true;
            pbBar.setVisibility(View.VISIBLE);
            getMyRides(functions.getPref(StaticVariables.ACCESSCODE,""),"2");
            details = new ArrayList<>();
            recyclerAdapter = new MyRidesAdapter(details, MyRidesList.this);
            recyclerView.setAdapter(recyclerAdapter);
        }
    }




    @Override
    public void onBackPressed() {
        if(setResultOk)
            setResult(RESULT_OK);
        super.onBackPressed();
    }

    private void initToolbar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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


    @Override
    protected void onResume() {
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                StaticVariables.SEARCHMESSAGE));
        super.onResume();
    }




    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("type");

            if (type.contentEquals("startMyRidesDetails"))
            {
                Intent it = new Intent(MyRidesList.this, RiderJoinActivity.class);
                it.putExtra(StaticVariables.JSONSTRING,intent.getStringExtra(StaticVariables.JSONSTRING));
                it.putExtra("from","myride");
                startActivityForResult(it,MYRIDESDETAILS);

            }


        }
    };




    private void getMyRides(String accessCode, final String type)
    {

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
                                            StaticVariables.MYLOC = data.toString();


                                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
                                            {
                                                new BindDataAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR) ;
                                            }else {
                                                new BindDataAsync().execute();
                                            }
                                        }else
                                        {
                                            showRelod();
                                        }

                                    }else
                                    {
                                        showRelod();

                                    }


                                }else
                                {
                                    showRelod();
                                }

                            } catch (Exception ex) {
                                showRelod();
                                ex.printStackTrace();
                            }
                        }
                    });


        }else
        {
            showRelod();
        }



    }




    class BindDataAsync extends AsyncTask<String, String, String> {

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


            bindData();

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            pbBar.setVisibility(View.GONE);
            recyclerAdapter = new MyRidesAdapter(details, MyRidesList.this);
            recyclerView.setAdapter(recyclerAdapter);
            if(details.size()>0)
            {
                tvRides.setVisibility(View.VISIBLE);
            }



        }





    }



    private void showRelod()
    {
        bReload.setVisibility(View.VISIBLE);
        pbBar.setVisibility(View.GONE);
    }





    private void bindData()
    {
        details = new ArrayList<>();


        try
        {

            JSONArray rides = new JSONArray(StaticVariables.MYLOC);
            GettersAndSetters Details;

            for(int i=0; i<rides.length(); i++)
            {
                JSONObject one = rides.getJSONObject(i);
                RideObject rb = new RideObject(one.toString(),functions);
                Details = new GettersAndSetters();
                Details.setType(StaticVariables.BOOKTYPE);


                if(functions.getPref(rb.RideId,0) != 0)
                {
                    Details.setSeatNo("SEAT "+functions.getPref(rb.RideId,0));
                }else
                {
                    JSONArray passengers = rb.Passengers;

                    for(int a=0; a<passengers.length(); a++)
                    {
                        JSONObject onePass = passengers.getJSONObject(a);
                        ProfileObject po = new ProfileObject(onePass.toString(),functions);

                        if(po.AccessCode == functions.getPref(StaticVariables.ACCESSCODE,""))
                        {
                            Details.setSeatNo("SEAT "+po.Seat);

                            break;
                        }else
                        {
                            continue;
                        }

                    }
                }





                ProfileObject driver = new ProfileObject(rb.Driver.toString(),functions);
                Details.setDriversProfileUrl(driver.ProfileUrl);
                Details.setName(driver.Fullname);





                if(rb.RideStart !=null)
                {
                    Details.setStartingPoint(functions.getJsonString(rb.RideStart, StaticVariables.NAME));
                }

                if(rb.RideEnd != null)
                {
                    Details.setEndingPoint(functions.getJsonString(rb.RideEnd, StaticVariables.NAME));
                }



                if(rb.Stop1 != null)
                {
                    Details.setPoint1(functions.getJsonString(rb.Stop1, StaticVariables.NAME));
                }else{
                    Details.setPoint1("");
                }


                if(rb.Stop2 != null)
                {
                    Details.setPoint2(functions.getJsonString(rb.Stop2, StaticVariables.NAME));
                }else{
                    Details.setPoint2("");
                }

                Details.setJsonString(one.toString());
                Details.setStatus(rb.Status);
                details.add(Details);


            }


        }catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


}
