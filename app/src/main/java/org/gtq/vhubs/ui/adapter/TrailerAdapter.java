package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.ClassificationItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import support.ui.adapter.SetBaseAdapter;
import support.ui.image.GImageLoader;
import support.utils.SystemUtils;

/**
 * Created by guo on 2016/6/11.
 */
public class TrailerAdapter extends SetBaseAdapter<String> {

    OnItemViewClickListener onItemViewClickListener;
    Context context;

    ArrayList<List<String>> oList;


    public TrailerAdapter(Context context, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
        oList = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_trailer, null);

            holder.ll01 = (LinearLayout) convertView.findViewById(R.id.ll01);
            holder.ll02 = (LinearLayout) convertView.findViewById(R.id.ll02);
            holder.ll03 = (LinearLayout) convertView.findViewById(R.id.ll03);

            holder.image01 = (ImageView) convertView.findViewById(R.id.image01);
            holder.image02 = (ImageView) convertView.findViewById(R.id.image02);
            holder.image03 = (ImageView) convertView.findViewById(R.id.image03);

            holder.name01 = (TextView) convertView.findViewById(R.id.name01);
            holder.name02 = (TextView) convertView.findViewById(R.id.name02);
            holder.name03 = (TextView) convertView.findViewById(R.id.name03);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<String> list = (List<String>) getItem(position);
        for (int i = 0; i < list.size(); i++) {
            String url = list.get(i);
            switch (i) {
                case 0:
                    setData(holder.ll01, holder.image01, holder.name01, url, position);
                    break;
                case 1:
                    setData(holder.ll02, holder.image02, holder.name02, url, position);
                    break;
                case 2:
                    setData(holder.ll03, holder.image03, holder.name03, url, position);
                    break;
            }
        }

        return convertView;
    }

    private void setData(LinearLayout ll, final ImageView imageView, TextView name, String url, final int p) {
        VApplication.setBitmapEx(imageView,url,R.mipmap.loading);
    }

    @Override
    public Object getItem(int position) {


        return oList.get(position);
    }


    @Override
    public void replaceAll(Collection<String> collection) {
        oList.clear();
        super.replaceAll(collection);

        initOList(mListObject);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Collection<String> collection) {

        super.addAll(collection);
        initOList(mListObject);
        notifyDataSetChanged();
    }

    private void initOList(List<String> collection) {
        for (int i = 0; i < collection.size(); ) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < 3; j++) {
                list.add(collection.get(i));
                i++;
                if (i == collection.size()) {
                    break;
                }
            }
            oList.add(list);
        }
    }

    @Override
    public int getCount() {
        return oList.size();
    }


    public class ViewHolder {

        LinearLayout ll01;
        ImageView image01;
        TextView name01;

        LinearLayout ll02;
        ImageView image02;
        TextView name02;

        LinearLayout ll03;
        ImageView image03;
        TextView name03;

    }
}
