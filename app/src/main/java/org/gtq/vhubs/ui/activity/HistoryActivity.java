package org.gtq.vhubs.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.videodemo.FilmDetailsAct;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HistoryMoive;
import org.gtq.vhubs.ui.adapter.HMoiveAdapter;
import org.gtq.vhubs.ui.adapter.HistoryAdapter;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import support.db.XDB;
import support.ui.activity.VBaseActivity;
import support.ui.adapter.SetBaseAdapter;

/**
 * Created by guo on 2016/6/5.
 */
public class HistoryActivity extends VBaseActivity implements SetBaseAdapter.OnItemViewClickListener{

    LinearLayout pb_ll;
    TextView fail_tv;
    StickyListHeadersListView lv;
    HistoryAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        pb_ll = (LinearLayout) findViewById(R.id.pb_ll);
        fail_tv = (TextView) findViewById(R.id.fail_tv);
        lv = (StickyListHeadersListView) findViewById(R.id.lv);
        adapter = new HistoryAdapter(this,this);
        lv.setAdapter(adapter);
        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (adapter.getCount() != 0) {
                    SimpleDateFormat sf = new SimpleDateFormat("dd");
                    TextView day = (TextView) findViewById(R.id.day);
                    String dayStr = sf.format(new Date(((HistoryMoive) adapter.getItem(firstVisibleItem)).getWatch_time()));
                    day.setText(dayStr);
                }
            }
        });
    }

    @Override
    public void Resume() {
        super.Resume();
        loadForDb();
    }

    private void sort(List<HistoryMoive> list) {
        Collections.sort(list, new Comparator<HistoryMoive>() {
            @Override
            public int compare(HistoryMoive lhs, HistoryMoive rhs) {
                if (lhs.getWatch_time() > rhs.getWatch_time()) {
                    return -1;
                } else if (lhs.getWatch_time() < rhs.getWatch_time()) {
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
                .map(new Func1<String, List<HistoryMoive>>() {
                    @Override
                    public List<HistoryMoive> call(String s) {


                        try {

                            return XDB.getInstance().readAll(HistoryMoive.class, false);
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HistoryMoive>>() {
                    @Override
                    public void call(List<HistoryMoive> list) {

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
    public void onItemViewClick(View view, int position) {
        FilmDetailsAct.Launch(this,((HMoiveItem) view.getTag()).getId(),((HMoiveItem) view.getTag()).getVedio_url());
    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }
}
