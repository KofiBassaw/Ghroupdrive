package com.ghroupdrive.app;

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
public class RiderWalletFragement extends Fragment {


ArrayList<GettersAndSetters> details;
    RecyclerView recyclerView;
    RiderWalletAdapter recyclerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.rider_wallet, container, false);
details = new ArrayList<>();
        recyclerView = (RecyclerView) theLayout.findViewById(R.id.recyclerView);
        initRecyclerView();

        bindData();
        return  theLayout;
    }


    private void initRecyclerView() {



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

    private void bindData()
    {

        GettersAndSetters Details;

        Details = new GettersAndSetters();
        Details.setType(StaticVariables.TOPTYPE);
        details.add(Details);



        String  title[] = {"Speedbanking Voucher","To Hilla Limmann Hall","Mobile Money"};
        String sum[] = {"Money loaded to wallet","Payment received for ride","Money withdrawn from wallet"};
        String price[] = {"GHS 25.40","GHS 5.00","GHS 15.00"};
        int type[] = {1,2,3};

           for(int i=0; i<price.length; i++)
           {
               Details = new GettersAndSetters();
                Details.setPrice(price[i]);
                Details.setSummary(sum[i]);
                Details.setTitle(title[i]);
               Details.setType(StaticVariables.CONTENT);
               Details.setHistoryType(type[i]);
               details.add(Details);
           }

        recyclerAdapter = new RiderWalletAdapter(details, getActivity());
        recyclerView.setAdapter(recyclerAdapter);

    }
}
