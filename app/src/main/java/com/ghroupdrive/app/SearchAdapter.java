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
public class SearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<GettersAndSetters> mItemList;
    Context _c;

    public SearchAdapter(ArrayList<GettersAndSetters> itemList, Context c) {
        mItemList = itemList;
        this._c = c;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == StaticVariables.TOPTYPE){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_top, parent, false);
            return SearchTop.newInstance(view);
        }else if(viewType == StaticVariables.BOOKTYPE){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
            return SearchBookedContent.newInstance(view);
        }else if(viewType == StaticVariables.GOTIT){
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gotit_root, parent, false);
            return GotitViewHolder.newInstance(view);
        }else
        {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_item, parent, false);
            return SearchTopContent.newInstance(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        GettersAndSetters Details=mItemList.get(position);

        switch(getType(position)){

            case StaticVariables.TOPTYPE:
                SearchTop holder = (SearchTop) viewHolder;

                holder.setItemText(Details,_c,position);

                break;

            case StaticVariables.CONTENT:
                SearchTopContent  holder2 = (SearchTopContent) viewHolder;
                holder2.setItemText(Details,_c,position);
                break;
            case StaticVariables.BOOKTYPE:
                SearchBookedContent  holder3 = (SearchBookedContent) viewHolder;
                holder3.setItemText(Details,_c,position);
                break;
            case StaticVariables.GOTIT:
                GotitViewHolder  gotit = (GotitViewHolder) viewHolder;
                gotit.setItemText(Details,_c,position);
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
