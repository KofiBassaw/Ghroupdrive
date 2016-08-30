package com.ghroupdrive.app;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;


/**
 * Created by pk on 4/22/15.
 */
public class RiderWalletTop extends RecyclerView.ViewHolder {
    private final RelativeLayout rlAdd;
    private final RippleView rpAdd;


    public RiderWalletTop(final View parent, RelativeLayout rlAdd, RippleView rpAdd) {
        super(parent);
        this.rlAdd = rlAdd;
        this.rpAdd = rpAdd;

    }

    public static RiderWalletTop newInstance(View parent) {
        RelativeLayout rlAdd = (RelativeLayout) parent.findViewById(R.id.rlAdd);
        RippleView rpAdd = (RippleView) parent.findViewById(R.id.rpAdd);
        return new RiderWalletTop(parent,rlAdd,rpAdd);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {

        rlAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpAdd.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        context.startActivity(new Intent(context,WalletActivity.class));
                    }
                });
            }
        });

    }

}
