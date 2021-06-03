package com.example.YunDays.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.YunDays.R;
import com.example.YunDays.event.dakaEvent;

import java.util.List;

public class FriendDakaAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<dakaEvent> dakaEvents;

    public FriendDakaAdapter(Context context, List<dakaEvent> dakaEvents) {
        this.mInflater = LayoutInflater.from(context);
        this.dakaEvents = dakaEvents;
    }

    @Override
    public int getCount() {
        return this.dakaEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dakaEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FriendDakaAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FriendDakaAdapter.ViewHolder();
            convertView = mInflater.inflate(R.layout.daka_event_list_layout, null);
            viewHolder.type = (ImageView) convertView.findViewById(R.id.dakaEventType);
            viewHolder.name = (TextView) convertView.findViewById(R.id.dakaEventName);
            convertView.setTag(viewHolder);

        } else { viewHolder = (FriendDakaAdapter.ViewHolder) convertView.getTag(); }

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

        return convertView;
    }

    private class ViewHolder {
        public ImageView type;
        public TextView name;
    }
}
