package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.ClassificationItem;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HomeListItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import support.bean.IDObject;
import support.ui.adapter.SetBaseAdapter;

/**
 * Created by guo on 2016/6/5.
 */
public  class HMoiveAdapter<T> extends SetBaseAdapter<T> {

    OnItemViewClickListener onItemViewClickListener;
    Context context;


    public HMoiveAdapter(Context context, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_h_movie, null);

            holder.image = (ImageView) convertView.findViewById(R.id.image);
            holder.duration = (TextView) convertView.findViewById(R.id.duration);
            holder.all_ll = (LinearLayout) convertView.findViewById(R.id.all_ll);
            holder.good = (TextView) convertView.findViewById(R.id.good);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.creat = (TextView) convertView.findViewById(R.id.creat);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HMoiveItem moiveItem = null;
        if(getItem(position) instanceof HMoiveItem){
            moiveItem = (HMoiveItem) getItem(position);
        }else if(getItem(position) instanceof FavoritesMoive){
            moiveItem = ((FavoritesMoive) getItem(position)).gethMoiveItem();
        }
        VApplication.setBitmapEx(holder.image, moiveItem.getCover_img());
        holder.name.setText(moiveItem.getName());
        holder.good.setText(moiveItem.getGrade());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        holder.duration.setText(simpleDateFormat.format(new Date(Integer.valueOf(moiveItem.getDuration())*1000)));
        SimpleDateFormat creatTimeSf = new SimpleDateFormat("yyyy-MM-dd");
        holder.creat.setText(context.getString(R.string.creat_time,creatTimeSf.format(new Date(Long.valueOf(moiveItem.getCreate_time())*1000))));
        holder.time.setText(context.getString(R.string.view_num,moiveItem.getShow_play_time()));
        if(position%2==1){
            holder.all_ll.setBackgroundColor(context.getResources().getColor(R.color.h_movie_white));
        }else{
            holder.all_ll.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
        }
        return convertView;
    }

    public class ViewHolder {

        ImageView image;
        TextView duration;
        LinearLayout all_ll;
        TextView good;
        TextView name;
        TextView time;
        TextView creat;

    }

}
