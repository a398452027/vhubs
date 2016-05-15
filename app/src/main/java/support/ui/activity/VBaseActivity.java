package support.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import support.ui.frt.ActivityToFragmentInterface;

/**
 * Created by guo on 2016/5/15.
 */
public class VBaseActivity extends AppCompatActivity implements ActivityToFragmentInterface {
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
