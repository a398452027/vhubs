package support.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import org.gtq.vhubs.R;

import support.ui.frt.ActivityToFragmentInterface;
import support.utils.SystemUtils;

/**
 * Created by guo on 2016/5/15.
 */
public class VBaseActivity extends AppCompatActivity implements ActivityToFragmentInterface {
    View status_bar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status_bar = findViewById(R.id.status_bar);
        if (status_bar != null) {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) status_bar.getLayoutParams();
            lp.height = SystemUtils.getStatusBarHeight(this);
            status_bar.setLayoutParams(lp);
        }
    }

    @Override
    public void setmActionBar(ActionBar mActionBar) {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void activityResult(int requestCode, int resultCode, Intent data) {
        onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void SaveInstanceState(Bundle outState) {

    }

    @Override
    public void initInstanceState(Bundle outState) {

    }
}
