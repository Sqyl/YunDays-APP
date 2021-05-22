package com.example.YunDays.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.YunDays.R;
import com.example.YunDays.event.dakaEvent;

import java.util.List;

public class dakaAdapter extends BaseAdapter {

    private static final String TAG = "Sqyl";
    private LayoutInflater mInflater;
    private List<dakaEvent> dakaEvents;

    public dakaAdapter(Context context, List<dakaEvent> dakaEvents) {
        this.mInflater = LayoutInflater.from(context);
        this.dakaEvents = dakaEvents;
    }

    @Override
    public int getCount() {
        return this.dakaEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return dakaEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.daka_event_list_layout, null);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.dakaEventType);
            viewHolder.name = (TextView) convertView.findViewById(R.id.dakaEventName);
            viewHolder.last_days = (TextView) convertView.findViewById(R.id.lastDays);
                convertView.setTag(viewHolder);

        } else { viewHolder = (ViewHolder) convertView.getTag(); }

            switch(dakaEvents.get(position).getType()) {
                case dakaEvent.DAKA_LEARN :
                    //问题定位在画图函数
                    viewHolder.type.setImageResource(R.drawable.ic_baseline_study_24);break;
                case dakaEvent.DAKA_HOBBY :
                    viewHolder.type.setImageResource(R.drawable.ic_baseline_hobby_24);break;
                case dakaEvent.DAKA_SPORT :
                    viewHolder.type.setImageResource(R.drawable.ic_baseline_sports_24);break;
                case dakaEvent.DAKA_READ :
                    viewHolder.type.setImageResource(R.drawable.ic_baseline_read_24);break;
                case dakaEvent.DAKA_SLEEPEARLY:
                    viewHolder.type.setImageResource(R.drawable.ic_baseline_sleep_24);break;
                default:break;
            }
            viewHolder.name.setText(dakaEvents.get(position).getName());
            viewHolder.last_days.setText(String.valueOf(dakaEvents.get(position).getLast_days()));

        return convertView;
    }

    private final class ViewHolder {
        public ImageView type;
        public TextView name;
        public TextView belong_UserID;
        public boolean daka_today;
        public TextView last_days;
        public List<String> daka_days;

    }
}

