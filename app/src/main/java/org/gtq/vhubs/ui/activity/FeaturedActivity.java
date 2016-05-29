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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.gtq.vhubs.R;
import org.gtq.vhubs.dao.HomeListItem;
import org.gtq.vhubs.utils.HttpUtils;

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
 * Created by guo on 2016/5/28.
 */
public class FeaturedActivity extends VBaseActivity implements ScrollBottomLoadListView.OnScrollBottomListener, PulldownableListView.OnPullDownListener {

    ViewPager vp;
    ScrollBottomLoadListView lv;
    View headView;
    int page = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured);

        lv = (ScrollBottomLoadListView) findViewById(R.id.lv);
        headView = LayoutInflater.from(this).inflate(R.layout.view_home_header, null);
        vp = (ViewPager) headView.findViewById(R.id.vp);
        lv.addHeaderView(headView);
        lv.setAdapter(new MoveAdapter());

        lv.startRun();

        lv.setOnScrollBottomListener(this);
        lv.setOnPullDownListener(this);
    }

    private void loadDataFornet() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {


                        try {
                            return HttpUtils.doPost("pageindex", "index");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String bean) {
                        lv.endRun();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });

    }

    @Override
    public void onBottomLoad(ScrollBottomLoadListView listView) {

    }

    @Override
    public void onStartRun(PulldownableListView view) {
        loadDataFornet();
    }


    public class MoveAdapter extends SetBaseAdapter<HomeListItem> {

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(FeaturedActivity.this).inflate(R.layout.item_home_movie, null);
//            holder.iv = (ImageView) convertView.findViewById(R.id.hashnest_powermain_item_iv);
//            holder.name = (TextView) convertView.findViewById(R.id.hashnest_powermain_item_name_tv);
//            holder.power = (TextView) convertView.findViewById(R.id.hashnest_powermain_item_power_tv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

        public class ViewHolder {


        }
    }


}
