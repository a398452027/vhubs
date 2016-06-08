package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.videodemo.FilmDetailsAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.ClassificationItem;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HomeCarousels;
import org.gtq.vhubs.dao.HomeListItem;
import org.gtq.vhubs.ui.adapter.HomeListAdapter;
import org.gtq.vhubs.ui.adapter.HomePageAdapter;
import org.gtq.vhubs.utils.DBUtil;
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
import support.ui.view.PageIndicator;
import support.ui.view.PullToRefreshListView;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;

/**
 * Created by guo on 2016/5/28.
 */
public class FeaturedActivity extends VBaseActivity implements ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener {

    ViewPager vp;
    PageIndicator pi;
    PullToRefreshListView lv;
    View headView;
    int page = 0;

    List<HomeCarousels> carouselses;
    HomeListAdapter homeListAdapter;

    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);

        lv = (PullToRefreshListView) findViewById(R.id.lv);


        headView = LayoutInflater.from(this).inflate(R.layout.view_home_header, null);
        name = (TextView) headView.findViewById(R.id.name);
        pi = (PageIndicator) headView.findViewById(R.id.pi);
        vp = (ViewPager) headView.findViewById(R.id.vp);

        lv.addHeaderView(headView);
        homeListAdapter = new HomeListAdapter(this, this);
        lv.setAdapter(homeListAdapter);
        lv.startRun();

        lv.setOnPullDownListener(this);
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
                            return new JSONObject(HttpUtils.doPost("pageindex", "index"));
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
                                loadDataToViewPager(bean.getJSONObject("data"));
                                loadDataTolist(bean.getJSONObject("data"));
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

    private void loadDataTolist(JSONObject jsonObject) throws JSONException {
        List<HomeListItem> data = new ArrayList<>();
        HomeListItem hot = new HomeListItem();
        hot.setCname(getString(R.string.hot_movie));
        hot.setCid("-1");
        JSONArray hotArray = jsonObject.getJSONArray("hotMovices");
        for (int i = 0; i < hotArray.length(); i++) {
            HMoiveItem movicesBean = JSON.parseObject(hotArray.getJSONObject(i).toString(), HMoiveItem
                    .class);
            hot.getMovices().add(movicesBean);
        }
        data.add(hot);

        //待接口修改后使用
        JSONArray categorys = jsonObject.getJSONArray("categorys");
        for (int i = 0; i < categorys.length(); i++) {
            HomeListItem homeListItem = JSON.parseObject(categorys.getJSONObject(i).toString(), HomeListItem
                    .class);
            data.add(homeListItem);
        }
        homeListAdapter.replaceAll(data);
    }


    private void loadDataToViewPager(JSONObject jsonObject) throws JSONException {

        JSONArray array = jsonObject.getJSONArray("carousels");
        List<View> list = new ArrayList<>();
        if (array.length() > 0) {
            if (carouselses == null) {
                carouselses = new ArrayList<>();
            } else {
                carouselses.clear();
            }
        }
        for (int i = 0; i < array.length(); i++) {
            final HomeCarousels carousels = JSON.parseObject(array.getJSONObject(i).toString(), HomeCarousels.class);
            View view = LayoutInflater.from(this).inflate(R.layout.item_home_page, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.image);

            VApplication.setBitmapEx(imageView, carousels.getImg_url());

            list.add(view);
            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewpagerClick(carousels, finalI);
                }
            });
            carouselses.add(carousels);
        }
        name.setText(carouselses.get(0).getDesc());
        vp.setAdapter(new HomePageAdapter(this, list));
        pi.setPageCount(list.size());
        pi.setPageCurrent(0);

        vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                pi.setPageCurrent(position);
                name.setText(carouselses.get(position).getDesc());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void viewpagerClick(HomeCarousels carousels, int index) {
        if(carousels.getIs_movice().equals("1")){

            //电影详情
            FilmDetailsAct.Launch(this,carousels.getMovice_id(),carousels.getUrl());

        }else{
            //网页
            WebviewActivity.Launch(this,carousels.getDesc(),carousels.getUrl());
        }
    }

    @Override
    public void onBottomLoad(ScrollBottomLoadListView listView) {

    }

    @Override
    public void onStartRun(PulldownableListView view) {
        loadDataFornet();
    }


    @Override
    public void onItemViewClick(View view, int position) {
        if (view instanceof LinearLayout && view.getTag() instanceof HomeListItem) {

            HomeListItem type = (HomeListItem) view.getTag();
            if (type.getCid().equals("-1")) {
                //热门影片列表界面
                AllMoiveForHotActivity.Launch(this);

            } else {
                OneTypeActivity.Launch(this, type.getCid(), type.getCname());
            }
        } else if (view instanceof RelativeLayout && view.getTag() instanceof HMoiveItem) {
            //收藏测试
//            DBUtil.addToFavorites((HMoiveItem) view.getTag());
            //记录测试
            DBUtil.addToHistory((HMoiveItem) view.getTag());

            //影片点击


        }
    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }
}
