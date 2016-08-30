package com.ghroupdrive.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.andexert.library.RippleView;
import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

/**
 * Created by matiyas on 8/12/16.
 */
public class TripDetailsFragment extends Fragment implements View.OnClickListener {


    boolean oneS = false, twoS = false, threeS = false, fourS = false;
    boolean onel = false, twol = false, threel = false, fourl = true;


    ImageView ivSeat1,ivSeat2,ivSeat3,ivSeat4;
    RippleView rpSeat1,rpSeat2,rpSeat3,rpSeat4;

    ImageView ivProfile;
    UserFunctions functions;
    String jsonString;
    RideObject ro;

    TextView tvName,tvPrice;
    RatingBar ratingbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.trip_details, container, false);
        ivSeat1 = (ImageView) theLayout.findViewById(R.id.ivSeat1);
        ivSeat2 = (ImageView) theLayout.findViewById(R.id.ivSeat2);
        ivSeat3 = (ImageView) theLayout.findViewById(R.id.ivSeat3);
        ivSeat4 = (ImageView) theLayout.findViewById(R.id.ivSeat4);
        tvName = (TextView) theLayout.findViewById(R.id.tvName);
        tvPrice = (TextView) theLayout.findViewById(R.id.tvPrice);
        ratingbar = (RatingBar) theLayout.findViewById(R.id.ratingbar);


        rpSeat1 = (RippleView) theLayout.findViewById(R.id.rpSeat1);
        rpSeat2 = (RippleView) theLayout.findViewById(R.id.rpSeat2);
        rpSeat3 = (RippleView) theLayout.findViewById(R.id.rpSeat3);
        rpSeat4 = (RippleView) theLayout.findViewById(R.id.rpSeat4);
        ivProfile = (ImageView) theLayout.findViewById(R.id.ivProfile);
        jsonString = getActivity().getIntent().getStringExtra(StaticVariables.JSONSTRING);
        functions = new UserFunctions(getActivity());

        ro = new RideObject(jsonString,functions);


        if(ro.Seat1 != 0)
        {
            onel = true;
            ivSeat1.setImageResource(R.drawable.one_locked);
        }

        if(ro.Seat2 != 0)
        {
            twol = true;
            ivSeat2.setImageResource(R.drawable.two_locked);
        }

        if(ro.Seat3 != 0)
        {
            threel = true;
            ivSeat3.setImageResource(R.drawable.three_locked);
        }

        if(ro.Seat4 != 0)
        {
            fourl = true;
            ivSeat4.setImageResource(R.drawable.four_locked);
        }




        ivSeat1.setOnClickListener(this);
        ivSeat2.setOnClickListener(this);
        ivSeat3.setOnClickListener(this);
        ivSeat4.setOnClickListener(this);
        ivProfile.setOnClickListener(this);




        ProfileObject po = new ProfileObject(ro.Driver.toString(),functions);
        tvName.setText(po.Fullname);
        tvPrice.setText("GHS "+ro.Price);

        String url = StaticVariables.CLOUDORDINARYURL+"w_100,h_100/"+po.ProfileUrl+".jpg";
        AQuery aq = new AQuery(getActivity());
        ImageOptions op=new ImageOptions();
        op.fileCache = true;
        op.memCache=true;
        op.targetWidth = 0;
        op.fallback = R.drawable.uploadpicture;
        aq.id(ivProfile).image(url, op);



        if(ro.Passengers.length()>0)
        {
            ratingbar.setRating(ro.Rate/ro.Passengers.length());
        }else
        {
            ratingbar.setRating(0);
        }




        return  theLayout;
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
              String point = intent.getStringExtra("point");
                int pos = intent.getExtras().getInt("pos");
                switchSeat(pos);

                if(point.contentEquals("start"))
                {
                    Trips.selectedLoc = new LocationObj(ro.RideStart.toString(), functions);
                }else if(point.contentEquals("point1"))
                {
                    Trips.selectedLoc = new LocationObj(ro.Stop1.toString(), functions);
                }else if(point.contentEquals("point2"))
                {
                    Trips.selectedLoc = new LocationObj(ro.Stop2.toString(), functions);
                }else if(point.contentEquals("end"))
                {
                    Trips.selectedLoc = new LocationObj(ro.RideEnd.toString(), functions);
                }
            }


        }
    };








    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.ivSeat1:
                rpSeat1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {


                        if(!onel)
                        {
                            if(!oneS) {
                                //set oneS to false

                                showPanel(1);
                            }else
                            {
                                Trips.pos = 0;
                                Intent it = new Intent(StaticVariables.TRIPSELECTION);
                                it.putExtra("type","deselect");
                                getActivity().sendBroadcast(it);
                                Trips.selectedLoc = null;
                                ivSeat1.setImageResource(R.drawable.one_empty);
                                oneS = false;
                            }
                        }
                    }
                });
                break;

            case R.id.ivSeat2:
                rpSeat2.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                      if(!twol)
                      {
                          if(!twoS) {
                              //set oneS to false


                              showPanel(2);
                          }else
                          {
                              Trips.pos = 0;
                              Intent it = new Intent(StaticVariables.TRIPSELECTION);
                              it.putExtra("type","deselect");
                              getActivity().sendBroadcast(it);
                              Trips.selectedLoc = null;
                              ivSeat2.setImageResource(R.drawable.two_empty);
                              twoS = false;
                          }
                      }
                    }
                });
                break;

            case R.id.ivSeat3:
                rpSeat3.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                   if(!threel)
                   {
                       if(!threeS) {
                           //set oneS to false


                           showPanel(3);
                       }else
                       {
                           Trips.pos = 0;
                           Intent it = new Intent(StaticVariables.TRIPSELECTION);
                           it.putExtra("type","deselect");
                           getActivity().sendBroadcast(it);
                           Trips.selectedLoc = null;
                           ivSeat3.setImageResource(R.drawable.three_empty);
                           threeS = false;
                       }
                   }
                    }
                });
                break;

            case R.id.ivSeat4:
                rpSeat4.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        if(!fourl)
                        {
                            if(!fourS) {
                                //set oneS to false


                                showPanel(4);
                            }else
                            {
                                Trips.pos = 0;
                                Intent it = new Intent(StaticVariables.TRIPSELECTION);
                                it.putExtra("type","deselect");
                                getActivity().sendBroadcast(it);
                                Trips.selectedLoc = null;
                                ivSeat4.setImageResource(R.drawable.four_empty);
                                fourS = false;
                            }
                        }

                    }


                });
                break;
            case R.id.ivProfile:
                JSONObject driver = ro.Driver;
                functions = new UserFunctions(getActivity());
                functions.showUserProfileDialog();

                break;
        }
    }



    private void showPanel(int pos)
    {
        Intent it = new Intent(StaticVariables.TRIPMESSAGE);
        it.putExtra("type","open");
        it.putExtra("pos",pos);
        getActivity().sendBroadcast(it);

    }






    private void switchSeat(int pos)
    {
        switch (pos)
        {
            case 1:

                oneS = true;
                ivSeat1.setImageResource(R.drawable.one_selected);

                if (!twol) {
                    ivSeat2.setImageResource(R.drawable.two_empty);
                    twoS = false;
                }

                if (!threel) {
                    ivSeat3.setImageResource(R.drawable.three_empty);
                    threeS = false;
                }

                if (!fourl){
                    ivSeat4.setImageResource(R.drawable.four_empty);
                    fourS = false;

                }
            break;

            case 2:

                twoS = true;
                ivSeat2.setImageResource(R.drawable.two_selected);

                if (!onel) {
                    ivSeat1.setImageResource(R.drawable.one_empty);
                    oneS = false;
                }
                if (!threel) {
                    ivSeat3.setImageResource(R.drawable.three_empty);
                    threeS = false;
                }

                if (!fourl){
                    ivSeat4.setImageResource(R.drawable.four_empty);
                    fourS = false;

                }
            break;

            case 3:

                threeS = true;
                ivSeat3.setImageResource(R.drawable.three_selected);

                if (!onel) {
                    ivSeat1.setImageResource(R.drawable.one_empty);
                    oneS = false;
                }

                if (!twol){
                    ivSeat2.setImageResource(R.drawable.two_empty);
                    twoS = false;
                }

                if(!fourl) {
                    ivSeat4.setImageResource(R.drawable.four_empty);
                    fourS = false;
                }

            break;


            case 4:

                fourS = true;
                ivSeat4.setImageResource(R.drawable.four_selected);

                if (!onel) {
                    ivSeat1.setImageResource(R.drawable.one_empty);
                    oneS = false;
                }

                if (!twol){
                    ivSeat2.setImageResource(R.drawable.two_empty);
                    twoS = false;
                }


                if(!threel) {
                    ivSeat3.setImageResource(R.drawable.three_empty);
                    threeS = false;
                }

            break;
        }

    }
}
