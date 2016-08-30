package com.ghroupdrive.app;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by matiyas on 8/18/16.
 */
public class UserProfile extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {


    ViewPager vpPager;
    ViewPager vpPager2;
    private static final int NUM_PAGES = 4;
    private PagerAdapter mPagerAdapter;
    private BannerAdapter mPagerAdapter2;
    ImageView iview[] = new ImageView[2];
    int selectedPos = 0;


    private static final int BANNERNUM = 2;
    int images[] = {R.drawable.car1, R.drawable.sampimage};


    boolean change = true;
    AppBarLayout appbarLayout;
    private int mMaxScrollSize;
    private static final int PERCENTAGE_TO_ANIMATE_AVATAR = 20;
    private boolean mIsAvatarShown = true;
    ImageView ivProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        vpPager = (ViewPager)findViewById(R.id.vpPager);
        vpPager2 = (ViewPager) findViewById(R.id.vpPager2);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        iview[0] = (ImageView)findViewById(R.id.ivOne);
        iview[1] = (ImageView)findViewById(R.id.ivTwo);

        CollapsingToolbarLayout collapsingToolbarLayout =
                (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);

        collapsingToolbarLayout.setTitle("");
        initToolbar();

        appbarLayout = (AppBarLayout) findViewById(R.id.main_appbar);

        mMaxScrollSize = appbarLayout.getTotalScrollRange();


        vpPager.setPageMargin(25);
        vpPager.setPageMarginDrawable(R.color.mainback);

        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPagerAdapter2 = new BannerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(mPagerAdapter);
        vpPager2.setAdapter(mPagerAdapter2);

        vpPager.setClipToPadding(false);
        vpPager.setPadding(70, 0, 70, 0);


        vpPager2.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int pos = vpPager2.getCurrentItem();
                if (selectedPos != pos) {
                    selectedPos = pos;
                    change = false;
                    if (pos == 0) {
                        ivProfile.setImageResource(images[1]);
                    } else if (pos == 1) {
                        ivProfile.setImageResource(images[0]);
                    }
                    alternateImage(pos);
                    change = true;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });



    }



    private void initToolbar() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        // mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }




    @Override
    protected void onPause() {
        super.onPause();
        appbarLayout.removeOnOffsetChangedListener(this);
    }

    @Override
    protected void onResume() {
        appbarLayout.addOnOffsetChangedListener(this);

        super.onResume();
    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case android.R.id.home:
                setResult(RESULT_OK);
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {

        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int percentage = (Math.abs(i)) * 100 / mMaxScrollSize;

        if (percentage >= PERCENTAGE_TO_ANIMATE_AVATAR && mIsAvatarShown) {
            mIsAvatarShown = false;
            ivProfile.animate().scaleY(0).scaleX(0).setDuration(200).start();
        }

        if (percentage <= PERCENTAGE_TO_ANIMATE_AVATAR && !mIsAvatarShown) {
            mIsAvatarShown = true;

            ivProfile.animate()
                    .scaleY(1).scaleX(1)
                    .start();
        }


    }


    private void alternateImage(int pos)
    {
        for(int i=0; i<iview.length; i++)
        {
            if(i == pos)
            {
                iview[i].setImageResource(R.drawable.myovalwhite);
            }else
            {
                iview[i].setImageResource(R.drawable.myovalgrey);
            }
        }
    }








    public static class ImageOne extends Fragment {


        ImageView ivCarImages;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.images , container, false);
            ivCarImages = (ImageView) theLayout.findViewById(R.id.ivCarImages);

            ivCarImages.setImageResource(R.drawable.car1);
            return theLayout;

        }
    }



    public static class ImageTwo extends Fragment {


        ImageView ivCarImages;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.images , container, false);
            ivCarImages = (ImageView) theLayout.findViewById(R.id.ivCarImages);

            ivCarImages.setImageResource(R.drawable.car2);
            return theLayout;

        }
    }


    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return new ImageOne();

                case 1:
                    return new ImageTwo();
                case 2:
                    return new ImageThree();
                case 3:
                    return new ImageFour();
            }

            return null;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public static class ImageThree extends Fragment {


        ImageView ivCarImages;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.images , container, false);
            ivCarImages = (ImageView) theLayout.findViewById(R.id.ivCarImages);

            ivCarImages.setImageResource(R.drawable.car3);
            return theLayout;

        }
    }

    public static class ImageFour extends Fragment {

        ImageView ivCarImages;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            if (container == null) {
                return null;
            }
            View theLayout = inflater.inflate(
                    R.layout.images , container, false);
            ivCarImages = (ImageView) theLayout.findViewById(R.id.ivCarImages);


            ivCarImages.setImageResource(R.drawable.car4);
            return theLayout;

        }
    }









    private class BannerAdapter extends FragmentStatePagerAdapter {
        public BannerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            BannerFragment hold = new BannerFragment();
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
