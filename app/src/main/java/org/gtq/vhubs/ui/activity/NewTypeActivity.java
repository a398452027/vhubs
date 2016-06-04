package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.ClassificationItem;
import org.gtq.vhubs.dao.MovieForType;
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
import support.ui.frt.BaseFrt;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by guotengqian on 2016/6/1.
 */
public class NewTypeActivity extends VBaseActivity  implements ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener, View.OnClickListener{
    ScrollBottomLoadListView lv;

    MovieForTypeAdapter movieForTypeAdapter;
    String typeId;
    int page;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeId = getIntent().getStringExtra(BaseFrt.FRTTAG_NAME);
        setContentView(R.layout.activity_hottype);
        lv = (ScrollBottomLoadListView) findViewById(R.id.lv);
        lv.setIsAutoLoad(false);
        movieForTypeAdapter = new MovieForTypeAdapter(this, this);
        lv.setAdapter(movieForTypeAdapter);
        lv.startRun();

        lv.setOnScrollBottomListener(this);
        lv.setOnPullDownListener(this);
    }
    private void loadMoreData(){
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("category", "normalMovices", typeId,page+""));
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
                                JSONArray jsonArray = bean.getJSONArray("data");
                                List<MovieForType> list = new ArrayList<MovieForType>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MovieForType movieForType = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), MovieForType.class);
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
                            return new JSONObject(HttpUtils.doPost("category", "normalMovices", typeId));
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
                                page=2;
                                JSONArray jsonArray = bean.getJSONArray("data");
                                List<MovieForType> list = new ArrayList<MovieForType>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    MovieForType movieForType = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), MovieForType.class);
                                    list.add(movieForType);
                                }
                                loadDataTolist(list);
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

    private void loadDataTolist(List<MovieForType> list) throws JSONException {
        movieForTypeAdapter.replaceAll(list);
    }


    @Override
    public void onItemViewClick(View view, int position) {

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

    @Override
    public void onClick(View v) {
        if (v instanceof LinearLayout && v.getTag() instanceof ClassificationItem.HotsBean) {
            ClassificationItem.HotsBean type = (ClassificationItem.HotsBean) v.getTag();
            //hot type click
            OneTypeActivity.Launch(this, type.getId(), type.getName());
        }
    }
}
