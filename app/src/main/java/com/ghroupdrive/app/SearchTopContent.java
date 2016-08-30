package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONObject;


/**
 * Created by pk on 4/22/15.
 */
public class SearchTopContent extends RecyclerView.ViewHolder {
     private final LinearLayout llRow;
     private final ImageView ivProfile;
     private final TextView tvTime;
     private final TextView tvPrice;
     private final TextView tvStarting;
     private final TextView tvMid1;
     private final TextView tvMid2;
     private final TextView tvAv;
     private final TextView tvDriverName;
     private final TextView tvCarType;




    public SearchTopContent(final View parent, LinearLayout llRow, ImageView ivProfile, TextView tvTime,TextView tvPrice,TextView tvStarting,
                            TextView tvMid1, TextView tvMid2,TextView tvAv,TextView tvDriverName,TextView tvCarType) {
        super(parent);
        this.llRow = llRow;
        this.ivProfile = ivProfile;
        this.tvTime = tvTime;
        this.tvPrice = tvPrice;
        this.tvStarting = tvStarting;
        this.tvMid1 = tvMid1;
        this.tvMid2 = tvMid2;
        this.tvAv = tvAv;
        this.tvDriverName = tvDriverName;
        this.tvCarType = tvCarType;

    }

    public static SearchTopContent newInstance(View parent) {
        LinearLayout llRow = (LinearLayout) parent.findViewById(R.id.llRow);
        ImageView ivProfile = (ImageView) parent.findViewById(R.id.ivProfile);
        TextView tvTime = (TextView) parent.findViewById(R.id.tvTime);
        TextView tvPrice = (TextView) parent.findViewById(R.id.tvPrice);
        TextView tvStarting = (TextView) parent.findViewById(R.id.tvStarting);
        TextView tvMid1 = (TextView) parent.findViewById(R.id.tvMid1);
        TextView tvMid2 = (TextView) parent.findViewById(R.id.tvMid2);
        TextView tvAv = (TextView) parent.findViewById(R.id.tvAv);
        TextView tvDriverName = (TextView) parent.findViewById(R.id.tvDriverName);
        TextView tvCarType = (TextView) parent.findViewById(R.id.tvCarType);
        return new SearchTopContent(parent,llRow,ivProfile,tvTime,tvPrice,tvStarting,tvMid1,tvMid2,tvAv,tvDriverName,tvCarType);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        tvTime.setText(Details.startingTime);
        tvStarting.setText(Details.startingPoint);
        tvDriverName.setText(Details.driversName);
        tvCarType.setText(Details.carType);
        tvPrice.setText("GHS " + Details.price);
        if(Details.availableSeat.contentEquals("1"))
        {
            tvAv.setText(Details.availableSeat+" seat Available");
        }else
        {
            tvAv.setText(Details.availableSeat+" seats Available");
        }


        if(Details.point1 != null)
        {
            if(!Details.point1.contentEquals(""))
            {
                tvMid1.setVisibility(View.VISIBLE);
                tvMid1.setText(Details.point1);
            }
        }


        if(Details.point2 != null)
        {
            if(!Details.point2.contentEquals(""))
            {
                tvMid2.setVisibility(View.VISIBLE);
                tvMid2.setText(Details.point2);
            }
        }







        llRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent(new Intent(context, Trips.class));
                it.putExtra(StaticVariables.JSONSTRING,Details.jsonString);
                context.startActivity(it);
            }
        });


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              try
              {
                  UserFunctions functions = new UserFunctions(context);

                  String jsonString = Details.getJsonString();
                  JSONObject json = new JSONObject(jsonString);
                  JSONObject driver = functions.getJsonObject(json, StaticVariables.DRIVER);
                  functions.showUserProfileDialog();
              }catch (Exception ex)
              {
                  ex.printStackTrace();
              }
            }
        });


        String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+Details.driversProfileUrl+".jpg";
        AQuery aq = new AQuery(context);
        ImageOptions op=new ImageOptions();
        op.fileCache = true;
        op.memCache=true;
        op.targetWidth = 0;
        op.fallback = R.drawable.uploadpicture;
        aq.id(ivProfile).image(url, op);


    }

}
