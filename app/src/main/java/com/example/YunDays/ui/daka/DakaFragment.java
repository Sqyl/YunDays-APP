package com.example.YunDays.ui.daka;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.YunDays.R;
import com.example.YunDays.adapter.dakaAdapter;
import com.example.YunDays.event.dakaEvent;
import com.example.YunDays.request.DakaEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;

import java.util.ArrayList;
import java.util.List;

public class DakaFragment extends Fragment {

    private static final String TAG = "Sqyl";
    private List<dakaEvent> dakaEvents = new ArrayList<>();
    private View root;
    private ListView listView;
    private dakaAdapter dakaAdapter;
    private ImageView dakaEvent_add;
    private Intent intent;
    private Chronometer timer;
    private Button timer_start;
    private Button timer_pause;
    private Button timer_restart;
    private EventSQLiteOperation operation = new EventSQLiteOperation();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取数据
        dakaEvents = operation.searchDakaEvent(getActivity());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(root == null) {
            root = inflater.inflate(R.layout.fragment_daka, container, false);
        }

        dakaEvent_add = root.findViewById(R.id.dakaEvent_add);
        dakaEvent_add.setOnClickListener(v -> {
            intent = new Intent(getActivity(), AddDakaActivity.class);
            startActivity(intent);
        });

        listView = (ListView) root.findViewById(R.id.dakaList);

        dakaAdapter = new dakaAdapter(getActivity(), dakaEvents);
        listView.setAdapter(dakaAdapter);
        //打卡功能
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog.Builder click_builder = new AlertDialog.Builder(requireActivity());
            click_builder.setTitle("打卡信息");
            click_builder.setMessage("打卡项：" + dakaEvents.get(position).getName() + "\n"
                    + "剩余次数：" + dakaEvents.get(position).getLast_days());
            click_builder.setPositiveButton("我知道啦", (dialog, which) -> { });
            click_builder.setNegativeButton("打卡", (dialog, which) -> {

                dakaEvents.get(position).daka(getActivity());
                dakaAdapter.notifyDataSetChanged();
            });

            click_builder.create().show();

        });

        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
            builder.setTitle("提示");
            builder.setMessage("是否删除该项？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                if(operation.deleteDakaEvent(getActivity(), dakaEvents.get(position))){
                    DakaEventRequest.deleteDakaEvent(getActivity(), dakaEvents.get(position));
                    dakaEvents.remove(position);
                    Toast.makeText(getActivity(), "删除打卡项成功！",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "删除打卡项失败！",Toast.LENGTH_SHORT).show();
                }
                dakaAdapter.notifyDataSetChanged();
            });
            builder.setNegativeButton("取消", (dialog, which) -> {
            });
            builder.create().show();
            return true;
        });

        timer = (Chronometer) root.findViewById(R.id.timer);
        timer.setFormat("%s");
        timer_start = (Button) root.findViewById(R.id.btn_start);
        timer_start.setEnabled(true);
        timer_pause = (Button) root.findViewById(R.id.btn_pause);
        timer_pause.setEnabled(false);
        timer_restart = (Button) root.findViewById(R.id.btn_restart);
        timer_restart.setEnabled(false);
        timer_start.setOnClickListener(v -> {
            if(!timer_start.getText().equals("继续")) {
                timer.setBase(SystemClock.elapsedRealtime());
                timer.start();
            } else {
                timer.start();
            }
            timer_pause.setEnabled(true);
            timer_start.setEnabled(false);
            timer_restart.setEnabled(false);
        });
        timer_pause.setOnClickListener(v -> {
            timer_start.setText("继续");
            timer.stop();
            timer_start.setEnabled(true);
            timer_pause.setEnabled(false);
            timer_restart.setEnabled(true);
        });
        timer_restart.setOnClickListener(v -> {
            timer_start.setText("开始");
            timer.setBase(SystemClock.elapsedRealtime());
            timer_start.setEnabled(true);
            timer_pause.setEnabled(false);
            timer_restart.setEnabled(false);
        });

        timer.setOnChronometerTickListener(chronometer -> {
            if ( SystemClock.elapsedRealtime() - timer.getBase() > 3600 * 1000) {
                timer.stop();
                timer_start.setEnabled(true);
                timer_restart.setEnabled(false);
                timer_pause.setEnabled(false);
            }
        });

        return root;
    }


}