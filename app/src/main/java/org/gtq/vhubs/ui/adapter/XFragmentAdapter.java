package org.gtq.vhubs.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by guo on 2016/5/28.
 */
public class XFragmentAdapter  extends FragmentPagerAdapter{

        private ArrayList<Fragment> fragmentsList;

        public XFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public XFragmentAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
            super(fm);
            this.fragmentsList = fragments;
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Override
        public Fragment getItem(int arg0) {
            return fragmentsList.get(arg0);
        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);
        }


}
