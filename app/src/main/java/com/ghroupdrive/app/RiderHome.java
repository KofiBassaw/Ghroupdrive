package com.ghroupdrive.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

/**
 * Created by matiyas on 8/9/16.
 */
public class RiderHome extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {



    ViewPager pager;
    ViewPagerAdapter adapter;
    // SlidingTabLayout tabs;
    CharSequence Titles[]={"Profile","Search","Messages","Wallet"};
    int Numboftabs =4;
    private int mMaxScrollSize;
    public static FragmentManager fragmentManager;
    private int[] imageResId ={R.drawable.profile_inactive,R.drawable.search_inactive,R.drawable.message_inactive,R.drawable.wallet_inactive};
    public static int mToolbarHeight;
    UserFunctions function;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        function = new UserFunctions(this);


        if(!function.getPref(StaticVariables.HASLOGEDING,false))
        {
            startActivity(new Intent(this,SplashScreen.class));
            finish();
        }else if (function.getPref(StaticVariables.OFFERRIDE,false))
        {
            startActivity(new Intent(this,DriverHome.class));
            finish();
        }else {


            setContentView(R.layout.rider_home);
            AppBarLayout appbarLayout = (AppBarLayout) findViewById(R.id.materialup_appbar);
            appbarLayout.addOnOffsetChangedListener(this);
            mMaxScrollSize = appbarLayout.getTotalScrollRange();
            //mToolbarContainer = (LinearLayout) findViewById(R.id.toolbarContainer);
            initToolbar();
            fragmentManager = getSupportFragmentManager();


            adapter =  new ViewPagerAdapter(fragmentManager,Titles,Numboftabs,this);

            // Assigning ViewPager View and setting the adapter



            TabLayout tabLayout = (TabLayout) findViewById(R.id.materialup_tabs);
            // Assigning ViewPager View and setting the adapter
            pager = (ViewPager) findViewById(R.id.materialup_viewpager);
            pager.setAdapter(adapter);
            // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            tabLayout.setupWithViewPager(pager);

            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(imageResId[i]);
            }


            pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            pager.setCurrentItem(1);

        }




    }


    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(mToolbar);
        setTitle("");

        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        mToolbarHeight = Utils.getToolbarHeight(this);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }
}
