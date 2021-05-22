package com.example.YunDays.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.YunDays.R;
import com.example.YunDays.event.dayEvent;

import java.util.List;

public class dayAdapter extends BaseAdapter {

    private static final String TAG = "Sqyl";
    private LayoutInflater mInflater;
    private List<dayEvent> dayEvents;

    public dayAdapter(Context context, List<dayEvent> dayEvents) {
        this.mInflater = LayoutInflater.from(context);
        this.dayEvents = dayEvents;

    }

    @Override
    public int getCount() { return this.dayEvents.size(); }

    @Override
    public Object getItem(int position) { return dayEvents.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.day_event_list_layout, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.dayEventName);
            viewHolder.time = (TextView) convertView.findViewById(R.id.dayEventTime);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.name.setText(dayEvents.get(position).getName());
        viewHolder.time.setText(dayEvents.get(position).getTime());

        return convertView;
    }

    private final class ViewHolder {
        public TextView name;
        public TextView belong_UserID;
        public TextView time;

    }
}
