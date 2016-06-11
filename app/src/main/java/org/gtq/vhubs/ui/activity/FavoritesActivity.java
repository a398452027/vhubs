package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.videodemo.FilmDetailsAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.ui.adapter.HMoiveAdapter;
import org.gtq.vhubs.utils.HttpUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
        adapter = new HMoiveAdapter<FavoritesMoive>(this, this);
        lv.setAdapter(adapter);

    }

    @Override
    public void Resume() {
        super.Resume();
        loadForDb();
    }

    private void sort(List<FavoritesMoive> list) {
        Collections.sort(list, new Comparator<FavoritesMoive>() {
            @Override
            public int compare(FavoritesMoive lhs, FavoritesMoive rhs) {
                if (lhs.getSave_time() > rhs.getSave_time()) {
                    return -1;
                } else if (lhs.getSave_time() < rhs.getSave_time()) {
                    return 1;
                }
                return 0;
            }
        });
    }

    private void loadForDb() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, List<FavoritesMoive>>() {
                    @Override
                    public List<FavoritesMoive> call(String s) {


                        try {

                            return XDB.getInstance().readAll(FavoritesMoive.class, false);
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<FavoritesMoive>>() {
                    @Override
                    public void call(List<FavoritesMoive> list) {
                        pb_ll.setVisibility(View.GONE);
                        if (list == null || list.size() == 0) {
                            fail_tv.setVisibility(View.VISIBLE);
                        } else {
                            sort(list);
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
        FilmDetailsAct.Launch(this,((HMoiveItem) view.getTag()).getId(),((HMoiveItem) view.getTag()).getVedio_url());
    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }
}
