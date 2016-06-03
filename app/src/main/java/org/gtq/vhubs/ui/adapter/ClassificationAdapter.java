package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * Created by guotengqian on 2016/6/1.
 */
public class ClassificationAdapter extends SetBaseAdapter<ClassificationItem.NormalsBean> {

    OnItemViewClickListener onItemViewClickListener;
    Context context;

    ArrayList<List<ClassificationItem.NormalsBean>> oList;


    public ClassificationAdapter(Context context, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
        oList = new ArrayList<>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_classification, null);

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
        List<ClassificationItem.NormalsBean> list = (List<ClassificationItem.NormalsBean>) getItem(position);
        for (int i = 0; i < list.size(); i++) {
            ClassificationItem.NormalsBean normalsBean = list.get(i);
            switch (i) {
                case 0:
                    setData(holder.ll01, holder.image01, holder.name01, normalsBean, position);
                    break;
                case 1:
                    setData(holder.ll02, holder.image02, holder.name02, normalsBean, position);
                    break;
                case 2:
                    setData(holder.ll03, holder.image03, holder.name03, normalsBean, position);
                    break;
            }
        }
        switch (list.size()){
            case 1:
                holder.ll02.setVisibility(View.GONE);
                holder.ll03.setVisibility(View.GONE);
                break;
            case 2:
                holder.ll02.setVisibility(View.VISIBLE);
                holder.ll03.setVisibility(View.GONE);
                break;
            case 3:
                holder.ll02.setVisibility(View.VISIBLE);
                holder.ll03.setVisibility(View.VISIBLE);
                break;
        }
        return convertView;
    }

    private void setData(LinearLayout ll, final ImageView imageView, TextView name, ClassificationItem.NormalsBean normalsBean, final int p) {

        ll.setTag(normalsBean);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClickListener.onItemViewClick(v, p);
            }
        });
        name.setText(normalsBean.getName());
        VApplication.setBitmapEx(normalsBean.getLogo(), new GImageLoader.ImageLoadingListener() {
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
    public Object getItem(int position) {


        return oList.get(position);
    }

    @Override
    public void replaceAll(Collection<ClassificationItem.NormalsBean> collection) {

        super.replaceAll(collection);
        initOList(mListObject);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Collection<ClassificationItem.NormalsBean> collection) {

        super.addAll(collection);
        initOList(mListObject);
        notifyDataSetChanged();
    }

    private void initOList(List<ClassificationItem.NormalsBean> collection) {
        for (int i = 0; i < collection.size(); i++) {
            List<ClassificationItem.NormalsBean> list = new ArrayList<>();
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
