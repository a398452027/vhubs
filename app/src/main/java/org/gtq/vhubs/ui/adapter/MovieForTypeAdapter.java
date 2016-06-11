package org.gtq.vhubs.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.gtq.vhubs.R;
import org.gtq.vhubs.core.VApplication;
import org.gtq.vhubs.dao.HMoiveItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import support.ui.adapter.SetBaseAdapter;

/**
 * Created by guo on 2016/6/4.
 */
public class MovieForTypeAdapter extends SetBaseAdapter<HMoiveItem> {

    OnItemViewClickListener onItemViewClickListener;
    Context context;

    ArrayList<List<HMoiveItem>> oList;


    public MovieForTypeAdapter(Context context, OnItemViewClickListener onItemViewClickListener) {
        this.context = context;
        this.onItemViewClickListener = onItemViewClickListener;
        oList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_moviefortype, null);

            holder.movie_01 = (RelativeLayout) convertView.findViewById(R.id.movie_01);
            holder.movie_02 = (RelativeLayout) convertView.findViewById(R.id.movie_02);
            holder.movie_03 = (RelativeLayout) convertView.findViewById(R.id.movie_03);

            holder.movie_iv01 = (ImageView) convertView.findViewById(R.id.movie_iv01);
            holder.movie_iv02 = (ImageView) convertView.findViewById(R.id.movie_iv02);
            holder.movie_iv03 = (ImageView) convertView.findViewById(R.id.movie_iv03);

            holder.name_01 = (TextView) convertView.findViewById(R.id.name_01);
            holder.name_02 = (TextView) convertView.findViewById(R.id.name_02);
            holder.name_03 = (TextView) convertView.findViewById(R.id.name_03);

            holder.good_01 = (TextView) convertView.findViewById(R.id.good_01);
            holder.good_02 = (TextView) convertView.findViewById(R.id.good_02);
            holder.good_03 = (TextView) convertView.findViewById(R.id.good_03);

            holder.time_01 = (TextView) convertView.findViewById(R.id.time_01);
            holder.time_02 = (TextView) convertView.findViewById(R.id.time_02);
            holder.time_03 = (TextView) convertView.findViewById(R.id.time_03);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        List<HMoiveItem> list = (List<HMoiveItem>) getItem(position);
        for (int i = 0; i < list.size(); i++) {
            HMoiveItem normalsBean = list.get(i);
            switch (i) {
                case 0:
                    setMoveData(holder.movie_01, holder.movie_iv01, holder.name_01, holder.good_01, holder.time_01, normalsBean, position);
                    break;
                case 1:
                    setMoveData(holder.movie_02, holder.movie_iv02, holder.name_02, holder.good_02, holder.time_02, normalsBean, position);
                    break;
                case 2:
                    setMoveData(holder.movie_03, holder.movie_iv03, holder.name_03, holder.good_03, holder.time_03, normalsBean, position);
                    break;
            }
        }
        switch (list.size()) {
            case 1:
                holder.movie_02.setVisibility(View.INVISIBLE);
                holder.movie_03.setVisibility(View.INVISIBLE);
                break;
            case 2:
                holder.movie_02.setVisibility(View.VISIBLE);
                holder.movie_03.setVisibility(View.INVISIBLE);
                break;
            case 3:
                holder.movie_02.setVisibility(View.VISIBLE);
                holder.movie_03.setVisibility(View.VISIBLE);
                break;
        }

        return convertView;
    }

    private void setMoveData(
            final RelativeLayout movie,
            ImageView movie_iv,
            TextView name,
            TextView good,
            TextView time, HMoiveItem data, final int p
    ) {
        movie.setTag(data);
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemViewClickListener.onItemViewClick(movie, p);
            }
        });
        VApplication.setBitmapEx(movie_iv, data.getCover_img());
        name.setText(data.getName());
        good.setText(data.getGrade());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        time.setText(simpleDateFormat.format(new Date(Integer.valueOf(data.getDuration())*1000)));
    }


    @Override
    public Object getItem(int position) {


        return oList.get(position);
    }

    @Override
    public void replaceAll(Collection<HMoiveItem> collection) {
        oList.clear();
        super.replaceAll(collection);
        initOList(mListObject);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(Collection<HMoiveItem> collection) {

        super.addAll(collection);
        initOList(mListObject);
        notifyDataSetChanged();
    }

    private void initOList(List<HMoiveItem> collection) {
        for (int i = 0; i < collection.size();) {
            List<HMoiveItem> list = new ArrayList<>();
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

    }

}
