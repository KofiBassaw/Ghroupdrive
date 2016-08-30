package com.ghroupdrive.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by matiyas on 8/12/16.
 */
public class TripOthersFragment extends Fragment {

    RideObject rb;
    String jsonString;
    UserFunctions functions;
    TextView tvStart,tvEndPoint,tvPoint1,tvPoint2,tvPrice,tvSelectedSeat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.trip_other, container, false);
        jsonString = getActivity().getIntent().getStringExtra(StaticVariables.JSONSTRING);
        tvStart = (TextView) theLayout.findViewById(R.id.tvStart);
        tvEndPoint = (TextView) theLayout.findViewById(R.id.tvEndPoint);
        tvPoint1 = (TextView) theLayout.findViewById(R.id.tvPoint1);
        tvPoint2 = (TextView) theLayout.findViewById(R.id.tvPoint2);
        tvPrice = (TextView) theLayout.findViewById(R.id.tvPrice);
        tvSelectedSeat = (TextView) theLayout.findViewById(R.id.tvSelectedSeat);
        functions = new UserFunctions(getActivity());
        rb = new RideObject(jsonString,functions);








        bindDetails();
        return  theLayout;

    }



    private void bindDetails()
    {

        tvPrice.setText("GHS "+rb.Price);
        if(Trips.pos == 0)
        {
            tvSelectedSeat.setText("Not Set");
        }
        if(rb.RideStart !=null)
        {
            tvStart.setText(functions.getJsonString(rb.RideStart , StaticVariables.NAME));
        }



        if(rb.RideEnd != null)
        {
            tvEndPoint.setText(functions.getJsonString(rb.RideEnd, StaticVariables.NAME));
        }


        ;

        if(rb.Stop1 != null)
        {
            tvPoint1.setText(functions.getJsonString(rb.Stop1 , StaticVariables.NAME));
        }else{
            tvPoint1.setVisibility(View.GONE);
        }


        if(rb.Stop2 != null)
        {
            tvPoint2.setText(functions.getJsonString(rb.Stop2, StaticVariables.NAME));
        }else{
            tvPoint2.setVisibility(View.GONE);
        }


    }




    @Override
    public void onResume() {
        getActivity().registerReceiver(mHandleMessageReceiver, new IntentFilter(
                StaticVariables.TRIPSELECTION));
        super.onResume();
    }

    @Override
    public void onDestroy() {
        try {
            getActivity().unregisterReceiver(mHandleMessageReceiver);
        } catch (Exception e) {
            Log.e("rror", "> " + e.getMessage());
        }
        super.onDestroy();
    }






    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            String type = intent.getStringExtra("type");

            if (type.contentEquals("select"))
            {
                int pos = intent.getExtras().getInt("pos");
                tvSelectedSeat.setText("Seat "+pos);
            }else if(type.contentEquals("deselect"))
            {
                tvSelectedSeat.setText("Not Set");
            }


        }
    };
}
