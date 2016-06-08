package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.gtq.vhubs.R;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HistoryMoive;
import org.gtq.vhubs.ui.adapter.XFragmentAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import support.ui.activity.VBaseActivity;
import support.ui.frt.BaseFrtFactory;

/**
 * Created by guo on 2016/5/15.
 */
public class HomeActivity extends ViewpageActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        select_iv0.getBackground().setAlpha(255);
        select_iv1.getBackground().setAlpha(0);
    }



    @Override
    protected void initFragment() {
        // Fragment容器
        fragmentsList = new ArrayList<Fragment>();
        // 生成每个tab对应的fragment
        Fragment featured = BaseFrtFactory.createForActivityView(
                FeaturedActivity.class.getName(), "home");
        Fragment classification = BaseFrtFactory.createForActivityView(
                ClassificationActivity.class.getName(), "home");

        fragmentsList.add(featured);
        fragmentsList.add(classification);
    }

    @Override
    protected void setmContentView() {
        setContentView(R.layout.activity_home);
    }
}
