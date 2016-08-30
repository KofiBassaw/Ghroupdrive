package com.ghroupdrive.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;


/**
 * Created by pk on 4/22/15.
 */
public class SearchTop extends RecyclerView.ViewHolder {
    private final TextView tvSearchTitle;


    public SearchTop(final View parent, TextView tvSearchTitle) {
        super(parent);
        this.tvSearchTitle = tvSearchTitle;

    }

    public static SearchTop newInstance(View parent) {
        TextView tvSearchTitle = (TextView) parent.findViewById(R.id.tvSearchTitle);
        return new SearchTop(parent,tvSearchTitle);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        tvSearchTitle.setText(Details.title);

    }

}
