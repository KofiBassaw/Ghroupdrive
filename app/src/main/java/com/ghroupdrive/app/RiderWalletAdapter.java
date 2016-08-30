package com.ghroupdrive.app;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by pk on 4/22/15.
 */
public class RiderWalletAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GettersAndSetters> mItemList;
    Context _c;

    public RiderWalletAdapter(ArrayList<GettersAndSetters> itemList, Context c) {
        mItemList = itemList;
        this._c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == StaticVariables.TOPTYPE){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_wallet_top, parent, false);
            return RiderWalletTop.newInstance(view);
        }else
        {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_wallet_bottom, parent, false);
            return RiderWalletHistory.newInstance(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        GettersAndSetters Details=mItemList.get(position);

        switch(getType(position)){

            case StaticVariables.TOPTYPE:
                RiderWalletTop holder = (RiderWalletTop) viewHolder;

                holder.setItemText(Details,_c,position);

                break;
            case StaticVariables.CONTENT:
                RiderWalletHistory  holder2 = (RiderWalletHistory) viewHolder;
                holder2.setItemText(Details,_c,position);
                break;
        }



    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }



    @Override
    public int getItemViewType(int position) {
        return mItemList.get(position).type;
    }


    private int getType(int position) {
        return mItemList.get(position).type;
    }


}
