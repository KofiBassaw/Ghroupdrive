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
public class RiderMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GettersAndSetters> mItemList;
    Context _c;

    public RiderMessageAdapter(ArrayList<GettersAndSetters> itemList, Context c) {
        mItemList = itemList;
        this._c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            return RiderMessageViewHolder.newInstance(view);


    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        GettersAndSetters Details=mItemList.get(position);


        RiderMessageViewHolder holder = (RiderMessageViewHolder) viewHolder;

                holder.setItemText(Details,_c,position);




    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }






}
