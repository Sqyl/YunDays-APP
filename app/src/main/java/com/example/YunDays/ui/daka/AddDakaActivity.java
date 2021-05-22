package com.example.YunDays.ui.daka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.YunDays.MainActivity;
import com.example.YunDays.R;
import com.example.YunDays.event.dakaEvent;
import com.example.YunDays.request.DakaEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.time_class.AddClassActivity;

import java.util.ArrayList;

public class AddDakaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner daka_type_spinner;
    private EditText input_daka_event_name;
    private EditText input_daka_event_days;
    private Button btn_save_daka_event;
    private String[] spinner_array;
    private String input_daka_event_type;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(AddDakaActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daka);

        toolbar = findViewById(R.id.toolbar_add_daka);
        toolbar.setNavigationOnClickListener(v -> finish());

        spinner_array = getResources().getStringArray(R.array.TypeofDaka);
        daka_type_spinner = (Spinner) findViewById(R.id.daka_type_spinner);
        daka_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (spinner_array[position]) {
                    case "学习": input_daka_event_type = dakaEvent.DAKA_LEARN;break;
                    case "运动": input_daka_event_type = dakaEvent.DAKA_SPORT;break;
                    case "爱好": input_daka_event_type = dakaEvent.DAKA_HOBBY;break;
                    case "阅读": input_daka_event_type = dakaEvent.DAKA_READ;break;
                    case "早睡": input_daka_event_type = dakaEvent.DAKA_SLEEPEARLY;break;
                    default:break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        input_daka_event_name = (EditText) findViewById(R.id.input_daka_event_name);
        input_daka_event_days = (EditText) findViewById(R.id.input_daka_event_days);
        btn_save_daka_event = (Button) findViewById(R.id.btn_save_daka_event);
        btn_save_daka_event.setOnClickListener(v -> {
            if (input_daka_event_name.getText().toString().equals("")) {
                Toast.makeText(this, "还没有输入打卡项名称！", Toast.LENGTH_SHORT).show();
            } else if (Integer.parseInt(input_daka_event_days.getText().toString()) <= 0) {
                Toast.makeText(this, "打卡次数不能小于等于0！", Toast.LENGTH_SHORT).show();
            } else {
                /**
                 * public dakaEvent(String type, String name, int belongUserID, int last_days,
                 *                      List<String> daka_days)
                 */
                //写入数据
                dakaEvent insert_dakaEvent = new dakaEvent(0, input_daka_event_type,
                        input_daka_event_name.getText().toString(),
                        belong_userID, Integer.parseInt(input_daka_event_days.getText().toString()),
                        new ArrayList<String>());
                DakaEventRequest.insertDakaEvent(AddDakaActivity.this, insert_dakaEvent);
                operation.insertDakaEvent(AddDakaActivity.this, insert_dakaEvent);
                finish();
                Intent intent = new Intent(AddDakaActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}