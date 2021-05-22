package com.example.YunDays.ui.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.YunDays.MainActivity;
import com.example.YunDays.R;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.request.DayEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.time_class.AddClassActivity;

import java.util.Calendar;

public class AddDayEventActivity extends AppCompatActivity {

    private static final String TAG = "Sqyl";
    private Toolbar toolbar;
    private EditText input_day_event_name;
    private EditText input_day_event_date;
    private EditText input_day_event_time;
    private Button btn_save_day_event;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(AddDayEventActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_day_event);

        toolbar = findViewById(R.id.toolbar_add_day_event);
        toolbar.setNavigationOnClickListener(v -> finish());

        input_day_event_name = (EditText) findViewById(R.id.input_day_event_name);
        input_day_event_date = (EditText) findViewById(R.id.input_day_event_date);
        input_day_event_time = (EditText) findViewById(R.id.input_day_event_time);

        //设置日期筛选器
        input_day_event_date.setInputType(InputType.TYPE_NULL);
        input_day_event_date.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(AddDayEventActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        input_day_event_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        input_day_event_date.setOnClickListener(v -> {Calendar c = Calendar.getInstance();
        new DatePickerDialog(AddDayEventActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                input_day_event_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
        input_day_event_time.setInputType(InputType.TYPE_NULL);
        input_day_event_time.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(AddDayEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        input_day_event_time.setText(hourOfDay + ":" + minute);
                    }
                }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
            }
        });

        btn_save_day_event = findViewById(R.id.btn_save_day_event);
        btn_save_day_event.setOnClickListener(v -> {
            if(input_day_event_name.getText().toString().equals("")) {
                Toast.makeText(this, "还没有输入日程名！",Toast.LENGTH_SHORT).show();
            } else {
                /**
                 * public dayEvent(String name, int belong_userID, String date, String time)
                 */
                //写入数据
                dayEvent insert_dayevent = new dayEvent(0, input_day_event_name.getText().toString(),
                        belong_userID, input_day_event_date.getText().toString(),
                        input_day_event_time.getText().toString());
                operation.insertDayEvent(AddDayEventActivity.this, insert_dayevent);
                DayEventRequest.insertDayEvent(AddDayEventActivity.this, insert_dayevent);
                finish();
                Intent intent = new Intent(AddDayEventActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

    }
}