package com.ncslab.pyojihye.interpret.Fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by PYOJIHYE on 2017-07-13.
 */

public class MyFragmentPagerAdapter extends FragmentStatePagerAdapter {
    private final String TAG = "MyFragmentPagerAdapter";

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
//        Log.d(TAG, "getCount()");
        return 2;
    }

    @Override
    public int getItemPosition(Object object) {
//        Log.d(TAG, "getItemPosition()");
        return super.getItemPosition(object);
    }

    @Override
    public Fragment getItem(int position) {
//        Log.d(TAG, "getItem()");

        switch (position) {
            case 0:
                return ViewerOptionFragment.getInstance();

            case 1:
                return ViewerOptionListFragment.getInstance();

            default:
                break;
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        Log.d(TAG, "getPageTitle()");

        switch (position) {
            case 0:
                return "Setting";
            case 1:
                return "Delete Word";
        }
        return "";
    }
}