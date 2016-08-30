package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andexert.library.RippleView;

/**
 * Created by matiyas on 8/12/16.
 */
public class WalletActivity extends AppCompatActivity implements View.OnClickListener {


    ViewPager vpPager;
    private PagerAdapter mPagerAdapter;

    private static final int BANNERNUM = 3;
    private Toolbar toolbar;
    boolean canChange = true;
    int selectedPos = 1;
    LinearLayout holders[] = new LinearLayout[3];
    String walletArray[];
    TextView tvDetails;
    ImageView iview[] = new ImageView[3];


    RelativeLayout rlLoad;
    RippleView rpLoad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadwallet);
        toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        tvDetails = (TextView) findViewById(R.id.tvDetails);
        rpLoad = (RippleView) findViewById(R.id.rpLoad);
        rlLoad = (RelativeLayout) findViewById(R.id.rlLoad);
        vpPager.setPageMargin(35);
        walletArray = getResources().getStringArray(R.array.walletarray);
        vpPager.setPageMarginDrawable(R.color.colorPrimary);
        mPagerAdapter = new BannerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mPagerAdapter);

        holders[0] =  (LinearLayout) findViewById(R.id.llSpeed);
        holders[1] = (LinearLayout) findViewById(R.id.llmmHolder);
        holders[2] = (LinearLayout) findViewById(R.id.llatmholder);
        iview[0] = (ImageView) findViewById(R.id.ivOne);
        iview[1] = (ImageView) findViewById(R.id.ivTwo);
        iview[2] = (ImageView) findViewById(R.id.ivThree);

        vpPager.setClipToPadding(false);
        vpPager.setPadding(70, 0, 70, 0);
        vpPager.setCurrentItem(1);

        vpPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int pos = vpPager.getCurrentItem();
                if (pos != selectedPos) {
                    selectedPos = pos;
                    changeLayoutt(pos);

                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        rlLoad.setOnClickListener(this);

    }




    private void changeLayoutt(int pos)
    {
        tvDetails.setText(walletArray[pos]);
        for(int i=0; i<holders.length; i++)
        {
            if(i==pos)
            {
                holders[i].setVisibility(View.VISIBLE);
                iview[i].setImageResource(R.drawable.myovalwhite);

            }else
            {
                holders[i].setVisibility(View.GONE);
                iview[i].setImageResource(R.drawable.myovalgrey);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.rlLoad:
                rpLoad.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {
                        startActivity(new Intent(WalletActivity.this,AfterLoadingWallet.class));

                        finish();
                    }
                });
                break;
        }
    }

    private class BannerAdapter extends FragmentStatePagerAdapter {
        public BannerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            WalletImages hold = new WalletImages();
            Bundle bundle=new Bundle();
            bundle.putInt("pos",position);
            hold.setArguments(bundle);
            return hold;
        }

        @Override
        public int getCount() {
            return BANNERNUM;
        }
    }


}
