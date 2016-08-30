package com.ghroupdrive.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by pk on 4/22/15.
 */
public class TripTopViewHolder extends RecyclerView.ViewHolder {
   // private final TextView tvCategory;


    public TripTopViewHolder(final View parent) {
        super(parent);
       // this.tvCategory = tvCategory;

    }

    public static TripTopViewHolder newInstance(View parent) {
        //TextView tvCategory = (TextView) parent.findViewById(R.id.tvCategory);
        return new TripTopViewHolder(parent);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {



    }

}
