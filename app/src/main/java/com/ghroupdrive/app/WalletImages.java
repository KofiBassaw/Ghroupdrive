package com.ghroupdrive.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by matiyas on 8/11/16.
 */
public class WalletImages extends Fragment {


    int images[] = {R.drawable.speedbanking, R.drawable.mm,R.drawable.atm};
    ImageView ivImage;
    int position =0;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        View theLayout = inflater.inflate(
                R.layout.wallet_images, container, false);
        ivImage = (ImageView) theLayout.findViewById(R.id.ivImage);
        position = getArguments().getInt("pos");

        ivImage.setImageResource(images[position]);
        return  theLayout;
    }
}
