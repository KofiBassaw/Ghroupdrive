package com.ghroupdrive.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

import com.andexert.library.RippleView;

/**
 * Created by matiyas on 8/12/16.
 */
public class AfterLoadingWallet extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout rlLoad;
    RippleView rpLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.afterloading);
        rlLoad = (RelativeLayout) findViewById(R.id.rlLoad);
        rpLoad = (RippleView) findViewById(R.id.rpLoad);





        rlLoad.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.rlLoad:
                rpLoad.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        onBackPressed();
                    }
                });
                break;
        }
    }
}
