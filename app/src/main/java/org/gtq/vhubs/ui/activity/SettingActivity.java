package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.utils.Function_Utility;

import support.ui.activity.VBaseActivity;

/**
 * Created by guo on 2016/6/5.
 */
public class SettingActivity extends VBaseActivity {
    LinearLayout updata;
    TextView current_ver;
    TextView about;
    @Override
    protected void setmContentView() {
        setContentView(R.layout.activity_setting);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updata=(LinearLayout)findViewById(R.id.updata);
        current_ver=(TextView)findViewById(R.id.current_ver);
        about=(TextView) findViewById(R.id.about);
        current_ver.setText(getString(R.string.setting_current_ver,Function_Utility.getVerName(this)));
        updata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Function_Utility.checkUpdate(SettingActivity.this);
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
