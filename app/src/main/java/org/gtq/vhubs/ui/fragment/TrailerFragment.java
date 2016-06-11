package org.gtq.vhubs.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.ui.adapter.HMoiveAdapter;
import org.gtq.vhubs.ui.adapter.TrailerAdapter;

import java.util.List;

import support.ui.adapter.SetBaseAdapter;

/**
 * Created by gtq on 2016/6/8.
 */
public class TrailerFragment extends Fragment implements View.OnClickListener,
        SetBaseAdapter.OnItemViewClickListener {
    LinearLayout pb_ll;
    TextView fail_tv;
    ListView lv;
    TrailerAdapter adapter;
    List<String> urls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trailer, null);
        pb_ll = (LinearLayout) view.findViewById(R.id.pb_ll);
        fail_tv = (TextView) view.findViewById(R.id.fail_tv);
        lv = (ListView) view.findViewById(R.id.lv);
        adapter = new TrailerAdapter(getActivity(), this);
        lv.setAdapter(adapter);

        return view;


    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
        pb_ll.setVisibility(View.GONE);
        if (urls == null || urls.size() == 0) {
            fail_tv.setVisibility(View.VISIBLE);
        } else {
            fail_tv.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            adapter.replaceAll(urls);
        }


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onItemViewClick(View view, int position) {

    }

    @Override
    public void onItemViewLongClick(View view, int position) {

    }
}
