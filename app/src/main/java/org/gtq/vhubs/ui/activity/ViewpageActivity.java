package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import org.gtq.vhubs.R;
import org.gtq.vhubs.ui.adapter.XFragmentAdapter;

import java.util.ArrayList;

import support.ui.activity.VBaseActivity;
import support.ui.frt.BaseFrtFactory;

/**
 * Created by guo on 2016/5/28.
 */
public abstract class ViewpageActivity extends VBaseActivity {

    protected RelativeLayout tab_0;
    protected RelativeLayout tab_1;
    protected ViewPager vp;
    protected ImageView select_iv0;
    protected ImageView select_iv1;

    protected ArrayList<Fragment> fragmentsList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tab_0 = (RelativeLayout) findViewById(R.id.tab_0);
        tab_1 = (RelativeLayout) findViewById(R.id.tab_1);
        vp = (ViewPager) findViewById(R.id.vp);
        select_iv0 = (ImageView) findViewById(R.id.select_iv0);
        select_iv1 = (ImageView) findViewById(R.id.select_iv1);

        initFragment();
        // 给ViewPager添加适配器
        vp.setAdapter(new XFragmentAdapter(
                getSupportFragmentManager(), fragmentsList));
        // 设置默认的视图为第1个
        vp.setCurrentItem(0);
        // 给Viewpager添加监听事件
        vp.setOnPageChangeListener(initOnPageChangeListener());
        tab_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(0);
            }
        });
        tab_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1);
            }
        });
    }

    protected  ViewPager.OnPageChangeListener initOnPageChangeListener(){
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                switch (position){
                    case 0:
                        select_iv0.getBackground().setAlpha((int)(255*(1-positionOffset)));
                        select_iv1.getBackground().setAlpha((int)(255*positionOffset));
                        break;
                    case 1:
                        select_iv0.getBackground().setAlpha((int)(255*positionOffset));
                        select_iv1.getBackground().setAlpha((int)(255*(1-positionOffset)));

                        break;
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
    }

    protected abstract void initFragment();
}
