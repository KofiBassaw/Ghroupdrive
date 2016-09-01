package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by matiyas on 8/17/16.
 */
public class DriverRouteSelection extends AppCompatActivity {

   String first = "";
   String firstID = "";
    String last = "";
    String lastID = "";


    String start = "";
    String end = "";
    private static final int SELECTION = 1;


    ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    DriverRouteSelectionAdapter recyclerAdapter;
    TextView tvStarting,tvDestination,tvLoc1,tvLoc2;
    ImageView point1,point2;

    int lastPos =-1;
    int firstPos = -1;


    RelativeLayout rlLoad;
    RippleView rpLoad;
    String jsonString="";
    String startID="",endID="";
    UserFunctions functions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_route_selection);
        initToolbar();
        tvStarting = (TextView) findViewById(R.id.tvStarting);
        tvDestination = (TextView) findViewById(R.id.tvDestination);
        tvLoc1 = (TextView) findViewById(R.id.tvLoc1);
        tvLoc2 = (TextView) findViewById(R.id.tvLoc2);
        point1 = (ImageView) findViewById(R.id.point1);
        point2 = (ImageView) findViewById(R.id.point2);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        rlLoad = (RelativeLayout)findViewById(R.id.rlLoad);
        rpLoad = (RippleView)findViewById(R.id.rpLoad);
        functions = new UserFunctions(this);


        initRecyclerView();



        start = getIntent().getStringExtra("start");
        startID = getIntent().getStringExtra("startID");
        end = getIntent().getStringExtra("end");
        endID = getIntent().getStringExtra("endID");
        jsonString = getIntent().getStringExtra(StaticVariables.JSONSTRING);

        tvStarting.setText(start);
        tvDestination.setText(end);

        bindData();


        tvLoc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData(last);
                last = "";
                hideLast(false);

            }
        });

        tvLoc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (last.contentEquals(""))
                {

                    addData(first);
                    first = "";
                    firstID = "";
                    hideFirst(false);
                }else {

                    first = last;
                    firstID = lastID;
                    addData(first);
                    tvLoc1.setText(first);
                    last = "";
                    lastID = "";
                    hideLast(false);
                }


            }
        });


        rlLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpLoad.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent it = new Intent(DriverRouteSelection.this, DriverTrips.class);
                        it.putExtra("start",start);
                        it.putExtra("startID",startID);
                        it.putExtra("end",end);
                        it.putExtra("endID",endID);
                        it.putExtra("first",first);
                        it.putExtra("firstID",firstID);
                        it.putExtra("last",last);
                        it.putExtra("lastID",lastID);
                        startActivityForResult(it,SELECTION);
                    }
                });
            }
        });
    }



    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Select drive-through points");
        // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }




    private  void  bindData()
    {




        try
        {
            details = new ArrayList<>();
            GettersAndSetters Details;

            Details = new GettersAndSetters();
            Details.setType(StaticVariables.TOPTYPE);
            Details.setTitle("TAP TO SELECT OTHER DRIVE THROUGH POINTS");
            details.add(Details);

            JSONArray locs = new JSONArray(jsonString);

            for(int i=0; i<locs.length(); i++)
            {
                JSONObject c = locs.getJSONObject(i);
                LocationObj loc = new LocationObj(c.toString(),functions);
                Details = new GettersAndSetters();
                Details.setType(StaticVariables.CONTENT);
                Details.setTitle(loc.Name);
                Details.setServerID(loc.Id);
                details.add(Details);

            }
            recyclerAdapter = new DriverRouteSelectionAdapter(details, this);
            recyclerView.setAdapter(recyclerAdapter);

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }


    }




    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacingsmall);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setItemAnimator(new SlideInUpAnimator(new OvershootInterpolator(1f)));











        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        String text = details.get(position).title;
                        String id = details.get(position).serverID;
                        if(first.contentEquals(""))
                        {

                            firstPos = position;
                            changeData(position);
                            first = text;
                            firstID = id;
                            tvLoc1.setText(first);
                            hideFirst(true);
                            hideLast(false);

                        }else if (last.contentEquals(""))
                        {
                            lastPos = position;
                            changeData(position);
                            last = text;
                            lastID = id;
                            tvLoc2.setText(last);
                            hideLast(true);
                        }

                    }
                })
        );
    }


    private void  changeData(int position)
    {

        details.remove(position);
        recyclerAdapter.notifyItemRemoved(position);
    }


    private void  addData(String text)
    {


       GettersAndSetters Details = new GettersAndSetters();
        Details.setTitle(text);
        Details.setType(StaticVariables.CONTENT);

        details.add(getminimum(),Details);
        recyclerAdapter.notifyItemInserted(getminimum());
    }



    private int getminimum()
    {
        if(lastPos == -1)
            return  firstPos;

        if(lastPos>firstPos)
            return  firstPos;
            else
            return lastPos;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

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
    public void onBackPressed() {

        if(!first.contentEquals("")|| !last.contentEquals(""))
        {
            Bundle bb = new Bundle();
            bb.putString("last",last);
            bb.putString("first",first);
            Intent it = new Intent();
            it.putExtras(bb);
            setResult(RESULT_OK,it);
        }

        super.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == SELECTION)
        {
            onBackPressed();
        }
    }
}
