package com.ghroupdrive.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by pk on 4/22/15.
 */
public class DriverSelectionContentTop extends RecyclerView.ViewHolder {
    private final TextView tvTitle;


    public DriverSelectionContentTop(final View parent, TextView tvTitle) {
        super(parent);
        this.tvTitle = tvTitle;

    }

    public static DriverSelectionContentTop newInstance(View parent) {
        TextView tvTitle = (TextView) parent.findViewById(R.id.tvTitle);
        return new DriverSelectionContentTop(parent,tvTitle);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        tvTitle.setText(Details.title);

    }

}
