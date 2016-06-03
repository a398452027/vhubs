package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


import java.util.ArrayList;
import java.util.List;

import support.ui.adapter.SetBaseAdapter;

/**
 * Created by guotengqian on 2016/4/5.
 */
public class HomePageAdapter extends PagerAdapter {


    public List<View> mListViews;
    Context context;

    public HomePageAdapter(Context context,  List<View>
            mListViews) {
        super();
        this.context = context;
        this.mListViews = mListViews;

    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object
            object) {
        ((ViewGroup) container).removeView((View) object);

        object = null;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        ((ViewPager) container).addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
