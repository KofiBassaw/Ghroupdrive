package com.ghroupdrive.app;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/15/16.
 */
public class DriverJoinActivity extends AppCompatActivity implements View.OnClickListener {


    RelativeLayout rlJoin;
    RippleView rpJoin;
    TextView tvTotalRide;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driver_join_screen);
        rlJoin = (RelativeLayout) findViewById(R.id.rlLoad);
        rpJoin = (RippleView) findViewById(R.id.rpLoad);
        tvTotalRide = (TextView) findViewById(R.id.tvTotalRide);


        tvTotalRide.setText("GHS "+getIntent().getExtras().getDouble(StaticVariables.BASEPRICE));
        rlJoin.setOnClickListener(this);



    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rlLoad:
                rpJoin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                       setResult(RESULT_OK);
                        onBackPressed();
                    }
                });
                break;
        }
    }
}
