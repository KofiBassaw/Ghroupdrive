package com.ghroupdrive.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by matiyas on 8/9/16.
 */
public class RiderSearchFragement extends Fragment {

    AutoCompleteTextView autocompleteView,autoLocation;
    ImageView ivSearch;
    RelativeLayout rlLocation;
    ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    SearchAdapter recyclerAdapter;
    ProgressBar pbBar;
    Button bReload;
    TextView tvRides;
    UserFunctions functions;
    String fromLocName = "";
    String toLocName = "";
    String fromLocId;
    String toLocId;
    String idS = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.rider_search_layout, container, false);
        autocompleteView =(AutoCompleteTextView) theLayout.findViewById(R.id.autocompleteView);
        autoLocation =(AutoCompleteTextView) theLayout.findViewById(R.id.autoLocation);
        recyclerView = (RecyclerView) theLayout.findViewById(R.id.recyclerView);
        ivSearch =(ImageView) theLayout.findViewById(R.id.ivSearch);
        rlLocation =(RelativeLayout) theLayout.findViewById(R.id.rlLocation);
        int layoutItemId = android.R.layout.simple_dropdown_item_1line;
        pbBar = (ProgressBar) theLayout.findViewById(R.id.pbBar);
        bReload = (Button) theLayout.findViewById(R.id.bReload);
        tvRides = (TextView) theLayout.findViewById(R.id.tvRides);
        functions = new UserFunctions(getActivity());
       // String[] dogArr = getResources().getStringArray(R.array.dogs_list);
        List<String> dogList = StaticVariables.locNames;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), layoutItemId, dogList);
        autocompleteView.setAdapter(adapter);
        autoLocation.setAdapter(adapter);
        initRecyclerView();


        autocompleteView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().contentEquals("")) {
                    //hide location
                    //show recycler
                    //show search icon
                    ivSearch.setVisibility(View.VISIBLE);
                    //recyclerView.setVisibility(View.VISIBLE);
                    rlLocation.setVisibility(View.GONE);
                    fromLocName = "";
                    toLocName = "";
                    toLocId = "";
                    fromLocId = "";
                    autoLocation.setText("");
                    pbBar.setVisibility(View.VISIBLE);
                    getLocation("");

                } else {
                    //show location
                    //hide recycler
                    //hide search icon

                    ivSearch.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.GONE);
                    rlLocation.setVisibility(View.VISIBLE);
                }
            }
        });




        autoLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.toString().trim().contentEquals("")) {


                    toLocName = "";
                    toLocId = "";

                }
            }
        });





        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                int pos = StaticVariables.locNames.indexOf(selected);

                fromLocName = selected;
                fromLocId = StaticVariables.locIDs.get(pos);

                if(!toLocId.contentEquals(""))
                {

                    pbBar.setVisibility(View.VISIBLE);
                     String ids = "?"+StaticVariables.ACCESSCODE+"="+functions.getPref(StaticVariables.ACCESSCODE,"")+"&"+StaticVariables.FROMID+"="+fromLocId+"&"+StaticVariables.TOID+"="+toLocId;
                    getLocation(ids);
                }


            }
        });


        autoLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                int pos = StaticVariables.locNames.indexOf(selected);

                 toLocName = selected;
                toLocId = StaticVariables.locIDs.get(pos);

                if(!fromLocId.contentEquals(""))
                {
                    pbBar.setVisibility(View.VISIBLE);
                    pbBar.setVisibility(View.VISIBLE);
                    String ids = "?"+StaticVariables.ACCESSCODE+"="+functions.getPref(StaticVariables.ACCESSCODE,"")+"&"+StaticVariables.FROMID+"="+fromLocId+"&"+StaticVariables.TOID+"="+toLocId;
                    getLocation(ids);
                }


            }
        });






        pbBar.setVisibility(View.VISIBLE);

        getLocation("");


        bReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bReload.setVisibility(View.GONE);
                pbBar.setVisibility(View.VISIBLE);
                getLocation(idS);
            }
        });
  return  theLayout;
    }







    private void getLocation(String idsString)
    {
        idS  = idsString;
        String url = "Rides?"+StaticVariables.ACCESSCODE+"="+functions.getPref(StaticVariables.ACCESSCODE,"")+"&";
        if(!idsString.contentEquals(""))
        {
            url="SearchRides"+idsString;
        }




        ConnectionDetector cd=new ConnectionDetector(getActivity());
        if(cd.isConnectingToInternet()){
            //System.out.println(functions.getCokies());
            Ion.with(this)
                    .load("GET", StaticVariables.BASEURL + url)
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



                                            if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.HONEYCOMB)
                                            {
                                                new BindAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,data.toString());
                                            }else {
                                                new BindAsync().execute(data.toString());
                                            }


                                        }else
                                        {
                                            errorOccure();
                                        }

                                    }else {
                                        errorOccure();
                                    }


                                }else
                                {
                                    errorOccure();
                                }

                            } catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });


        }



    }





    private void errorOccure()
    {
        pbBar.setVisibility(View.GONE);
        bReload.setVisibility(View.VISIBLE);
    }








    private  void  bindData(JSONArray json)
    {




     details = functions.formatRide(json);


        /*
        for(int i=0; i<5; i++)
        {
            Details = new GettersAndSetters();
            Details.setType(StaticVariables.CONTENT);
            details.add(Details);
        }

        */


    }

    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));











        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );
    }








    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("type");

            if (type.contentEquals("gotit"))
            {
                functions.setPref(StaticVariables.HASGOTITSEARCH,true);
                details.remove(0);
                recyclerAdapter.notifyItemRemoved(0);
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










    class BindAsync extends AsyncTask<String, String, String> {

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

            try{

                bindData(new JSONArray(args[0]));

            }catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }




            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {

            pbBar.setVisibility(View.GONE);

            if(recyclerAdapter == null)
            {
                recyclerAdapter = new SearchAdapter(details, getActivity());
                recyclerView.setAdapter(recyclerAdapter);
            }else
            {
                recyclerAdapter.notifyDataSetChanged();
            }

            recyclerView.setVisibility(View.VISIBLE);
            if(details.size() <=0)
            {
                tvRides.setVisibility(View.VISIBLE);
            }

        }





    }



}
