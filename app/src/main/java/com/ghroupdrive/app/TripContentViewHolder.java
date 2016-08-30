package com.ghroupdrive.app;

import android.content.Context;
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
public class TripContentViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvName;
    private final TextView tvSeatNo;
    private final ImageView ivProfile;


    public TripContentViewHolder(final View parent, TextView tvName, TextView tvSeatNo, ImageView ivProfile) {
        super(parent);
       this.tvName = tvName;
       this.tvSeatNo = tvSeatNo;
       this.ivProfile = ivProfile;

    }

    public static TripContentViewHolder newInstance(View parent) {
        TextView tvName = (TextView) parent.findViewById(R.id.tvName);
        TextView tvSeatNo = (TextView) parent.findViewById(R.id.tvSeatNo);
        ImageView ivProfile = (ImageView) parent.findViewById(R.id.ivProfile);
        return new TripContentViewHolder(parent,tvName,tvSeatNo,ivProfile);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {
        tvName.setText(Details.name);
        tvSeatNo.setText(Details.seatNo);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    UserFunctions functions = new UserFunctions(context);
                    JSONObject profile = new JSONObject(Details.jsonString);
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
