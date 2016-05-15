package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import org.gtq.vhubs.R;

import support.ui.activity.VBaseActivity;

/**
 * Created by guo on 2016/5/15.
 */
public class HomeActivity extends VBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
