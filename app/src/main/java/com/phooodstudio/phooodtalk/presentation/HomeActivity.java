package com.phooodstudio.phooodtalk.presentation;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.phooodstudio.phooodtalk.R;

/**
 * Created by Christopher Cabreros on 25-Jun-16.
 * Defines the home activity
 */
public class HomeActivity extends AppCompatActivity{

    //Variables
    //    private SlidingTabLayout mTabs;
    private CharSequence mTitles[] = {"Home", "Journal", "Friends"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Setup the pager and the adapter
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager(), mTitles);
        ViewPager toolbarPager = (ViewPager) findViewById(R.id.home_pager);
        if (toolbarPager != null) {
            toolbarPager.setAdapter(homePagerAdapter);
        }

        //Setup tab layout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.home_tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(toolbarPager);
        }

    }


    /**
     * Class that helps fill the toolbar dynamically.
     */
    public class HomePagerAdapter extends FragmentStatePagerAdapter {

        private CharSequence mTitles[]; //stores the titles
        private int mNumTabs; // stores the number of tabs

        /**
         * Constructor
         * @param fm - fragment manager from parent activity
         * @param titles - a sequence of titles.
         */
        public HomePagerAdapter(FragmentManager fm, CharSequence[] titles) {
            super(fm);

            mTitles = titles;
            mNumTabs = titles.length;

            if (titles.length <= 2){
                throw new IllegalArgumentException("Titles does not have enough titles");
            }
        }


        /**
         * Gets the item at the current position
         * @param position - position to get the fragment at
         * @return - fragment that corresponds to each position
         */
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0: return new FeedFragment();
                case 1: return new JournalFragment();
                case 2: return new FriendsFragment();
                default: return new Fragment();
            }
        }


        /**
         * Gets the page title at the position
         * @param position - position to get the title at
         * @return - title at specific position
         */
        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= mNumTabs){
                throw new IndexOutOfBoundsException("HomePagerAdapter method getPageTitle has " +
                        "position out of bounds.");
            }
            return mTitles[position];
        }


        /**
         * Returns the amount of tabs that exist
         * @return - number of tabs that exist
         */
        @Override
        public int getCount() {
            return mNumTabs;
        }
    }


}
