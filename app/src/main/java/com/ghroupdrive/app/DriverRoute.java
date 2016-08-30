package com.ghroupdrive.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

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
        ivLine = (ImageView) theLayout.findViewById(R.id.ivLine);
        ivBottom = (ImageView) theLayout.findViewById(R.id.ivBottom);
        ivBottomPoint = (ImageView) theLayout.findViewById(R.id.ivBottomPoint);
        //List<String> dogList = Arrays.asList(dogArr);
        List<String> dogList = StaticVariables.locNames;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layoutItemId, dogList);
        autocompleteView.setAdapter(adapter);
        autocompleteView1.setAdapter(adapter);
        functions = new UserFunctions(getActivity());




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



                //bindStrting(selected);





            }
        });



        return  theLayout;
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
                                    JSONObject datar = functions.getJsonObject(json,StaticVariables.DATA);


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

}
