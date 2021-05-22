package com.example.YunDays.ui.time_class;

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
import com.example.YunDays.event.classEvent;
import com.example.YunDays.request.ClassEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;

public class AddClassActivity extends AppCompatActivity {

    private static final String TAG = "Sqyl";
    private Toolbar toolbar;
    private EditText input_class_event_name;
    private EditText input_class_event_begin;
    private EditText input_class_event_end;
    private EditText input_class_event_teacher;
    private EditText input_class_event_classroom;
    private Button btn_save_class_event;
    private String[] dayOfWeek_array;
    private Spinner class_dayOfWeek_spinner;
    private int input_class_dayOfWeek;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(AddClassActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);
        toolbar = findViewById(R.id.toolbar_add_class);
        toolbar.setNavigationOnClickListener(v -> finish());

        dayOfWeek_array = getResources().getStringArray(R.array.dayOfWeek);
        class_dayOfWeek_spinner = (Spinner) findViewById(R.id.class_dayOfWeek_spinner);
        class_dayOfWeek_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String input_dayOfWeek = dayOfWeek_array[position];
                switch (input_dayOfWeek) {
                    case "周一": input_class_dayOfWeek = classEvent.Monday;break;
                    case "周二": input_class_dayOfWeek = classEvent.Tuesday;break;
                    case "周三": input_class_dayOfWeek = classEvent.Wednesday;break;
                    case "周四": input_class_dayOfWeek = classEvent.Thursday;break;
                    case "周五": input_class_dayOfWeek = classEvent.Friday;break;
                    case "周六": input_class_dayOfWeek = classEvent.Saturday;break;
                    case "周日": input_class_dayOfWeek = classEvent.Sunday;break;
                    default:break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });


        input_class_event_name = (EditText) findViewById(R.id.input_class_event_name);
        input_class_event_begin = (EditText) findViewById(R.id.input_class_event_begin);
        input_class_event_end = (EditText) findViewById(R.id.input_class_event_end);
        input_class_event_teacher = (EditText) findViewById(R.id.input_class_event_teacher);
        input_class_event_classroom = (EditText) findViewById(R.id.input_class_event_classroom);

        btn_save_class_event = (Button) findViewById(R.id.btn_save_class_event);
        btn_save_class_event.setOnClickListener(v -> {
            if(input_class_event_name.getText().toString().equals("")) {
                Toast.makeText(this, "还没有输入课程名！", Toast.LENGTH_SHORT).show();
            } else {
                /**
                 * public classEvent(String name, int belong_UserID,
                 *                            int class_date, String class_teacher, String classRoom,
                 *                            int begin_class, int end_class)
                 */
                //写入数据
                classEvent insert_classEvent = new classEvent(0, input_class_event_name.getText().toString(),
                        belong_userID, input_class_dayOfWeek,
                        input_class_event_teacher.getText().toString(),
                        input_class_event_classroom.getText().toString(),
                        Integer.parseInt(input_class_event_begin.getText().toString()),
                        Integer.parseInt(input_class_event_end.getText().toString()));
                operation.insertClassEvent(AddClassActivity.this, insert_classEvent);
                ClassEventRequest.insertClassEvent(AddClassActivity.this, insert_classEvent);
                finish();
                Intent intent = new Intent(AddClassActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}