package com.example.YunDays.ui.time_class;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.YunDays.R;
import com.example.YunDays.adapter.classAdapter;
import com.example.YunDays.event.classEvent;
import com.example.YunDays.request.ClassEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.calendar.UpdateDayEventActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class timeClassFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "Sqyl";
    private View root;
    private ImageView classEvent_add;
    private Intent intent;
    private List<classEvent> classEvents = new ArrayList<>();
    private ListView listView;
    private classAdapter classAdapter;
    private TextView dayOfWeek;
    private int class_date;
    public timeClassFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //获取数据
        Calendar c = Calendar.getInstance();
        class_date = c.get(Calendar.DAY_OF_WEEK);
        EventSQLiteOperation operation = new EventSQLiteOperation();
        classEvents = operation.searchClassEvent(getActivity(), class_date);
        Collections.sort(classEvents);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        if (root == null) {
            root = inflater.inflate(R.layout.fragment_time_class, container, false);
        }

        //获取今日日期，并将相对应的textView背景换色
        switch (class_date){
            case 1:
                dayOfWeek = root.findViewById(R.id.text_class_Sunday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 2:
                dayOfWeek = root.findViewById(R.id.text_class_Monday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 3:
                dayOfWeek = root.findViewById(R.id.text_class_Tuesday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 4:
                dayOfWeek = root.findViewById(R.id.text_class_Wednesday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 5:
                dayOfWeek = root.findViewById(R.id.text_class_Thursday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 6:
                dayOfWeek = root.findViewById(R.id.text_class_Friday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            case 7:
                dayOfWeek = root.findViewById(R.id.text_class_Saturday);
                dayOfWeek.setBackgroundColor(getResources().getColor(R.color.teal_200));
                break;
            default:break;
        }

        //点击导航栏按钮跳转至添加课程页面
        classEvent_add = root.findViewById(R.id.classEvent_add);
        classEvent_add.setOnClickListener(v -> {
            intent = new Intent(getActivity(), AddClassActivity.class);
        startActivity(intent);
            classAdapter.notifyDataSetChanged();
        });

        //对课程根据开始时间排序
        Collections.sort(classEvents);

        //为标题栏每个元素添加点击事件
        TextView[] dayOfWeeks = {
                root.findViewById(R.id.text_class_Monday),
                root.findViewById(R.id.text_class_Tuesday),
                root.findViewById(R.id.text_class_Wednesday),
                root.findViewById(R.id.text_class_Thursday),
                root.findViewById(R.id.text_class_Friday),
                root.findViewById(R.id.text_class_Saturday),
                root.findViewById(R.id.text_class_Sunday)};
        for(TextView textView : dayOfWeeks) {
            textView.setOnClickListener(this);
        }

        //如果内容为空则返回一个字符串
        listView = (ListView) root.findViewById(R.id.classList);
        View if_empty = root.findViewById(R.id.if_empty_class);
        listView.setEmptyView(if_empty);

        //调用adapter
        classAdapter = new classAdapter(getActivity(), classEvents);
        listView.setAdapter(classAdapter);

        //单击listView中元素，显示具体信息的dialog
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder click_builder = new AlertDialog.Builder(requireActivity());
            click_builder.setTitle("课程信息");
            String temp_class;
            if(classEvents.get(position).getEnd_class() != -1) {
                temp_class = ("第" + classEvents.get(position).getBegin_class()
                        + "~" + classEvents.get(position).getEnd_class() + "节");
            } else {
                temp_class = ("第" + classEvents.get(position).getBegin_class() + "节");
            }
            click_builder.setMessage("课程：" + classEvents.get(position).getName() + "\n"
                    + "教室：" + classEvents.get(position).getClassRoom() + "\n"
                    + "老师：" + classEvents.get(position).getClass_teacher() + "\n"
                    + temp_class);

            click_builder.setPositiveButton("我知道啦", (dialog, which) -> { });
            click_builder.setNegativeButton("更改", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), UpdateClassEventActivity.class);
                    intent.putExtra("classEvent_id", classEvents.get(position).get_id());
                    intent.putExtra("name", classEvents.get(position).getName());
                    intent.putExtra("teacher", classEvents.get(position).getClass_teacher());
                    intent.putExtra("classroom", classEvents.get(position).getClassRoom());
                    intent.putExtra("begin_class", classEvents.get(position).getBegin_class());
                    intent.putExtra("end_class", classEvents.get(position).getEnd_class());
                    startActivity(intent);
                }
            });
            click_builder.create().show();

        });
        //长按listView中元素删除
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            EventSQLiteOperation operation = new EventSQLiteOperation();
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("提示");
            builder.setMessage("是否删除该项？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                if(operation.deleteClassEvent(getActivity(), classEvents.get(position))){
                    ClassEventRequest.deleteClassEvent(getActivity(), classEvents.get(position));
                    classEvents.remove(position);
                    Toast.makeText(getActivity(), "删除课程成功！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "删除课程失败！",Toast.LENGTH_SHORT).show();
                }
                classAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.create().show();
            return true;
        });
        return root;
    }

    @Override
    public void onClick(View v) {
        EventSQLiteOperation operation = new EventSQLiteOperation();
        switch (v.getId()) {
            case R.id.text_class_Monday:
            classEvents = operation.searchClassEvent(getActivity(), classEvent.Monday);
            classAdapter = new classAdapter(getActivity(), classEvents);
            listView.setAdapter(classAdapter);
            break;
            case R.id.text_class_Tuesday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Tuesday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            case R.id.text_class_Wednesday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Wednesday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            case R.id.text_class_Thursday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Thursday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            case R.id.text_class_Friday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Friday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            case R.id.text_class_Saturday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Saturday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            case R.id.text_class_Sunday:
                classEvents = operation.searchClassEvent(getActivity(), classEvent.Sunday);
                classAdapter = new classAdapter(getActivity(), classEvents);
                listView.setAdapter(classAdapter);
                break;
            default:break;
        }
    }
}