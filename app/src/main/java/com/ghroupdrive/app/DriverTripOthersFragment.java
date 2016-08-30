package com.ghroupdrive.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by matiyas on 8/12/16.
 */
public class DriverTripOthersFragment extends Fragment {

    TextView tvLoc1,tvLoc2,tvStart,tvEnd;
    String last = "", first ="", start ="", end ="";
   public static TextView tvSet,tvBasePrice;
    UserFunctions functions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.driver_trip_other, container, false);
        tvLoc1 = (TextView) theLayout.findViewById(R.id.tvLoc1);
        tvLoc2 = (TextView) theLayout.findViewById(R.id.tvLoc2);
        tvStart = (TextView) theLayout.findViewById(R.id.tvStart);
        tvEnd = (TextView) theLayout.findViewById(R.id.tvEnd);
        tvSet = (TextView) theLayout.findViewById(R.id.tvSet);
        tvBasePrice = (TextView) theLayout.findViewById(R.id.tvBasePrice);

        last = getActivity().getIntent().getStringExtra("last");
        first = getActivity().getIntent().getStringExtra("first");
        start = getActivity().getIntent().getStringExtra("start");
        end = getActivity().getIntent().getStringExtra("end");
        functions = new UserFunctions(getActivity());

        tvBasePrice.setText("GHS "+functions.getPref(StaticVariables.BASEPRICE,2));

        if(!last.contentEquals(""))
        {
            tvLoc2.setVisibility(View.VISIBLE);
            tvLoc2.setText(last);
        }

        if(!first.contentEquals(""))
        {
            tvLoc1.setVisibility(View.VISIBLE);
            tvLoc1.setText(first);
        }

        tvStart.setText(start);
        tvEnd.setText(end);

        return  theLayout;

    }
}
