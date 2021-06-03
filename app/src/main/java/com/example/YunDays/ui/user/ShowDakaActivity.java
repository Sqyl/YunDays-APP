package com.example.YunDays.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.R;
import com.example.YunDays.adapter.FriendDakaAdapter;
import com.example.YunDays.event.Friend;
import com.example.YunDays.event.dakaEvent;
import com.example.YunDays.request.RequestIP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowDakaActivity extends AppCompatActivity {

    private final String TAG = "Sqyl";
    private Toolbar toolbar;
    private ListView listView;
    private FriendDakaAdapter fdAdapter;
    private List<dakaEvent> dakaEvents = new ArrayList<>();
    private static Gson gson = new GsonBuilder().create();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_daka);
        toolbar = findViewById(R.id.toolbar_show_daka);
        toolbar.setNavigationOnClickListener(v -> finish());
        listView = findViewById(R.id.list_show_daka);

        Intent intent = getIntent();
        int belong_userID =  intent.getIntExtra("FriendID", -1);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_userID", belong_userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/dakaevent/getDakaEventByUserId";
        RequestQueue requestQueue = Volley.newRequestQueue(ShowDakaActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("查询到打卡项")) {
                        JSONArray detail = response.getJSONArray("detail");
                        List<dakaEvent> dakaEvents1 = gson.fromJson(detail.toString(),
                                new TypeToken<List<dakaEvent>>(){}.getType());
                        Log.i(TAG, "dakaevents1: " + dakaEvents1.size());
                        for(dakaEvent dakaEvent : dakaEvents1) {
                            dakaEvents.add(dakaEvent);
                        }
                        fdAdapter.notifyDataSetChanged();
                    } else if (msg.equals("未找到打卡项")) {
                        Toast.makeText(ShowDakaActivity.this, "未查询到打卡项",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ShowDakaActivity.this, "好友打卡项：网络错误",
                        Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);

        Log.i(TAG, "dakaevents: " + dakaEvents.size());
        fdAdapter = new FriendDakaAdapter(ShowDakaActivity.this, dakaEvents);
        listView.setAdapter(fdAdapter);

    }
}