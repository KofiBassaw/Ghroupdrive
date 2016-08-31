package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;


/**
 * Created by pk on 4/22/15.
 */
public class GotitViewHolder extends RecyclerView.ViewHolder {
    private final RelativeLayout rlGotit;
    private final RippleView rpGotIt;


    public GotitViewHolder(final View parent, RelativeLayout rlGotit, RippleView rpGotIt) {
        super(parent);
        this.rlGotit = rlGotit;
        this.rpGotIt = rpGotIt;

    }

    public static GotitViewHolder newInstance(View parent) {
        RelativeLayout rlGotit = (RelativeLayout) parent.findViewById(R.id.rlGotit);
        RippleView rpGotIt = (RippleView) parent.findViewById(R.id.rpGotIt);
        return new GotitViewHolder(parent,rlGotit,rpGotIt);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        rlGotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpGotIt.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        Intent it = new Intent(StaticVariables.SEARCHMESSAGE);
                        it.putExtra("type","gotit");
                        context.sendBroadcast(it);
                    }
                });
            }
        });

    }

}
