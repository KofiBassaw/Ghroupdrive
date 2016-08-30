package com.ghroupdrive.app;

/**
 * Created by pk on 4/22/15.
 */
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by hp1 on 21-01-2015.
 */
public class DriverViewPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    Context c;
    private int[] imageResId;

    /*
     {
            R.drawable.tabhymn,
            R.drawable.tabservice,
            R.drawable.tabnotes
    };
     */

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public DriverViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, Context c) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.c=c;


            imageResId =  new int[5];
            imageResId[0]= R.drawable.profile_inactive;
            imageResId[1]= R.drawable.set_route;
            imageResId[2]= R.drawable.search_inactive;
            imageResId[3]= R.drawable.message_inactive;
            imageResId[4]= R.drawable.wallet_inactive;


    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {


            if(position == 0) // if the position is 0 we are returning the First tab
            {
                RiderProfileFragement tab1 = new RiderProfileFragement();
                return tab1;
            }
            else  if (position == 1)          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                DriverRoute tab2 = new DriverRoute();


                return tab2;
            }
            else  if (position == 2)          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                RiderSearchFragement tab2 = new RiderSearchFragement();
                return tab2;
            } else  if (position == 3)          // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
            {
                RiderMessagesFragement tab2 = new RiderMessagesFragement();
                return tab2;
            }else
            {
                RiderWalletFragement tab2 = new RiderWalletFragement();
                return tab2;
            }




    }

    /*
    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = c.getResources().getDrawable(imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
    */



    // This method return the titles for the Tabs in the Tab Strip

    /*
    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }
    */

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
