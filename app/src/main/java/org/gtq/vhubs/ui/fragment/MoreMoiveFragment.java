package org.gtq.vhubs.ui.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.videodemo.FilmDetailsAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.ui.activity.SearchActivity;
import org.gtq.vhubs.ui.adapter.HMoiveAdapter;
import org.gtq.vhubs.ui.adapter.TrailerAdapter;
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
import support.ui.adapter.SetBaseAdapter;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by gtq on 2016/6/8.
 */
public class MoreMoiveFragment extends Fragment implements View.OnClickListener, ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener{

    ScrollBottomLoadListView lv;
    HMoiveAdapter adapter;
    int page = 1;
    TextView fail_tv;
    String category_id;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_moremoive, null);
        fail_tv = (TextView) view.findViewById(R.id.fail_tv);
        lv = (ScrollBottomLoadListView) view.findViewById(R.id.lv);
        lv.startRun();
        adapter = new HMoiveAdapter<HMoiveItem>(getActivity(), this);
        lv.setAdapter(adapter);
        lv.setOnScrollBottomListener(this);
        lv.setOnPullDownListener(this);

        return view;


    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
        lv.setVisibility(View.VISIBLE);
        fail_tv.setVisibility(View.GONE);
        lv.startRun();
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

                            return new JSONObject(HttpUtils.doPost("movice", "aboutMovices",category_id, page + ""));
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

    private void loadMoreMoive() {

        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("movice", "aboutMovices",category_id));
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


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemViewClick(View view, int position) {
        getActivity().finish();
        FilmDetailsAct.Launch(getActivity(),((HMoiveItem) view.getTag()).getId(),((HMoiveItem) view.getTag()).getVedio_url());
    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }

    @Override
    public void onStartRun(PulldownableListView view) {
        loadMoreMoive();
    }

    @Override
    public void onBottomLoad(ScrollBottomLoadListView listView) {
        loadMoreData();
    }
}
