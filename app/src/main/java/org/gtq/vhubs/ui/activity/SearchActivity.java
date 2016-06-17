package org.gtq.vhubs.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HotKey;
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
import support.utils.SystemUtils;

/**
 * Created by guo on 2016/6/4.
 */
public class SearchActivity extends VBaseActivity implements View.OnClickListener {

    public static final String SP_SEARCH = "sp_search";

    EditText edit;

    ImageView back;
    ImageView search;
    ImageView clear;

    TextView history_tv01;
    TextView history_tv02;
    TextView history_tv03;
    TextView history_tv04;
    TextView history_tv05;
    TextView history_tv06;

    TextView hot_tv01;
    TextView hot_tv02;
    TextView hot_tv03;
    TextView hot_tv04;
    TextView hot_tv05;
    TextView hot_tv06;

    LinearLayout history_title;

    @Override
    protected void setmContentView() {
        setContentView(R.layout.activity_search);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();


    }

    private void initView() {
        edit = (EditText) findViewById(R.id.edit);
        edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.KEYCODE_SEARCH) {
                    SearchResultActivity.Launch(SearchActivity.this, textView.getText().toString());
                    finish();
                }
                return false;
            }
        });

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(this);
        clear = (ImageView) findViewById(R.id.clear);
        clear.setOnClickListener(this);

        history_title = (LinearLayout) findViewById(R.id.history_title);

        history_tv01 = (TextView) findViewById(R.id.history_tv01);
        history_tv01.setOnClickListener(this);
        history_tv02 = (TextView) findViewById(R.id.history_tv02);
        history_tv02.setOnClickListener(this);
        history_tv03 = (TextView) findViewById(R.id.history_tv03);
        history_tv03.setOnClickListener(this);
        history_tv04 = (TextView) findViewById(R.id.history_tv04);
        history_tv04.setOnClickListener(this);
        history_tv05 = (TextView) findViewById(R.id.history_tv05);
        history_tv05.setOnClickListener(this);
        history_tv06 = (TextView) findViewById(R.id.history_tv06);
        history_tv06.setOnClickListener(this);


        initHistorySearch();

        hot_tv01 = (TextView) findViewById(R.id.hot_tv01);
        hot_tv01.setOnClickListener(this);
        hot_tv02 = (TextView) findViewById(R.id.hot_tv02);
        hot_tv02.setOnClickListener(this);
        hot_tv03 = (TextView) findViewById(R.id.hot_tv03);
        hot_tv03.setOnClickListener(this);
        hot_tv04 = (TextView) findViewById(R.id.hot_tv04);
        hot_tv04.setOnClickListener(this);
        hot_tv05 = (TextView) findViewById(R.id.hot_tv05);
        hot_tv05.setOnClickListener(this);
        hot_tv06 = (TextView) findViewById(R.id.hot_tv06);
        hot_tv06.setOnClickListener(this);
        loadHotKeyForNet();
    }
    private void initHistorySearch(){
        try {
            JSONArray jsonArray = new JSONArray(getSharedPreferences("sp", Context.MODE_PRIVATE).getString(SP_SEARCH, "[]"));
            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                if (i == jsonArray.length() - 1) {
                    history_tv01.setText(jsonArray.getString(i));
                } else if (i == jsonArray.length() - 2) {
                    history_tv02.setText(jsonArray.getString(i));
                } else if (i == jsonArray.length() - 3) {
                    history_tv03.setText(jsonArray.getString(i));
                } else if (i == jsonArray.length() - 4) {
                    history_tv04.setText(jsonArray.getString(i));
                } else if (i == jsonArray.length() - 5) {
                    history_tv05.setText(jsonArray.getString(i));
                } else if (i == jsonArray.length() - 6) {
                    history_tv06.setText(jsonArray.getString(i));
                }
            }

            switch (jsonArray.length()) {
                case 0:
                    history_title.setVisibility(View.GONE);
                    history_tv01.setVisibility(View.GONE);
                    history_tv02.setVisibility(View.GONE);
                    history_tv03.setVisibility(View.GONE);
                    history_tv04.setVisibility(View.GONE);
                    history_tv05.setVisibility(View.GONE);
                    history_tv06.setVisibility(View.GONE);
                    break;
                case 1:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.INVISIBLE);
                    history_tv03.setVisibility(View.GONE);
                    history_tv04.setVisibility(View.GONE);
                    history_tv05.setVisibility(View.GONE);
                    history_tv06.setVisibility(View.GONE);
                    break;
                case 2:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.VISIBLE);
                    history_tv03.setVisibility(View.GONE);
                    history_tv04.setVisibility(View.GONE);
                    history_tv05.setVisibility(View.GONE);
                    history_tv06.setVisibility(View.GONE);
                    break;
                case 3:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.VISIBLE);
                    history_tv03.setVisibility(View.VISIBLE);
                    history_tv04.setVisibility(View.INVISIBLE);
                    history_tv05.setVisibility(View.GONE);
                    history_tv06.setVisibility(View.GONE);
                    break;
                case 4:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.VISIBLE);
                    history_tv03.setVisibility(View.VISIBLE);
                    history_tv04.setVisibility(View.VISIBLE);
                    history_tv05.setVisibility(View.GONE);
                    history_tv06.setVisibility(View.GONE);
                    break;
                case 5:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.VISIBLE);
                    history_tv03.setVisibility(View.VISIBLE);
                    history_tv04.setVisibility(View.VISIBLE);
                    history_tv05.setVisibility(View.VISIBLE);
                    history_tv06.setVisibility(View.INVISIBLE);
                    break;
                default:
                    history_title.setVisibility(View.VISIBLE);
                    history_tv01.setVisibility(View.VISIBLE);
                    history_tv02.setVisibility(View.VISIBLE);
                    history_tv03.setVisibility(View.VISIBLE);
                    history_tv04.setVisibility(View.VISIBLE);
                    history_tv05.setVisibility(View.VISIBLE);
                    history_tv06.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (JSONException e) {

        }
    }

    private void loadHotKeyForNet() {
        Observable.just("").doOnSubscribe(new Action0() {
            @Override
            public void call() {

            }
        }).observeOn(Schedulers.io())
                .map(new Func1<String, JSONObject>() {
                    @Override
                    public JSONObject call(String s) {


                        try {
                            return new JSONObject(HttpUtils.doPost("hotSearch", "listAll"));
                        } catch (Exception e) {
                            VApplication.toast(getString(R.string.net_fail));
                        }
                        return null;
                    }
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<JSONObject>() {
                    @Override
                    public void call(JSONObject bean) {

                        try {
                            if (bean.getInt("code") != 0) {
                                VApplication.toastJsonError(bean);
                                hot_tv01.setVisibility(View.GONE);
                                hot_tv02.setVisibility(View.GONE);
                                hot_tv03.setVisibility(View.GONE);
                                hot_tv04.setVisibility(View.GONE);
                                hot_tv05.setVisibility(View.GONE);
                                hot_tv06.setVisibility(View.GONE);
                            } else {
                                JSONArray jsonArray = bean.getJSONArray("data");
                                List<HotKey> list = new ArrayList<HotKey>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    HotKey hotKey = JSON.parseObject(jsonArray.getJSONObject(i)
                                            .toString(), HotKey.class);
                                    list.add(hotKey);
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    if (i == 0) {
                                        hot_tv01.setText(list.get(i).getSearch_txt());
                                    } else if (i == 1) {
                                        hot_tv02.setText(list.get(i).getSearch_txt());
                                    } else if (i == 2) {
                                        hot_tv03.setText(list.get(i).getSearch_txt());
                                    } else if (i == 3) {
                                        hot_tv04.setText(list.get(i).getSearch_txt());
                                    } else if (i == 4) {
                                        hot_tv05.setText(list.get(i).getSearch_txt());
                                    } else if (i == 5) {
                                        hot_tv06.setText(list.get(i).getSearch_txt());
                                    }
                                }

                                switch (list.size()) {
                                    case 0:
                                        hot_tv01.setVisibility(View.GONE);
                                        hot_tv02.setVisibility(View.GONE);
                                        hot_tv03.setVisibility(View.GONE);
                                        hot_tv04.setVisibility(View.GONE);
                                        hot_tv05.setVisibility(View.GONE);
                                        hot_tv06.setVisibility(View.GONE);
                                        break;
                                    case 1:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.INVISIBLE);
                                        hot_tv03.setVisibility(View.GONE);
                                        hot_tv04.setVisibility(View.GONE);
                                        hot_tv05.setVisibility(View.GONE);
                                        hot_tv06.setVisibility(View.GONE);
                                        break;
                                    case 2:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.VISIBLE);
                                        hot_tv03.setVisibility(View.GONE);
                                        hot_tv04.setVisibility(View.GONE);
                                        hot_tv05.setVisibility(View.GONE);
                                        hot_tv06.setVisibility(View.GONE);
                                        break;
                                    case 3:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.VISIBLE);
                                        hot_tv03.setVisibility(View.VISIBLE);
                                        hot_tv04.setVisibility(View.INVISIBLE);
                                        hot_tv05.setVisibility(View.GONE);
                                        hot_tv06.setVisibility(View.GONE);
                                        break;
                                    case 4:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.VISIBLE);
                                        hot_tv03.setVisibility(View.VISIBLE);
                                        hot_tv04.setVisibility(View.VISIBLE);
                                        hot_tv05.setVisibility(View.GONE);
                                        hot_tv06.setVisibility(View.GONE);
                                        break;
                                    case 5:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.VISIBLE);
                                        hot_tv03.setVisibility(View.VISIBLE);
                                        hot_tv04.setVisibility(View.VISIBLE);
                                        hot_tv05.setVisibility(View.VISIBLE);
                                        hot_tv06.setVisibility(View.INVISIBLE);
                                        break;
                                    default:
                                        hot_tv01.setVisibility(View.VISIBLE);
                                        hot_tv02.setVisibility(View.VISIBLE);
                                        hot_tv03.setVisibility(View.VISIBLE);
                                        hot_tv04.setVisibility(View.VISIBLE);
                                        hot_tv05.setVisibility(View.VISIBLE);
                                        hot_tv06.setVisibility(View.VISIBLE);
                                        break;
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

    public static void Launch(Activity activity) {
        Intent intent = new Intent(activity, SearchActivity.class);
        activity.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            super.onBackPressed();
        } else if (view.getId() == R.id.search) {
            String key = edit.getText().toString();
            if (!TextUtils.isEmpty(key)) {
                SearchResultActivity.Launch(this, key);
                finish();
            }
        } else if (view.getId() == R.id.clear) {
            getSharedPreferences("sp", Context.MODE_PRIVATE).edit().putString(SP_SEARCH,"[]").commit();
            initHistorySearch();
        } else if (view instanceof TextView) {
            SearchResultActivity.Launch(this, ((TextView) view).getText().toString());
            finish();
        }
    }


}
