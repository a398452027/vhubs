package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HomeListItem;

import java.text.SimpleDateFormat;
import java.util.Date;

import support.ui.adapter.SetBaseAdapter;

/**
 * Created by guotengqian on 2016/5/31.
 */
public class HomeListAdapter extends SetBaseAdapter<HomeListItem> implements View.OnClickListener {
    OnItemViewClickListener onItemViewClickListener;
    Context context;

    public HomeListAdapter(Context context, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home_movie, null);

            holder.title_iv = (ImageView) convertView.findViewById(R.id.title_iv);
            holder.title_name = (TextView) convertView.findViewById(R.id.title_name);
            holder.more = (LinearLayout) convertView.findViewById(R.id.more);
            holder.movie_01 = (RelativeLayout) convertView.findViewById(R.id.movie_01);
            holder.movie_02 = (RelativeLayout) convertView.findViewById(R.id.movie_02);
            holder.movie_03 = (RelativeLayout) convertView.findViewById(R.id.movie_03);
            holder.movie_04 = (RelativeLayout) convertView.findViewById(R.id.movie_04);
            holder.movie_05 = (RelativeLayout) convertView.findViewById(R.id.movie_05);
            holder.movie_06 = (RelativeLayout) convertView.findViewById(R.id.movie_06);

            holder.movie_iv01 = (ImageView) convertView.findViewById(R.id.movie_iv01);
            holder.movie_iv02 = (ImageView) convertView.findViewById(R.id.movie_iv02);
            holder.movie_iv03 = (ImageView) convertView.findViewById(R.id.movie_iv03);
            holder.movie_iv04 = (ImageView) convertView.findViewById(R.id.movie_iv04);
            holder.movie_iv05 = (ImageView) convertView.findViewById(R.id.movie_iv05);
            holder.movie_iv06 = (ImageView) convertView.findViewById(R.id.movie_iv06);

            holder.name_01 = (TextView) convertView.findViewById(R.id.name_01);
            holder.name_02 = (TextView) convertView.findViewById(R.id.name_02);
            holder.name_03 = (TextView) convertView.findViewById(R.id.name_03);
            holder.name_04 = (TextView) convertView.findViewById(R.id.name_04);
            holder.name_05 = (TextView) convertView.findViewById(R.id.name_05);
            holder.name_06 = (TextView) convertView.findViewById(R.id.name_06);

            holder.good_01 = (TextView) convertView.findViewById(R.id.good_01);
            holder.good_02 = (TextView) convertView.findViewById(R.id.good_02);
            holder.good_03 = (TextView) convertView.findViewById(R.id.good_03);
            holder.good_04 = (TextView) convertView.findViewById(R.id.good_04);
            holder.good_05 = (TextView) convertView.findViewById(R.id.good_05);
            holder.good_06 = (TextView) convertView.findViewById(R.id.good_06);

            holder.time_01 = (TextView) convertView.findViewById(R.id.time_01);
            holder.time_02 = (TextView) convertView.findViewById(R.id.time_02);
            holder.time_03 = (TextView) convertView.findViewById(R.id.time_03);
            holder.time_04 = (TextView) convertView.findViewById(R.id.time_04);
            holder.time_05 = (TextView) convertView.findViewById(R.id.time_05);
            holder.time_06 = (TextView) convertView.findViewById(R.id.time_06);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        HomeListItem homeListItem = (HomeListItem) getItem(position);
        if (!TextUtils.isEmpty(homeListItem.getLogo())) {
            VApplication.setBitmapEx(holder.title_iv, homeListItem.getLogo());
        } else {
            holder.title_iv.setImageResource(R.mipmap.hot);
        }
        holder.title_name.setText(homeListItem.getName());
        holder.more.setTag(homeListItem);
        holder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClickListener.onItemViewClick(v,position);
            }
        });
        for (int i = 0; i < homeListItem.getMovices().size(); i++) {
            HomeListItem.MovicesBean movicesBean = homeListItem.getMovices().get(i);
            switch (i) {
                case 0:
                    setMoveData(holder.movie_01, holder.movie_iv01, holder.name_01, holder.good_01, holder.time_01,
                            movicesBean, position);
                    break;
                case 1:
                    setMoveData(holder.movie_02, holder.movie_iv02, holder.name_02, holder.good_02, holder.time_02,
                            movicesBean, position);
                    break;
                case 2:
                    setMoveData(holder.movie_03, holder.movie_iv03, holder.name_03, holder.good_03, holder.time_03,
                            movicesBean, position);
                    break;
                case 3:
                    setMoveData(holder.movie_04, holder.movie_iv04, holder.name_04, holder.good_04, holder.time_04,
                            movicesBean, position);
                    break;
                case 4:
                    setMoveData(holder.movie_05, holder.movie_iv05, holder.name_05, holder.good_05, holder.time_05,
                            movicesBean, position);
                    break;
                case 5:
                    setMoveData(holder.movie_06, holder.movie_iv06, holder.name_06, holder.good_06, holder.time_06,
                            movicesBean, position);
                    break;

            }
        }

        return convertView;
    }

    private void setMoveData(
            RelativeLayout movie,
            ImageView movie_iv,
            TextView name,
            TextView good,
            TextView time, HomeListItem.MovicesBean data, int p
    ) {
        movie.setTag(p);
        movie.setOnClickListener(this);
        VApplication.setBitmapEx(movie_iv, data.getCover_img());
        name.setText(data.getName());
        good.setText(data.getGrade());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm");
        time.setText(simpleDateFormat.format(new Date(Long.valueOf(data.getDuration()))));
    }

    public class ViewHolder {
        LinearLayout more;
        ImageView title_iv;
        TextView title_name;
        RelativeLayout movie_01;
        ImageView movie_iv01;
        TextView name_01;
        TextView good_01;
        TextView time_01;
        RelativeLayout movie_02;
        ImageView movie_iv02;
        TextView name_02;
        TextView good_02;
        TextView time_02;
        RelativeLayout movie_03;
        ImageView movie_iv03;
        TextView name_03;
        TextView good_03;
        TextView time_03;
        RelativeLayout movie_04;
        ImageView movie_iv04;
        TextView name_04;
        TextView good_04;
        TextView time_04;
        RelativeLayout movie_05;
        ImageView movie_iv05;
        TextView name_05;
        TextView good_05;
        TextView time_05;
        RelativeLayout movie_06;
        ImageView movie_iv06;
        TextView name_06;
        TextView good_06;
        TextView time_06;

    }

    @Override
    public void onClick(View v) {
        onItemViewClickListener.onItemViewClick(v, (Integer) v.getTag());
    }
}
