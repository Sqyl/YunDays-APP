package com.example.YunDays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.request.ClassEventRequest;
import com.example.YunDays.request.DakaEventRequest;
import com.example.YunDays.request.DayEventRequest;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.sqlite.MySQLiteOpenHelper;
import com.example.YunDays.ui.calendar.alarm.notificationService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Sqyl";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //从服务器刷新信息
        DayEventRequest.refreshDayEvent(MainActivity.this);
        ClassEventRequest.refreshClassEvent(MainActivity.this);
        DakaEventRequest.refreshDakaEvent(MainActivity.this);

        setContentView(R.layout.activity_main);
        //API24以上系统分享支持file:///开头
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_calendar, R.id.navigation_time_class, R.id.navigation_user,
                R.id.navigation_daka)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        startService(new Intent(MainActivity.this, notificationService.class));


//        SQLiteOpenHelper helper = MySQLiteOpenHelper.getInstance(MainActivity.this);
//        SQLiteDatabase db = helper.getReadableDatabase();
//        db.close();
    }

}