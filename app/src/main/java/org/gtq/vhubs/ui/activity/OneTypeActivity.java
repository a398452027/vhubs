package org.gtq.vhubs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import org.gtq.vhubs.R;

import java.util.ArrayList;

import support.ui.frt.BaseFrtFactory;
import support.utils.SystemUtils;

/**
 * Created by guotengqian on 2016/6/1.
 */
public class OneTypeActivity extends ViewpageActivity {
    String id;
    View status_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        String name = intent.getStringExtra("type_name");
        id = intent.getStringExtra("type_id");
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        select_iv0.getBackground().setAlpha(255);
        select_iv1.getBackground().setAlpha(0);

        status_bar = findViewById(R.id.status_bar);
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) status_bar.getLayoutParams();
        lp.height = SystemUtils.getStatusBarHeight(this);
        status_bar.setLayoutParams(lp);
    }

    @Override
    protected void setmContentView() {
        setContentView(R.layout.activity_onetype);
    }

    @Override
    protected void initFragment() {
        // Fragment容器
        fragmentsList = new ArrayList<Fragment>();
        // 生成每个tab对应的fragment
        Fragment hotType = BaseFrtFactory.createForActivityView(
                HotTypeActivity.class.getName(), id);
        Fragment newType = BaseFrtFactory.createForActivityView(
                NewTypeActivity.class.getName(), id);

        fragmentsList.add(hotType);
        fragmentsList.add(newType);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void Launch(Activity activity, String id, String name) {
        Intent intent = new Intent(activity, OneTypeActivity.class);
        intent.putExtra("type_id", id);
        intent.putExtra("type_name", name);
        activity.startActivity(intent);
    }
}
