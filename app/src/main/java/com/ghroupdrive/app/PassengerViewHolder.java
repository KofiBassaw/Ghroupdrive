package com.ghroupdrive.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONObject;


/**
 * Created by pk on 4/22/15.
 */
public class PassengerViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivProfile;
    private final TextView tvName;
    private final TextView tvPickup;
    private final TextView tvLocation;
    private final TextView tvNumber;


    public PassengerViewHolder(final View parent, ImageView ivProfile, TextView tvName, TextView tvPickup, TextView tvLocation, TextView tvNumber) {
        super(parent);
       this.ivProfile = ivProfile;
       this.tvName = tvName;
       this.tvPickup = tvPickup;
       this.tvLocation = tvLocation;
       this.tvNumber = tvNumber;

    }

    public static PassengerViewHolder newInstance(View parent) {
        ImageView ivProfile = (ImageView) parent.findViewById(R.id.ivProfile);
        TextView tvName = (TextView) parent.findViewById(R.id.tvName);
        TextView tvPickup = (TextView) parent.findViewById(R.id.tvPickup);
        TextView tvLocation = (TextView) parent.findViewById(R.id.tvLocation);
        TextView tvNumber = (TextView) parent.findViewById(R.id.tvNumber);
        return new PassengerViewHolder(parent,ivProfile,tvName,tvPickup,tvLocation,tvNumber);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {


        if(Details.jsonString.contentEquals(""))
        {
            tvName.setText("Seat Unbooked");

        }else
        {
            tvPickup.setText("Pick Up Point:");
            tvNumber.setText(Details.phone);
            tvName.setText(Details.name);
            tvLocation.setText(Details.locName);


        }


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
        op.fallback = R.drawable.ovaldashes;
        aq.id(ivProfile).image(url, op);


    }

}
