package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.ui.adapter.HMoiveAdapter;
import org.gtq.vhubs.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import support.db.XDB;
import support.ui.activity.VBaseActivity;
import support.ui.adapter.SetBaseAdapter;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by guo on 2016/6/5.
 */
public class FavoritesActivity extends VBaseActivity implements View.OnClickListener,
        SetBaseAdapter.OnItemViewClickListener {

    HMoiveAdapter adapter;
    LinearLayout pb_ll;
    TextView fail_tv;
    ListView lv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        pb_ll = (LinearLayout) findViewById(R.id.pb_ll);
        fail_tv = (TextView) findViewById(R.id.fail_tv);
        lv = (ListView) findViewById(R.id.lv);
        adapter = new HMoiveAdapter(this, this);
        lv.setAdapter(adapter);
        loadForDb();
    }

    private  void loadForDb(){
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, List<HMoiveItem>>() {
                    @Override
                    public  List<HMoiveItem> call(String s) {


                        try {

                            return XDB.getInstance().readAll(HMoiveItem.class,false);
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1< List<HMoiveItem>>() {
                    @Override
                    public void call( List<HMoiveItem> list) {
                        pb_ll.setVisibility(View.GONE);
                        if(list==null||list.size()==0){
                            fail_tv.setVisibility(View.VISIBLE);
                        }else{
                            fail_tv.setVisibility(View.GONE);
                            lv.setVisibility(View.VISIBLE);
                            adapter.replaceAll(list);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        VApplication.toast(getString(R.string.net_fail));
                    }
                });
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onItemViewClick(View view, int position) {

    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }
}
