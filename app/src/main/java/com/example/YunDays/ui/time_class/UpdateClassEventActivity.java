package com.example.YunDays.ui.time_class;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.YunDays.MainActivity;
import com.example.YunDays.R;
import com.example.YunDays.event.classEvent;
import com.example.YunDays.request.ClassEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.calendar.UpdateDayEventActivity;

public class UpdateClassEventActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText update_class_event_name;
    private EditText update_class_event_begin;
    private EditText update_class_event_end;
    private EditText update_class_event_teacher;
    private EditText update_class_event_classroom;
    private Button btn_update_class_event;
    private Spinner update_class_dayOfWeek_spinner;
    private String[] dayOfWeek_array;
    private int update_class_dayOfWeek;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(UpdateClassEventActivity.this).getUserID();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_class_event);
        Intent intent = getIntent();
        int classEvent_id = intent.getIntExtra("classEvent_id", -1);

        toolbar = findViewById(R.id.toolbar_update_class);
        toolbar.setNavigationOnClickListener(v -> finish());

        dayOfWeek_array = getResources().getStringArray(R.array.dayOfWeek);
        update_class_dayOfWeek_spinner = (Spinner) findViewById(R.id.update_class_dayOfWeek_spinner);
        update_class_dayOfWeek_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input_dayOfWeek = dayOfWeek_array[position];
                switch (input_dayOfWeek) {
                    case "周一": update_class_dayOfWeek = classEvent.Monday;break;
                    case "周二": update_class_dayOfWeek = classEvent.Tuesday;break;
                    case "周三": update_class_dayOfWeek = classEvent.Wednesday;break;
                    case "周四": update_class_dayOfWeek = classEvent.Thursday;break;
                    case "周五": update_class_dayOfWeek = classEvent.Friday;break;
                    case "周六": update_class_dayOfWeek = classEvent.Saturday;break;
                    case "周日": update_class_dayOfWeek = classEvent.Sunday;break;
                    default:break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        update_class_event_name = (EditText) findViewById(R.id.update_class_event_name);
        update_class_event_name.setText(intent.getStringExtra("name"));
        update_class_event_begin = (EditText) findViewById(R.id.update_class_event_begin);
        update_class_event_begin.setText(intent.getIntExtra("begin_class", -1) + "");
        update_class_event_end = (EditText) findViewById(R.id.update_class_event_end);
        update_class_event_end.setText(intent.getIntExtra("end_class", -1) + "");
        update_class_event_teacher = (EditText) findViewById(R.id.update_class_event_teacher);
        update_class_event_teacher.setText(intent.getStringExtra("teacher"));
        update_class_event_classroom = (EditText) findViewById(R.id.update_class_event_classroom);
        update_class_event_classroom.setText(intent.getStringExtra("classroom"));

        btn_update_class_event = (Button) findViewById(R.id.btn_update_class_event);
        btn_update_class_event.setOnClickListener(v -> {
            if(update_class_event_name.getText().toString().equals("")) {
                Toast.makeText(this, "还没有输入课程名！", Toast.LENGTH_SHORT).show();
            } else {
                /**
                 * public classEvent(String name, int belong_UserID,
                 *                            int class_date, String class_teacher, String classRoom,
                 *                            int begin_class, int end_class)
                 */
                //修改数据
                classEvent update_classEvent = new classEvent(classEvent_id, update_class_event_name.getText().toString(),
                        belong_userID, update_class_dayOfWeek,
                        update_class_event_teacher.getText().toString(),
                        update_class_event_classroom.getText().toString(),
                        Integer.parseInt(update_class_event_begin.getText().toString()),
                        Integer.parseInt(update_class_event_end.getText().toString()));
                EventSQLiteOperation operation = new EventSQLiteOperation();
                operation.updateClassEvent(UpdateClassEventActivity.this, update_classEvent);
                ClassEventRequest.updateClassEvent(UpdateClassEventActivity.this,
                        update_classEvent);
                finish();
                Intent intent1 = new Intent(UpdateClassEventActivity.this, MainActivity.class);
                startActivity(intent1);
            }
        });
    }
}