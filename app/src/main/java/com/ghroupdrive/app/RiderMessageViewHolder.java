package com.ghroupdrive.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by pk on 4/22/15.
 */
public class RiderMessageViewHolder extends RecyclerView.ViewHolder {
    private final ImageView ivProfile;
    private final ImageView tvPoint;
    private final TextView tvDate;
    private final TextView tvName;
    private final TextView tvtitle;


    public RiderMessageViewHolder(final View parent, ImageView ivProfile, TextView tvDate, ImageView tvPoint, TextView tvName, TextView tvtitle) {
        super(parent);
       this.ivProfile = ivProfile;
       this.tvDate = tvDate;
       this.tvPoint = tvPoint;
       this.tvName = tvName;
       this.tvtitle = tvtitle;

    }

    public static RiderMessageViewHolder newInstance(View parent) {
        ImageView ivProfile = (ImageView) parent.findViewById(R.id.ivProfile);
        ImageView tvPoint = (ImageView) parent.findViewById(R.id.tvPoint);
        TextView tvDate = (TextView) parent.findViewById(R.id.tvDate);
        TextView tvName = (TextView) parent.findViewById(R.id.tvName);
        TextView tvtitle = (TextView) parent.findViewById(R.id.tvtitle);
        return new RiderMessageViewHolder(parent,ivProfile,tvDate,tvPoint,tvName,tvtitle);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        tvDate.setText(Details.date);
        tvName.setText(Details.name);
        tvtitle.setText(Details.title);
        if(Details.read)
        {
            tvDate.setTextColor(Color.parseColor("#3d3e3f"));
            tvPoint.setVisibility(View.GONE);
        }else
        {
            tvDate.setTextColor(Color.parseColor("#2d9cf5"));
            tvPoint.setVisibility(View.VISIBLE);
        }
if(position == 0)
{
    //first item
    ivProfile.setImageResource(R.drawable.defaultprofile);
}else
{

}

    }

}
