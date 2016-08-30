package com.ghroupdrive.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/12/16.
 */
public class TripSubDetailsFragment extends Fragment {



    ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    TripAdapter recyclerAdapter;
    UserFunctions functions;
    String jsonString;
    RideObject ro;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.trip_sub_details, container, false);
        recyclerView = (RecyclerView) theLayout.findViewById(R.id.recyclerView);
        initRecyclerView();
        jsonString = getActivity().getIntent().getStringExtra(StaticVariables.JSONSTRING);
        functions = new UserFunctions(getActivity());

        ro = new RideObject(jsonString,functions);


        bindData();
        return  theLayout;

    }



    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacingsmall);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));













        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click

                    }
                })
        );
    }



    private  void  bindData()
    {
      try
      {
          details = new ArrayList<>();
          JSONArray passengers = ro.Passengers;
          if(passengers.length()>0)
          {
              GettersAndSetters Details;
              Details = new GettersAndSetters();
              Details.setType(StaticVariables.TOPTYPE);
              details.add(Details);
              for(int i=0; i<passengers.length(); i++)
              {
                  JSONObject c = passengers.getJSONObject(i);
                  ProfileObject po = new ProfileObject(c.toString(),functions);
                  Details = new GettersAndSetters();
                  Details.setName(po.Fullname);
                  Details.setSeatNo("Seat "+po.Seat);
                  Details.setType(StaticVariables.CONTENT);
                  Details.setJsonString(c.toString());
                  Details.setDriversProfileUrl(po.ProfileUrl);
                  details.add(Details);
              }

          }

          recyclerAdapter = new TripAdapter(details, getActivity());
          recyclerView.setAdapter(recyclerAdapter);

      }catch (Exception ex)
      {
          ex.printStackTrace();
      }
    }

}
