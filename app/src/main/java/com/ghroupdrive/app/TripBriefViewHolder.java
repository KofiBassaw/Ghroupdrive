package com.ghroupdrive.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;


/**
 * Created by pk on 4/22/15.
 */
public class TripBriefViewHolder extends RecyclerView.ViewHolder {
    private final TextView tvName;
    private final TextView tvPhone;
    private final TextView tcDesc;


    public TripBriefViewHolder(final View parent, TextView tvName,TextView tvPhone,TextView tcDesc) {
        super(parent);
       this.tvName = tvName;
       this.tvPhone = tvPhone;
       this.tcDesc = tcDesc;

    }

    public static TripBriefViewHolder newInstance(View parent) {
        TextView tvName = (TextView) parent.findViewById(R.id.tvName);
        TextView tvPhone = (TextView) parent.findViewById(R.id.tvPhone);
        TextView tcDesc = (TextView) parent.findViewById(R.id.tcDesc);
        return new TripBriefViewHolder(parent,tvName,tvPhone,tcDesc);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {


        String sourceString = "Please wait at <b>"+Details.locName+"</b> for pickup\\nYou\\'ll be notified when the Driver set off at <b>"+Details.time+"</b>";


        tcDesc.setText(Html.fromHtml(sourceString));
        tvName.setText(Details.name);
        tvPhone.setText(Details.phone);


    }

}
