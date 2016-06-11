package org.gtq.vhubs.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.videodemo.FilmDetailsAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.ui.adapter.MovieForTypeAdapter;
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
import support.ui.activity.VBaseActivity;
import support.ui.adapter.SetBaseAdapter;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by gtq on 2016/6/8.
 */
public class AllMoiveForHotActivity extends VBaseActivity implements ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener, View.OnClickListener {

    ScrollBottomLoadListView lv;
    MovieForTypeAdapter movieForTypeAdapter;
    int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allmoiveforhot);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.hot_movie));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        lv = (ScrollBottomLoadListView) findViewById(R.id.lv);
        movieForTypeAdapter = new MovieForTypeAdapter(this, this);
        lv.setAdapter(movieForTypeAdapter);
        lv.startRun();
        lv.setIsAutoLoad(true);
        lv.setOnScrollBottomListener(this);
        lv.setOnPullDownListener(this);
    }

    private void loadMoreData() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("movice", "listAll", page + ""));
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject bean) {
                        lv.setVisibility(View.VISIBLE);
                        lv.endRun();
                        try {
                            if (bean.getInt("code") != 0) {
                                VApplication.toastJsonError(bean);
                            } else {
                                page++;
                                JSONArray jsonArray = bean.getJSONObject("data").getJSONArray("movices");
                                lv.hasMoreLoad(bean.getJSONObject("data").getBoolean("hasMore"));
                                List<HMoiveItem> list = new ArrayList<HMoiveItem>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HMoiveItem movieForType = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), HMoiveItem.class);
                                    list.add(movieForType);
                                }
                                movieForTypeAdapter.addAll(list);
                            }
                        } catch (JSONException e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        VApplication.toast(getString(R.string.net_fail));
                    }
                });
    }

    private void loadDataFornet() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("movice", "listAll"));
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject bean) {
                        lv.setVisibility(View.VISIBLE);
                        lv.endRun();
                        try {
                            if (bean.getInt("code") != 0) {
                                VApplication.toastJsonError(bean);
                            } else {
                                page = 2;
                                JSONArray jsonArray = bean.getJSONObject("data").getJSONArray("movices");
                                lv.hasMoreLoad(bean.getJSONObject("data").getBoolean("hasMore"));
                                List<HMoiveItem> list = new ArrayList<HMoiveItem>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HMoiveItem movieForType = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), HMoiveItem.class);
                                    list.add(movieForType);
                                }
                                movieForTypeAdapter.replaceAll(list);
                            }
                        } catch (JSONException e) {
                            VApplication.toast(getString(R.string.net_fail));
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void Launch(Activity activity) {
        Intent intent = new Intent(activity, AllMoiveForHotActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemViewClick(View view, int position) {
        FilmDetailsAct.Launch(this,((HMoiveItem) view.getTag()).getId(),((HMoiveItem) view.getTag()).getVedio_url());
    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }

    @Override
    public void onStartRun(PulldownableListView view) {
        loadDataFornet();
    }

    @Override
    public void onBottomLoad(ScrollBottomLoadListView listView) {
        loadMoreData();
    }
}
