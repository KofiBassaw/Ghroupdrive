package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

/**
 * Created by matiyas on 8/12/16.
 */
public class DriverTripDetailsFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {


    boolean oneS = false, twoS = false, threeS = false, fourS = false;


    ImageView ivSeat1,ivSeat2,ivSeat3,ivSeat4;
    RippleView rpSeat1,rpSeat2,rpSeat3,rpSeat4;

    UserFunctions functions;
    LinearLayout llSetTime;
    TextView tvStartOffTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.driver_trip_details, container, false);
        llSetTime = (LinearLayout) theLayout.findViewById(R.id.llSetTime);
        tvStartOffTime = (TextView) theLayout.findViewById(R.id.tvStartOffTime);





        ivSeat1 = (ImageView) theLayout.findViewById(R.id.ivSeat1);
        ivSeat2 = (ImageView) theLayout.findViewById(R.id.ivSeat2);
        ivSeat3 = (ImageView) theLayout.findViewById(R.id.ivSeat3);
        ivSeat4 = (ImageView) theLayout.findViewById(R.id.ivSeat4);


        rpSeat1 = (RippleView) theLayout.findViewById(R.id.rpSeat1);
        rpSeat2 = (RippleView) theLayout.findViewById(R.id.rpSeat2);
        rpSeat3 = (RippleView) theLayout.findViewById(R.id.rpSeat3);
        rpSeat4 = (RippleView) theLayout.findViewById(R.id.rpSeat4);





        ivSeat1.setOnClickListener(this);
        ivSeat2.setOnClickListener(this);
        ivSeat3.setOnClickListener(this);
        ivSeat4.setOnClickListener(this);






        llSetTime.setOnClickListener(this);


        return  theLayout;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ivSeat1:
                rpSeat1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {



                            if(!oneS) {
                                //set oneS to false
                                oneS = true;
                                ivSeat1.setImageResource(R.drawable.one_locked);
                                DriverTrips.seat1 = 2;

                            }else
                            {
                                ivSeat1.setImageResource(R.drawable.one_empty);
                                oneS = false;
                                DriverTrips.seat1 = 0;
                            }
                        checkTrips();

                    }
                });
                break;

            case R.id.ivSeat2:
                rpSeat2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                          if(!twoS) {
                              //set oneS to false
                              twoS = true;
                              ivSeat2.setImageResource(R.drawable.two_locked);
                              DriverTrips.seat2 = 2;

                          }else
                          {
                              ivSeat2.setImageResource(R.drawable.two_empty);
                              twoS = false;
                              DriverTrips.seat2 = 0;
                          }

                        checkTrips();

                    }
                });
                break;

            case R.id.ivSeat3:
                rpSeat3.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                       if(!threeS) {
                           //set oneS to false
                           threeS = true;
                           ivSeat3.setImageResource(R.drawable.three_locked);
                           DriverTrips.seat3 = 2;


                       }else
                       {
                           ivSeat3.setImageResource(R.drawable.three_empty);
                           threeS = false;
                           DriverTrips.seat3 = 0;
                       }
                        checkTrips();


                    }
                });
                break;

            case R.id.ivSeat4:
                rpSeat4.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                            if(!fourS) {
                                //set oneS to false
                                fourS = true;
                                ivSeat4.setImageResource(R.drawable.four_locked);
                                DriverTrips.seat4 = 2;

                            }else
                            {
                                ivSeat4.setImageResource(R.drawable.four_empty);
                                fourS = false;
                                DriverTrips.seat3 = 0;
                            }

                        checkTrips();


                    }


                });
                break;
            case R.id.llSetTime:

                showTimer();
                break;
        }
    }





    private void checkTrips()
    {

        String tripString = "";

        if(oneS)
        {
            if (tripString.length()>0)
                tripString += ", ";

            tripString += "Seat 1";
        }

        if(twoS)
        {
            if (tripString.length()>0)
                tripString += ", ";

            tripString += "Seat 2";
        }

        if(threeS)
        {
            if (tripString.length()>0)
                tripString += ", ";

            tripString += "Seat 3";
        }


   if(fourS)
        {
            if (tripString.length()>0)
                tripString += ", ";

            tripString += "Seat 4";
        }

        if(tripString.contentEquals(""))
        {
            DriverTripOthersFragment.tvSeats.setText("No Seats");
        }else
        {
            DriverTripOthersFragment.tvSeats.setText(tripString);
        }

    }



    private void showTimer()
    {
        Calendar now = Calendar.getInstance();
        TimePickerDialog tpd = TimePickerDialog.newInstance(this,now.get(Calendar.HOUR),now.get(Calendar.MINUTE),now.get(Calendar.SECOND),true);

        tpd.show(getActivity().getFragmentManager(), "TimepickerDialog");

               //(TimePickerDialog) getActivity().getFragmentManager().findFragmentByTag("TimepickerDialog");
       // tpd.show(getActivity().getFragmentManager(), "TimepickerDialog");



       // if(tpd != null) tpd.setOnTimeSetListener(this);


    }






    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int i, int i1, int i2) {
        String time = i+":"+i1;
        tvStartOffTime.setText(time);
        DriverTrips.startOffTime = time;
        DriverTripOthersFragment.tvSet.setText(time);
    }
}
