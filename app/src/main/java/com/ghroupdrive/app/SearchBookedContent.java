package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONObject;


/**
 * Created by pk on 4/22/15.
 */
public class SearchBookedContent extends RecyclerView.ViewHolder {
     private final LinearLayout llRow;
     private final RelativeLayout rlViewAll;
     private final RippleView rpViewAll;
     private final ImageView ivProfile;
     private final TextView tvEnding;
    // private final TextView tvPrice;
     private final TextView tvStarting;
     private final TextView tvMid1;
     private final TextView tvMid2;
     //private final TextView tvAv;
     private final TextView tvDriverName;
     private final TextView tvCarType;
     private final TextView tvSeatNo;
     private final TextView tvTitle;
     private final TextView tvTitle2;




    public SearchBookedContent(final View parent, LinearLayout llRow, ImageView ivProfile, TextView tvStarting,
                               TextView tvMid1, TextView tvMid2, TextView tvDriverName, TextView tvCarType,TextView tvEnding,
                               TextView tvSeatNo, RelativeLayout rlViewAll,RippleView rpViewAll, TextView tvTitle, TextView tvTitle2) {
        super(parent);
        this.llRow = llRow;
        this.ivProfile = ivProfile;
        //this.tvTime = tvTime;
        //this.tvPrice = tvPrice;
        this.tvStarting = tvStarting;
        this.tvMid1 = tvMid1;
        this.tvMid2 = tvMid2;
       // this.tvAv = tvAv;
        this.tvDriverName = tvDriverName;
        this.tvCarType = tvCarType;
        this.tvEnding = tvEnding;
        this.tvSeatNo = tvSeatNo;
        this.rlViewAll = rlViewAll;
        this.rpViewAll = rpViewAll;
        this.tvTitle = tvTitle;
        this.tvTitle2 = tvTitle2;

    }

    public static SearchBookedContent newInstance(View parent) {
        LinearLayout llRow = (LinearLayout) parent.findViewById(R.id.llRow);
        ImageView ivProfile = (ImageView) parent.findViewById(R.id.ivProfile);
        //TextView tvTime = (TextView) parent.findViewById(R.id.tvTime);
        //TextView tvPrice = (TextView) parent.findViewById(R.id.tvPrice);
        TextView tvStarting = (TextView) parent.findViewById(R.id.tvStarting);
        TextView tvMid1 = (TextView) parent.findViewById(R.id.tvMid1);
        TextView tvMid2 = (TextView) parent.findViewById(R.id.tvMid2);
       // TextView tvAv = (TextView) parent.findViewById(R.id.tvAv);
        TextView tvDriverName = (TextView) parent.findViewById(R.id.tvDriverName);
        TextView tvCarType = (TextView) parent.findViewById(R.id.tvCarType);
        TextView tvEnding = (TextView) parent.findViewById(R.id.tvEnding);
        TextView tvSeatNo = (TextView) parent.findViewById(R.id.tvSeatNo);
        TextView tvTitle = (TextView) parent.findViewById(R.id.tvTitle);
        TextView tvTitle2 = (TextView) parent.findViewById(R.id.tvTitle2);
        RelativeLayout rlViewAll = (RelativeLayout) parent.findViewById(R.id.rlViewAll);
        RippleView rpViewAll = (RippleView) parent.findViewById(R.id.rpViewAll);
        return new SearchBookedContent(parent,llRow,ivProfile,tvStarting,tvMid1,tvMid2,tvDriverName,tvCarType,tvEnding,tvSeatNo,rlViewAll,rpViewAll,tvTitle,tvTitle2);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        //tvTime.setText(Details.startingTime);
        tvStarting.setText(Details.startingPoint);
        tvDriverName.setText(Details.driversName);
        tvCarType.setText(Details.carType);
        tvEnding.setText(Details.endingPoint);
        tvSeatNo.setText(Details.seatNo);

        if(Details.status == 1)
        {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle2.setVisibility(View.VISIBLE);
        }else
        {
            tvTitle.setVisibility(View.INVISIBLE);
            tvTitle2.setVisibility(View.INVISIBLE);
        }
       // tvPrice.setText("GHS " + Details.price);
        /*
        if(Details.availableSeat.contentEquals("1"))
        {
            tvAv.setText(Details.availableSeat+" seat Available");
        }else
        {
            tvAv.setText(Details.availableSeat+" seats Available");
        }

        */

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

              Intent it = new Intent(StaticVariables.SEARCHMESSAGE);
                it.putExtra(StaticVariables.JSONSTRING,Details.jsonString);
                it.putExtra("type","startMyRidesDetails");
                context.sendBroadcast(it);
            }
        });


        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              try
              {
                  UserFunctions functions = new UserFunctions(context);

                  RideObject obj = new RideObject(Details.jsonString,functions);
                  JSONObject driver =obj.Driver;
                  functions.showUserProfileDialog();
              }catch (Exception ex)
              {
                  ex.printStackTrace();
              }
            }
        });


        if(rlViewAll!= null)
        {


            rlViewAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rpViewAll.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                        @Override
                        public void onComplete(RippleView rippleView) {
                            //start all the booked ride activities
                         Intent it = new Intent(StaticVariables.SEARCHMESSAGE);
                            it.putExtra("type","rideViewAll");
                            context.sendBroadcast(it);
                        }
                    });
                }
            });

        }



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
