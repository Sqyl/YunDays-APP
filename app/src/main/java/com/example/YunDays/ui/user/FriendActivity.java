package com.example.YunDays.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.R;
import com.example.YunDays.adapter.friendAdapter;
import com.example.YunDays.event.Friend;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.request.RequestIP;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendActivity extends AppCompatActivity {

    private final String TAG = "Sqyl";
    private Toolbar toolbar;
    private ImageView imageView;
    private ListView listView;
    private List<Friend> friends = new ArrayList<>();
    private com.example.YunDays.adapter.friendAdapter friendAdapter;
    private static Gson gson = new GsonBuilder().create();
    private static EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(FriendActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        toolbar = findViewById(R.id.toolbar_friend);
        toolbar.setNavigationOnClickListener(v -> finish());
        imageView = findViewById(R.id.friend_add);
        imageView.setOnClickListener(v ->
                startActivity(new Intent(FriendActivity.this, AddFriendActivity.class)));

        listView = findViewById(R.id.friendList);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_UserId", belong_userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/friends/getFriendsByUserId";
        RequestQueue requestQueue = Volley.newRequestQueue(FriendActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("查询到好友")) {
                        JSONArray detail = response.getJSONArray("detail");
                        List<Friend> friends1 = gson.fromJson(detail.toString(),
                                new TypeToken<List<Friend>>(){}.getType());
                        Log.i(TAG, "onResponse: " + friends1.get(0).getBelong_UserId());
                        for(Friend friend : friends1) {
                            friends.add(friend);
                        }
                        friendAdapter.notifyDataSetChanged();
                    } else if (msg.equals("未查询到好友")) {
                        Toast.makeText(FriendActivity.this, "未查询到好友",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FriendActivity.this, "好友：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
        friendAdapter = new friendAdapter(FriendActivity.this, friends);
        listView.setAdapter(friendAdapter);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent1 = new Intent(FriendActivity.this, ShowDakaActivity.class);
            intent1.putExtra("FriendID", friends.get(position).getFriendId());
            startActivity(intent1);
        });
    }
}