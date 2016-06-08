package org.gtq.vhubs.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HotKey;
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
import support.ui.activity.VBaseActivity;
import support.ui.adapter.SetBaseAdapter;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by guo on 2016/6/4.
 */
public class SearchResultActivity extends VBaseActivity implements View.OnClickListener, ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener {
    EditText edit;

    ImageView back;
    ImageView search;

    String key;
    ScrollBottomLoadListView lv;
    HMoiveAdapter adapter;
    int page = 1;
    TextView fail_tv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        key = getIntent().getStringExtra("key");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchresult);
        initView();


    }

    private void initView() {

        fail_tv = (TextView) findViewById(R.id.fail_tv);

        edit = (EditText) findViewById(R.id.edit);
        edit.setText(key);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        lv = (ScrollBottomLoadListView) findViewById(R.id.lv);
        lv.startRun();
        adapter = new HMoiveAdapter<HMoiveItem>(this, this);
        lv.setAdapter(adapter);
        lv.setOnScrollBottomListener(this);
        lv.setOnPullDownListener(this);

    }

    private void searchMore() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {

                            return new JSONObject(HttpUtils.doPost("movice", "search", edit.getText().toString(), page + ""));
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
                        lv.endLoad();
                        try {
                            if (bean.getInt("code") != 0) {
                                VApplication.toastJsonError(bean);
                            } else {
                                page++;
                                JSONArray jsonArray = bean.getJSONObject("data").getJSONArray("movices");
                                lv.hasMoreLoad(bean.getJSONObject("data").getBoolean("hasMore"));
                                List<HMoiveItem> list = new ArrayList<HMoiveItem>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HMoiveItem hMoiveItem = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), HMoiveItem.class);
                                    list.add(hMoiveItem);
                                }

                                adapter.addAll(list);
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

    private void search() {

            com.alibaba.fastjson.JSONArray jsonArray =com.alibaba.fastjson.JSONArray.parseArray(getSharedPreferences("sp", Context.MODE_PRIVATE).getString(SearchActivity.SP_SEARCH, "[]"));

            for(int i=0;i<jsonArray.size();i++){
                if(jsonArray.getString(i).equals(edit.getText().toString())){
                    jsonArray.remove(i);
                }
            }
            jsonArray.add(edit.getText().toString());
            getSharedPreferences("sp", Context.MODE_PRIVATE).edit().putString(SearchActivity.SP_SEARCH, jsonArray.toString()).commit();


        adapter.clear();
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("movice", "search", edit.getText().toString()));
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

                                JSONArray jsonArray = bean.getJSONObject("data").getJSONArray("movices");
                                lv.hasMoreLoad(bean.getJSONObject("data").getBoolean("hasMore"));
                                List<HMoiveItem> list = new ArrayList<HMoiveItem>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HMoiveItem hMoiveItem = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), HMoiveItem.class);
                                    list.add(hMoiveItem);
                                }

                                if (list.size() == 0) {
                                    fail_tv.setVisibility(View.VISIBLE);
                                } else {
                                    page = 2;
                                    fail_tv.setVisibility(View.GONE);
                                    adapter.replaceAll(list);
                                }

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

    public static void Launch(Activity activity, String key) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra("key", key);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            super.onBackPressed();
        } else if (view.getId() == R.id.search) {
            search();
        }
    }

    @Override
    public void onItemViewClick(View view, int position) {

    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }

    @Override
    public void onStartRun(PulldownableListView view) {
        search();
    }

    @Override
    public void onBottomLoad(ScrollBottomLoadListView listView) {
        searchMore();
    }
}
