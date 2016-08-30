package com.ghroupdrive.app;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/15/16.
 */
public class RiderJoinActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    RiderJoinAdapter recyclerAdapter;
    AppBarLayout appbarLayout;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    ImageView ivProfile;
    String jsonString;
    RideObject rb;
    UserFunctions functions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rider_join_screen);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);

        collapsingToolbarLayout.setTitle("");
        initToolbar();
        initRecyclerView();

        appbarLayout = (AppBarLayout) findViewById(R.id.main_appbar);

        mMaxScrollSize = appbarLayout.getTotalScrollRange();
        jsonString = getIntent().getStringExtra(StaticVariables.JSONSTRING);
        functions = new UserFunctions(this);

        rb = new RideObject(jsonString,functions);

        ProfileObject po = new ProfileObject(rb.Driver.toString(),functions);
        String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+po.ProfileUrl+".jpg";
        AQuery aq = new AQuery(this);
        ImageOptions op=new ImageOptions();
        op.fileCache = true;
        op.memCache=true;
        op.targetWidth = 0;
        op.fallback = R.drawable.uploadpicture;
        aq.id(ivProfile).image(url, op);




        bindData();


    }



    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_OK);
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacingsmall);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(this));













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
    protected void onPause() {
        super.onPause();
        appbarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onResume() {
        appbarLayout.addOnOffsetChangedListener(this);

        super.onResume();
    }





    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            ivProfile.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            ivProfile.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }


    }


        private  void  bindData()
    {
       try
       {
           details = new ArrayList<>();
           GettersAndSetters Details;

           ProfileObject po = new ProfileObject(rb.Driver.toString(),functions);
           Details = new GettersAndSetters();
           Details.setType(StaticVariables.BRIEF);
           Details.setTime(rb.SetOffTime);
           Details.setName(po.Fullname);
           Details.setPhone(po.MobileNo);
           Details.setLocName(getIntent().getStringExtra(StaticVariables.SEAT));
           details.add(Details);


           Details = new GettersAndSetters();
           Details.setType(StaticVariables.TOPTYPE);
           details.add(Details);




           JSONArray passengers = rb.Passengers;
           for(int i=0; i<passengers.length(); i++)
           {
               JSONObject c = passengers.getJSONObject(i);
                po = new ProfileObject(c.toString(),functions);
               Details = new GettersAndSetters();
               Details.setName(po.Fullname);
               Details.setSeatNo("Seat "+po.Seat);
               Details.setType(StaticVariables.CONTENT);
               Details.setJsonString(c.toString());
               Details.setDriversProfileUrl(po.ProfileUrl);
               details.add(Details);
           }

           recyclerAdapter = new RiderJoinAdapter(details, this);
           recyclerView.setAdapter(recyclerAdapter);
       }catch (Exception ex)
       {
           ex.printStackTrace();
       }
    }
}
