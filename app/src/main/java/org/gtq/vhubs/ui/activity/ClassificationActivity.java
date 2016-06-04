package org.gtq.vhubs.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.nostra13.universalimageloader.core.assist.FailReason;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.ClassificationItem;
import org.gtq.vhubs.ui.adapter.ClassificationAdapter;
import org.gtq.vhubs.ui.adapter.HomeListAdapter;
import org.gtq.vhubs.utils.HttpUtils;
import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import support.ui.activity.VBaseActivity;
import support.ui.adapter.SetBaseAdapter;
import support.ui.image.GImageLoader;
import support.ui.view.PullToRefreshListView;
import support.ui.view.PulldownableListView;
import support.ui.view.ScrollBottomLoadListView;
import support.utils.SystemUtils;

/**
 * Created by guo on 2016/5/28.
 */
public class ClassificationActivity extends VBaseActivity implements ScrollBottomLoadListView.OnScrollBottomListener,
        PulldownableListView.OnPullDownListener, SetBaseAdapter.OnItemViewClickListener, View.OnClickListener {
    PullToRefreshListView lv;
    View headView;
    ClassificationAdapter classificationAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        lv = (PullToRefreshListView) findViewById(R.id.lv);

        headView = LayoutInflater.from(this).inflate(R.layout.view_classification_head, null);
        lv.addHeaderView(headView);
        classificationAdapter = new ClassificationAdapter(this, this);
        lv.setAdapter(classificationAdapter);
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
                            return new JSONObject(HttpUtils.doPost("category", "all"));
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
                                ClassificationItem classificationItem = JSON.parseObject(bean.getJSONObject("data")
                                        .toString(), ClassificationItem.class);
                                loadDataToHead(classificationItem);
                                loadDataTolist(classificationItem);
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

    private void loadDataTolist(ClassificationItem classificationItem) throws JSONException {
        classificationAdapter.replaceAll(classificationItem.getNormals());
    }

    private void loadDataToHead(ClassificationItem classificationItem) throws JSONException {
        for (int i = 0; i < classificationItem.getHots().size(); i++) {
            ClassificationItem.HotsBean hotsBean = classificationItem.getHots().get(i);
            switch (i) {
                case 0:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll01), (ImageView) headView.findViewById(R.id
                            .image01), (TextView) headView
                            .findViewById(R.id.name01), hotsBean);
                    break;
                case 1:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll02), (ImageView) headView.findViewById
                            (R.id
                                    .image02), (TextView) headView
                            .findViewById(R.id.name02), hotsBean);
                    break;
                case 2:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll03), (ImageView) headView.findViewById
                            (R.id
                                    .image03), (TextView) headView
                            .findViewById(R.id.name03), hotsBean);
                    break;
                case 3:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll04), (ImageView) headView.findViewById
                            (R.id
                                    .image04), (TextView) headView
                            .findViewById(R.id.name04), hotsBean);
                    break;
                case 4:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll05), (ImageView) headView.findViewById
                            (R.id
                                    .image05), (TextView) headView
                            .findViewById(R.id.name05), hotsBean);
                    break;
                case 5:
                    setHotItemData((LinearLayout) headView.findViewById(R.id.ll06), (ImageView) headView.findViewById
                            (R.id
                                    .image06), (TextView) headView
                            .findViewById(R.id.name06), hotsBean);
                    break;
            }
        }
    }

    private void setHotItemData(LinearLayout ll, final ImageView imageView, TextView name, ClassificationItem.HotsBean hotsBean) {
        ll.setTag(hotsBean);
        ll.setOnClickListener(this);
        name.setText(hotsBean.getName());
        VApplication.setBitmapEx(hotsBean.getLogo(), new GImageLoader.ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                imageView.setImageResource(R.mipmap.loading);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Bitmap bitmap = SystemUtils.createCircleImage(loadedImage, 180);
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }

            @Override
            public void onProgressUpdate(String imageUri, View view, int current, int total) {

            }
        });
    }

    @Override
    public void onItemViewClick(View view, int position) {
        if(view instanceof LinearLayout&&view.getTag() instanceof ClassificationItem.NormalsBean){
            ClassificationItem.NormalsBean type= (ClassificationItem.NormalsBean) view.getTag();
            //normal_type type click
            OneTypeActivity.Launch(this,type.getId(),type.getName());
        }
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

    }

    @Override
    public void onClick(View v) {
        if(v instanceof LinearLayout&&v.getTag() instanceof ClassificationItem.HotsBean){
            ClassificationItem.HotsBean type= (ClassificationItem.HotsBean) v.getTag();
            //hot type click
            OneTypeActivity.Launch(this,type.getId(),type.getName());
        }
    }
}
