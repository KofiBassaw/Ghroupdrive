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
public class RiderJoinAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GettersAndSetters> mItemList;
    Context _c;

    public RiderJoinAdapter(ArrayList<GettersAndSetters> itemList, Context c) {
        mItemList = itemList;
        this._c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == StaticVariables.TOPTYPE){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trider_join_top_item, parent, false);
            return TripTopViewHolder.newInstance(view);
        }else if(viewType == StaticVariables.CONTENT)
        {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_join_content, parent, false);
            return TripContentViewHolder.newInstance(view);
        }else {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rider_join_brief, parent, false);
            return TripBriefViewHolder.newInstance(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        GettersAndSetters Details=mItemList.get(position);

        switch(getType(position)){

            case StaticVariables.TOPTYPE:
                TripTopViewHolder holder = (TripTopViewHolder) viewHolder;

                holder.setItemText(Details,_c,position);

                break;
            case StaticVariables.BRIEF:
                TripBriefViewHolder holder3 = (TripBriefViewHolder) viewHolder;

                holder3.setItemText(Details,_c,position);

                break;
            case StaticVariables.CONTENT:
                TripContentViewHolder  holder2 = (TripContentViewHolder) viewHolder;
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
