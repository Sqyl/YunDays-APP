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

import java.util.Calendar;

public class UpdateDayEventActivity extends AppCompatActivity {

    private static final String TAG = "Sqyl";
    private Toolbar toolbar;
    private EditText update_day_event_name;
    private EditText update_day_event_date;
    private EditText update_day_event_time;
    private Button btn_update_day_event;
    private EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(UpdateDayEventActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_day_event);
        Intent intent = getIntent();
        int dayEvent_id = intent.getIntExtra("dayEvent_id", -1);

        toolbar = findViewById(R.id.toolbar_update_day_event);
        toolbar.setNavigationOnClickListener(v -> finish());

        update_day_event_name = (EditText) findViewById(R.id.update_day_event_name);
        update_day_event_name.setText(intent.getStringExtra("name"));
        update_day_event_date = (EditText) findViewById(R.id.update_day_event_date);
        update_day_event_date.setText(intent.getStringExtra("date"));
        update_day_event_time = (EditText) findViewById(R.id.update_day_event_time);
        update_day_event_time.setText(intent.getStringExtra("time"));

        //设置日期筛选器
        update_day_event_date.setInputType(InputType.TYPE_NULL);
        update_day_event_date.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(UpdateDayEventActivity.this, new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        update_day_event_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        update_day_event_date.setOnClickListener(v -> {
            Calendar c = Calendar.getInstance();
            new DatePickerDialog(UpdateDayEventActivity.this, (view, year, monthOfYear, dayOfMonth) -> {
                update_day_event_date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
        });
        update_day_event_time.setInputType(InputType.TYPE_NULL);
        update_day_event_time.setOnFocusChangeListener((v, hasFocus) -> {
            if(hasFocus) {
                Calendar c = Calendar.getInstance();
                new TimePickerDialog(UpdateDayEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        update_day_event_time.setText(hourOfDay + ":" + minute);
                    }
                }, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true).show();
            }
        });

        btn_update_day_event = findViewById(R.id.btn_update_day_event);
        btn_update_day_event.setOnClickListener(v -> {
            if(update_day_event_name.getText().toString().equals("")) {
                Toast.makeText(this, "还没有输入日程名！",Toast.LENGTH_SHORT).show();
            } else {
                /**
                 * public dayEvent(String name, int belong_userID, String date, String time)
                 */
                //写入数据
                dayEvent update_dayEvent =  new dayEvent(dayEvent_id, update_day_event_name.getText().toString(),
                        belong_userID, update_day_event_date.getText().toString(),
                        update_day_event_time.getText().toString());
                operation.updateDayEvent(UpdateDayEventActivity.this, update_dayEvent);
                DayEventRequest.updateDayEvent(UpdateDayEventActivity.this, update_dayEvent);
                finish();
                Intent intent1 = new Intent(UpdateDayEventActivity.this, MainActivity.class);
                startActivity(intent1);

            }
        });

    }
}