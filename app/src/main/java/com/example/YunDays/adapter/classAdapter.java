package com.example.YunDays.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.YunDays.R;
import com.example.YunDays.event.classEvent;

import java.util.List;


public class classAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<classEvent> classEvents;

    public classAdapter(Context context, List<classEvent> classEvents) {
        this.mInflater = LayoutInflater.from(context);
        this.classEvents = classEvents;
    }

    @Override
    public int getCount() {
        return this.classEvents.size();
    }

    @Override
    public Object getItem(int position) {
        return classEvents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if(convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.class_event_list_layout, null);
            viewHolder.name = (TextView) convertView.findViewById(R.id.classEventName);
            viewHolder.class_teacher = (TextView) convertView.findViewById(R.id.classEvent_teacher);
            viewHolder.classRoom = (TextView) convertView.findViewById(R.id.classEvent_classroom);
            viewHolder.class_time = (TextView) convertView.findViewById(R.id.class_begin_end);
            convertView.setTag(viewHolder);

        } else { viewHolder = (ViewHolder) convertView.getTag(); }
        viewHolder.name.setText(classEvents.get(position).getName());
        viewHolder.class_teacher.setText(classEvents.get(position).getClass_teacher());
        if(classEvents.get(position).getEnd_class() != -1) {
            viewHolder.class_time.setText("第" + classEvents.get(position).getBegin_class()
                    + "~" + classEvents.get(position).getEnd_class() + "节");
        } else {
            viewHolder.class_time.setText("第" + classEvents.get(position).getBegin_class() + "节");
        }
        viewHolder.classRoom.setText(classEvents.get(position).getClassRoom());
        return convertView;
    }

    private final class ViewHolder {
        public TextView name;
        public TextView class_teacher;
        public TextView classRoom;
        public TextView class_time;

    }
}
