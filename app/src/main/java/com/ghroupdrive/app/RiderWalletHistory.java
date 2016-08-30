package com.ghroupdrive.app;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * Created by pk on 4/22/15.
 * 1 - money added
 * 2 - payment
 * 3 - withdrawn
 */
public class RiderWalletHistory extends RecyclerView.ViewHolder {
   private final TextView tvHisTitle;
   private final TextView tvHisSum;
   private final TextView tvSign;
   private final TextView tvPrice;
   private final ImageView ivHistoryIcon;


    public RiderWalletHistory(final View parent, TextView tvHisTitle, ImageView ivHistoryIcon, TextView tvHisSum, TextView tvSign, TextView tvPrice) {
        super(parent);
        this.tvHisTitle = tvHisTitle;
        this.ivHistoryIcon = ivHistoryIcon;
        this.tvHisSum = tvHisSum;
        this.tvSign = tvSign;
        this.tvPrice = tvPrice;

    }

    public static RiderWalletHistory newInstance(View parent) {
        TextView tvHisTitle = (TextView) parent.findViewById(R.id.tvHisTitle);
        TextView tvHisSum = (TextView) parent.findViewById(R.id.tvHisSum);
        TextView tvSign = (TextView) parent.findViewById(R.id.tvSign);
        TextView tvPrice = (TextView) parent.findViewById(R.id.tvPrice);
        ImageView ivHistoryIcon = (ImageView) parent.findViewById(R.id.ivHistoryIcon);
        return new RiderWalletHistory(parent,tvHisTitle,ivHistoryIcon,tvHisSum,tvSign,tvPrice);
    }

    public void setItemText(final GettersAndSetters Details, final Context context, final int position) {
        tvHisTitle.setText(Details.title);
        tvHisSum.setText(Details.summary);
        tvPrice.setText(Details.price);

        if(Details.historyType == 1)
        {
            //money added here
            ivHistoryIcon.setImageResource(R.drawable.history_add);
            tvHisSum.setTextColor(Color.parseColor("#6dac28"));
            tvSign.setTextColor(Color.parseColor("#6dac28"));
            tvSign.setText("+");
        }else if(Details.historyType == 2)
        {
            //payment here
            ivHistoryIcon.setImageResource(R.drawable.history_add);
            tvHisSum.setTextColor(Color.parseColor("#ec9157"));
            tvSign.setTextColor(Color.parseColor("#ec9157"));
            tvSign.setText("+");
        }else if(Details.historyType == 3)
        {
            //withdrawn  here
            ivHistoryIcon.setImageResource(R.drawable.history_subtruct);
            tvHisSum.setTextColor(Color.parseColor("#2d9cf5"));
            tvSign.setTextColor(Color.parseColor("#2d9cf5"));
            tvSign.setText("-");
        }

    }

}
