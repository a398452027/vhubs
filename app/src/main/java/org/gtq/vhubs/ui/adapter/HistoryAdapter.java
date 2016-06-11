package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.FavoritesMoive;
import org.gtq.vhubs.dao.HMoiveItem;
import org.gtq.vhubs.dao.HistoryMoive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import support.ui.adapter.SetBaseAdapter;

/**
 * Created by gtq on 2016/6/7.
 */
public class HistoryAdapter<T> extends SetBaseAdapter<T> implements StickyListHeadersAdapter {


    Context context;
    OnItemViewClickListener onItemViewClickListener;

    public HistoryAdapter(Context context,   OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history, null);

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
        HMoiveItem moiveItem = ((HistoryMoive) getItem(position)).gethMoiveItem();
        holder.all_ll.setTag(moiveItem);
        holder.all_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClickListener.onItemViewClick(v,position);
            }
        });
        VApplication.setBitmapEx(holder.image, moiveItem.getCover_img());
        holder.name.setText(moiveItem.getName());
        holder.good.setText(moiveItem.getGrade());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        holder.duration.setText(simpleDateFormat.format(new Date(Integer.valueOf(moiveItem.getDuration()) * 1000)));
        SimpleDateFormat creatTimeSf = new SimpleDateFormat("yyyy-MM-dd");
        holder.creat.setText(context.getString(R.string.creat_time, creatTimeSf.format(new Date(Long.valueOf(moiveItem.getCreate_time()) * 1000))));
        holder.time.setText(context.getString(R.string.view_num, moiveItem.getShow_play_time()));
        if (position % 2 == 1) {
            holder.all_ll.setBackgroundColor(context.getResources().getColor(R.color.h_movie_white));
        } else {
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

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {

        HeaderViewHolder holder;
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_history_header, null);
            holder.textView = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        HistoryMoive moiveItem = (HistoryMoive) getItem(position);
        SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM");
        String year=sf.format(new Date(moiveItem.getWatch_time())).split("-")[0];

        String month=sf.format(new Date(moiveItem.getWatch_time())).split("-")[1];
        month=monthToString(Integer.valueOf(month));
        holder.textView.setText(month+" "+year);
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        SimpleDateFormat sf=new SimpleDateFormat("MM");
        int month= Integer.valueOf(sf.format(new Date(((HistoryMoive) getItem(position)).getWatch_time())));
        return month;
    }

    public class HeaderViewHolder {
        TextView textView;
    }

    private String monthToString(int month) {
        switch (month) {
            case 1:
                return "JANUARY";
            case 2:
                return "FEBRUARY";
            case 3:
                return "MARCH";
            case 4:
                return "APRIL";
            case 5:
                return "MAY";
            case 6:
                return "JUNE";
            case 7:
                return "JULY";
            case 8:
                return "AUGUST";
            case 9:
                return "SEPTEMBER";
            case 10:
                return "OCTOBER";
            case 11:
                return "NOVEMBER";
            case 12:
                return "DECEMBER";
        }
        return "";

    }

}
