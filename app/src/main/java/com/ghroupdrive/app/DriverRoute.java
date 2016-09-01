package com.ghroupdrive.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by matiyas on 8/17/16.
 */
public class DriverRoute extends Fragment {



    AutoCompleteTextView autocompleteView;
    AutoCompleteTextView autocompleteView1;
    String starting = "";
    String fromLocId = "";
    String destination ="";
    String toLocId ="";
    String first = "";
    String last = "";
    private static final int SELECTION = 1;
    TextView tvStarting,tvDestination,tvLoc1,tvLoc2;
    ImageView point1,point2,ivTop,ivTopPoint,ivLine,ivBottom,ivBottomPoint;
    UserFunctions functions;
    ProgressDialog pDIalogi;
    LinearLayout llContainer;
    RelativeLayout rlGotIt;
    CardView cvLayout;
    RippleView rpGotIt;
    LinearLayout llMyRides;
    ImageView profiles[] = new ImageView[4];
    TextView tvTime;
    TextView tvStartingPost,tvEndingPost,tvMid1Post,tvMid2Post;
    private static final int POSTEDDETAILS = 2;
    private static final int POSTEDDETAILS2 = 3;
    RelativeLayout rlViewAll;
    RippleView rpViewAll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.rider_route, container, false);


        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        String[] dogArr = getResources().getStringArray(R.array.dogs_list);
        tvStarting = (TextView) theLayout.findViewById(R.id.tvStarting);
        tvDestination = (TextView) theLayout.findViewById(R.id.tvDestination);
        autocompleteView =(AutoCompleteTextView) theLayout.findViewById(R.id.autocompleteView);
        autocompleteView1 =(AutoCompleteTextView) theLayout.findViewById(R.id.autocompleteView1);
        tvLoc1 = (TextView) theLayout.findViewById(R.id.tvLoc1);
        tvLoc2 = (TextView) theLayout.findViewById(R.id.tvLoc2);
        point1 = (ImageView) theLayout.findViewById(R.id.point1);
        point2 = (ImageView) theLayout.findViewById(R.id.point2);
        ivTop = (ImageView) theLayout.findViewById(R.id.ivTop);
        ivTopPoint = (ImageView) theLayout.findViewById(R.id.ivTopPoint);
        rlViewAll = (RelativeLayout) theLayout.findViewById(R.id.rlViewAll);
        rpViewAll = (RippleView) theLayout.findViewById(R.id.rpViewAll);
        ivLine = (ImageView) theLayout.findViewById(R.id.ivLine);
        ivBottom = (ImageView) theLayout.findViewById(R.id.ivBottom);
        ivBottomPoint = (ImageView) theLayout.findViewById(R.id.ivBottomPoint);
        llContainer = (LinearLayout) theLayout.findViewById(R.id.llContainer);
        cvLayout = (CardView) theLayout.findViewById(R.id.cvLayout);
        rlGotIt = (RelativeLayout) theLayout.findViewById(R.id.rlGotIt);
        rpGotIt = (RippleView) theLayout.findViewById(R.id.rpGotIt);
        llMyRides = (LinearLayout) theLayout.findViewById(R.id.llMyRides);
        profiles[0] = (ImageView) theLayout.findViewById(R.id.ivProfile1);
        profiles[1] = (ImageView) theLayout.findViewById(R.id.ivProfile2);
        profiles[2] = (ImageView) theLayout.findViewById(R.id.ivProfile3);
        profiles[3] = (ImageView) theLayout.findViewById(R.id.ivProfile4);

        tvTime = (TextView) theLayout.findViewById(R.id.tvTime);
        tvStartingPost = (TextView) theLayout.findViewById(R.id.tvStartingPost);
        tvEndingPost = (TextView) theLayout.findViewById(R.id.tvEndingPost);
        tvMid1Post = (TextView) theLayout.findViewById(R.id.tvMid1Post);
        tvMid2Post = (TextView) theLayout.findViewById(R.id.tvMid2Post);

        //List<String> dogList = Arrays.asList(dogArr);
        List<String> dogList = StaticVariables.locNames;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layoutItemId, dogList);
        autocompleteView.setAdapter(adapter);
        autocompleteView1.setAdapter(adapter);
        functions = new UserFunctions(getActivity());



        rlViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpViewAll.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                      Intent it = new Intent(getActivity(),PostedRideList.class);
                        startActivityForResult(it,POSTEDDETAILS2);
                    }
                });
            }
        });



        if(!functions.getPref(StaticVariables.HASGOTITROUT,false))
        {
            cvLayout.setVisibility(View.VISIBLE);
        }



        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().contentEquals("")) {
                    starting = "";
                    fromLocId = "";
                    hideFirst(false);
                    tvStarting.setText("");
                    ivTop.setVisibility(View.GONE);
                    ivTopPoint.setVisibility(View.GONE);
                    ivLine.setVisibility(View.GONE);

                    if(destination.contentEquals(""))
                    {
                        if(!functions.getPref(StaticVariables.HASGOTITROUT,false))
                        {
                            cvLayout.setVisibility(View.VISIBLE);
                        }
                        //hide the card
                        //hide the box
                        llContainer.setVisibility(View.GONE);
                    }else
                    {
                        cvLayout.setVisibility(View.GONE);
                    }
                }else
                {
                    cvLayout.setVisibility(View.GONE);
                }
            }
        });

        autocompleteView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(s.toString().contentEquals(""))
                {
                    destination =  "";
                    toLocId =  "";
                    hideLast(false);
                    tvDestination.setText("");
                    ivBottom.setVisibility(View.GONE);
                    ivBottomPoint.setVisibility(View.GONE);
                    ivLine.setVisibility(View.GONE);
                    if(starting.contentEquals(""))
                    {
                        if(!functions.getPref(StaticVariables.HASGOTITROUT,false))
                        {
                            cvLayout.setVisibility(View.VISIBLE);
                        }
                        //show the card
                        //hide the box
                        llContainer.setVisibility(View.GONE);
                    }else
                    {

                            cvLayout.setVisibility(View.GONE);

                    }
                }else
                {
                    cvLayout.setVisibility(View.GONE);
                }
            }
        });


        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);


                tvStarting.setText(selected);
                //starting is empty so bind it to the starting point
                starting = selected;
                ivTop.setVisibility(View.VISIBLE);
                ivTopPoint.setVisibility(View.VISIBLE);
                int pos = StaticVariables.locNames.indexOf(selected);
                System.out.println("----------------------------------------------- "+pos+"   selected: "+selected);
                fromLocId = StaticVariables.locIDs.get(pos);

                if (!destination.contentEquals("")) {

                   getInBetween();
                    ivLine.setVisibility(View.VISIBLE);

                }



                //show the box;
                llContainer.setVisibility(View.VISIBLE);

                //bindStrting(selected);


            }
        });


        autocompleteView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);


                tvDestination.setText(selected);
                //starting is empty so bind it to the starting point
                destination = selected;
                ivBottom.setVisibility(View.VISIBLE);
                ivBottomPoint.setVisibility(View.VISIBLE);
                int pos = StaticVariables.locNames.indexOf(selected);
                toLocId = StaticVariables.locIDs.get(pos);

                if(!starting.contentEquals(""))
                {
                    getInBetween();
                    ivLine.setVisibility(View.VISIBLE);


                }

                //show the box;



                //bindStrting(selected);





            }
        });




        if(!StaticVariables.POSTEDRIDES.contentEquals(""))
        {
            postedRide();
        }


        rlGotIt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpGotIt.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        functions.setPref(StaticVariables.HASGOTITROUT,true);
                        cvLayout.setVisibility(View.GONE);
                    }
                });
            }
        });
        return  theLayout;
    }




    private void postedRide()
    {
        try {

            for(int i=0; i<profiles.length; i++)
            {
                profiles[i].setImageResource(R.drawable.ovaldashes);
            }

            JSONArray rides = new JSONArray(StaticVariables.POSTEDRIDES);

            if(rides.length()>0)
            {


               final JSONObject oneJson = rides.getJSONObject(0);

                RideObject rb = new RideObject(oneJson.toString(),functions);

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




                AQuery aq = new AQuery(getActivity());
                ImageOptions op=new ImageOptions();
                op.fileCache = true;
                op.memCache=true;
                op.targetWidth = 0;
                op.fallback = R.drawable.ovaldashes;

                for(int i=0; i<passengers.length(); i++)
                {
                    JSONObject oneC =  passengers.getJSONObject(i);
                    ProfileObject po = new ProfileObject(oneC.toString(),functions);
                    String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+po.ProfileUrl+".jpg";
                    aq.id(profiles[i]).image(url, op);
                }



                llMyRides.setVisibility(View.VISIBLE);



                cvLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent it = new Intent(getActivity(), PostedRideDetails.class);
                        it.putExtra(StaticVariables.JSONSTRING,oneJson.toString());
                        startActivityForResult(it,POSTEDDETAILS);
                    }
                });

            }else
            {
                llMyRides.setVisibility(View.GONE);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }





    private  void bindStrting(String selected)
    {
        tvStarting.setText(selected);
        //starting is empty so bind it to the starting point
        starting = selected;
        autocompleteView.setText("");
        autocompleteView.setHint("Enter  destination");
        destination = "";
        tvDestination.setText("");
        hideFirst(false);
        hideLast(false);
                    /*
                    hide first
                    hide last
                    set Destination empty
                    */
    }



    private void hideFirst(boolean show)
    {
        if(show)
        {
            tvLoc1.setVisibility(View.VISIBLE);
            point1.setVisibility(View.VISIBLE);
        }else
        {
            tvLoc1.setVisibility(View.GONE);
            point1.setVisibility(View.GONE);
        }

    }


    private void hideLast(boolean show)
    {

        if(show)
        {
            point2.setVisibility(View.VISIBLE);
            tvLoc2.setVisibility(View.VISIBLE);

        }else
        {
            point2.setVisibility(View.GONE);
            tvLoc2.setVisibility(View.GONE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECTION && resultCode == getActivity().RESULT_OK)
        {
            Bundle bb = data.getExtras();
            last = bb.getString("last");
            first = bb.getString("first");
            starting = "";
            destination = "";
            tvDestination.setText("");
            tvStarting.setText("");



/*
            if(!last.contentEquals("") && !first.contentEquals(""))
            {
                //show both
                tvLoc1.setText(first);
                tvLoc2.setText(last);
                hideFirst(true);
                hideLast(true);
            }else if (!last.contentEquals("") || !first.contentEquals(""))
            {
                if(!last.contentEquals(""))
                {
                    tvLoc1.setText(last);
                }else
                {
                    tvLoc1.setText(first);
                }


                hideFirst(true);
                hideLast(false);

            }else {
                hideFirst(false);
                hideLast(false);
            }

            */

        }else if(resultCode ==  getActivity().RESULT_OK && requestCode == POSTEDDETAILS)
        {
            //lfhgvjkahegrdjakjerrfgjbfhsk

        }else if(resultCode ==  getActivity().RESULT_OK && requestCode == POSTEDDETAILS2)
        {
            //lfhgvjkahegrdjakjerrfgjbfhsk
            if(!StaticVariables.POSTEDRIDES.contentEquals(""))
            {
                postedRide();
            }else
            {
                llMyRides.setVisibility(View.GONE);
            }
        }
    }




private void getInBetween()
{
    pDIalogi = new ProgressDialog(getActivity());
    pDIalogi.setCancelable(true);
    pDIalogi.setMessage("Retrieving locations ...");
    pDIalogi.show();
    ConnectionDetector cd=new ConnectionDetector(getActivity());
    if(cd.isConnectingToInternet()){
        //System.out.println(functions.getCokies());

        System.out.println(StaticVariables.BASEURL + "Route?" + StaticVariables.ACCESSCODE + "=" + functions.getPref(StaticVariables.ACCESSCODE, "") + "&" + StaticVariables.FROMID+"="+fromLocId+"&"+StaticVariables.TOID+"="+toLocId);
        Ion.with(this)
                .load("GET", StaticVariables.BASEURL + "Route?" + StaticVariables.ACCESSCODE + "=" + functions.getPref(StaticVariables.ACCESSCODE, "") + "&" + StaticVariables.FROMID+"="+fromLocId+"&"+StaticVariables.TOID+"="+toLocId)
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
                                String message = functions.getJsonString(json, StaticVariables.MESSAGE);

                                if(code ==  200)
                                {
                                    JSONArray datar = functions.getJsonArray(json,StaticVariables.DATA);


                                    if(datar != null)
                                    {

                                        Intent it = new Intent(getActivity(), DriverRouteSelection.class);
                                        it.putExtra("start", starting);
                                        it.putExtra("startID", fromLocId);
                                        it.putExtra("end", destination);
                                        it.putExtra(StaticVariables.JSONSTRING, datar.toString());
                                        it.putExtra("endID", toLocId);

                                        startActivityForResult(it, SELECTION);


                                    }else
                                    {
                                        retry("Unable to retrieve locations");
                                    }

                                }else {
                                    retry(message);

                                }




                            } else {
                                pDIalogi.dismiss();
                                retry("Unable to verify invite please tray again later");

                            }

                        } catch (Exception ex) {
                            pDIalogi.dismiss();
                            ex.printStackTrace();
                        }
                    }
                });


    } else {
        pDIalogi.dismiss();
        Toast.makeText(getActivity(), "No internet Connection Please try again later", Toast.LENGTH_LONG).show();


    }


}




    private void retry(String message)
    {
        AlertDialog dd = new AlertDialog.Builder(getActivity()).create();
        dd.setMessage(message);
        dd.setButton(Dialog.BUTTON_NEGATIVE, "RETRY", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getInBetween();
            }
        });
        dd.setButton(Dialog.BUTTON_POSITIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        dd.show();
    }






    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("type");

            if (type.contentEquals("postedRide"))
            {
               postedRide();
            }


        }
    };



    @Override
    public void onResume() {
        getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(
                StaticVariables.SEARCHMESSAGE));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            Log.e("rror", "> " + e.getMessage());
        }
        super.onDestroy();
    }

}
