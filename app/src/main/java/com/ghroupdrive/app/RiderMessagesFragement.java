package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/9/16.
 */
public class RiderMessagesFragement extends Fragment {


ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    RiderMessageAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.rider_messages, container, false);
        details = new ArrayList<>();
        recyclerView = (RecyclerView) theLayout.findViewById(R.id.recyclerView);
        initRecyclerView();

        bindData();
        return  theLayout;
    }




    private void bindData()
    {

        GettersAndSetters Details;

        Details = new GettersAndSetters();
        Details.setRead(false);
        Details.setDate("Now");
        Details.setTitle("Welcome to groupdrive!");
        Details.setName("Nicole @Ghroupdrive");
        details.add(Details);


        Details = new GettersAndSetters();
        Details.setRead(true);
        Details.setDate("11:24 AM");
        Details.setName("Williams Cudjoe");
        Details.setTitle("Thank you");
        details.add(Details);


        recyclerAdapter = new RiderMessageAdapter(details, getActivity());
        recyclerView.setAdapter(recyclerAdapter);

    }

    private void initRecyclerView() {




        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        recyclerView.setPadding(recyclerView.getPaddingLeft(), recyclerView.getPaddingTop(), recyclerView.getPaddingRight(), recyclerView.getPaddingBottom());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));













        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Intent it = new Intent(getActivity(), Chat.class);
                        if(position == 0)
                        {
                            it.putExtra("type","group");
                        }else
                        {
                            it.putExtra("type","message");
                        }
                           startActivity(it);


                    }
                })
        );
    }
}
