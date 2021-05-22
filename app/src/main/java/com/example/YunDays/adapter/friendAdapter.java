package com.example.YunDays.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.YunDays.R;
import com.example.YunDays.event.Friend;
import com.example.YunDays.event.dayEvent;

import java.util.List;

public class friendAdapter extends BaseAdapter {

    private static final String TAG = "Sqyl";
    private LayoutInflater mInflater;
    private List<Friend> friends;

    public friendAdapter(Context context, List<Friend> friends) {
        this.mInflater = LayoutInflater.from(context);
        this.friends = friends;
    }

    @Override
    public int getCount() {
        return this.friends.size();
    }

    @Override
    public Object getItem(int position) {
        return this.friends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.i(TAG, "getView:inadapter " + this.friends.size());
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.friend_list_layout, null);
            viewHolder.friendId = (TextView) convertView.findViewById(R.id.friend_id);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        viewHolder.friendId.setText("ID:" + friends.get(position).getFriendId() + "");

        return convertView;
    }

    private final class ViewHolder {
        public TextView id;
        public TextView belong_UserId;
        public TextView friendId;

    }
}
