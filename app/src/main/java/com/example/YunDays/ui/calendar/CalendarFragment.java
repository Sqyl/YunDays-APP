package com.example.YunDays.ui.calendar;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.YunDays.R;
import com.example.YunDays.adapter.dayAdapter;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.request.DayEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CalendarFragment extends Fragment {

    private static final String TAG = "Sqyl";
    private View root;
    private CalendarView calendarView;
    private ListView listView;
    private List<dayEvent> dayEvents = new ArrayList<>();
    private dayAdapter adapter;
    private ImageView dayEvent_add;
    private Intent intent;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    public CalendarFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_calendar, container, false);
        }
        //获取数据
        Calendar c = Calendar.getInstance();
        @SuppressLint("DefaultLocale") String today = String.format("%d-%d-%d",
                c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
                c.get(Calendar.DAY_OF_MONTH));

        dayEvents = operation.searchDayEvent(getActivity(), today);

        //跳转至添加日程页面
        dayEvent_add = root.findViewById(R.id.dayEvent_add);
        dayEvent_add.setOnClickListener(v -> {
            intent = new Intent(getActivity(), AddDayEventActivity.class);
            startActivity(intent);
            adapter.notifyDataSetChanged();
        });



        listView = (ListView) root.findViewById(R.id.list_calendar_event);
        //如果内容为空则set一个字符串
        View if_empty = root.findViewById(R.id.if_empty);
        listView.setEmptyView(if_empty);
        //adapter显示数据
        adapter = new dayAdapter(getActivity(), dayEvents);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder click_builder = new AlertDialog.Builder(requireActivity());
            click_builder.setTitle("日程信息");
            click_builder.setMessage("日程：" + dayEvents.get(position).getName() + "\n" + "\n"
                                   + "时间：" + dayEvents.get(position).getTime());
            click_builder.setPositiveButton("我知道啦", (dialog, which) -> { });
            click_builder.setNegativeButton("更改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), UpdateDayEventActivity.class);
                    intent.putExtra("dayEvent_id", dayEvents.get(position).get_id());
                    intent.putExtra("name", dayEvents.get(position).getName());
                    intent.putExtra("date", dayEvents.get(position).getDate());
                    intent.putExtra("time", dayEvents.get(position).getTime());
                    startActivity(intent);
                }
            });
            click_builder.create().show();
        });


        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("提示");
            builder.setMessage("是否删除该项？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                if( operation.deleteDayEvent(getActivity(), dayEvents.get(position))){
                    DayEventRequest.deleteDayEvent(getActivity(), dayEvents.get(position));
                    dayEvents.remove(position);
                    Toast.makeText(getActivity(), "删除日程成功！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "删除日程失败！",Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("取消", (dialog, which) -> { });
            builder.create().show();
            return true;
        });

        calendarView = (CalendarView) root.findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            month = month + 1;
            //stringList.add("您选择的时间是" + year + "/"+ month + "/" + dayOfMonth);
            String date_selected = String.format("%d-%d-%d", year, month, dayOfMonth);
            EventSQLiteOperation operation1 = new EventSQLiteOperation();
            dayEvents = operation1.searchDayEvent(getActivity(), date_selected);
            adapter = new dayAdapter(getActivity(), dayEvents);
            listView.setAdapter(adapter);
        });

        return root;
    }

}