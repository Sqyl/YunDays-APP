package com.example.YunDays.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.R;
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.ui.time_class.AddClassActivity;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddFriendActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText editText;
    private Button button;
    private int add_id;
    EventSQLiteOperation operation = new EventSQLiteOperation();
    private int belong_userID = operation.searchUserClass(AddFriendActivity.this).getUserID();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        toolbar = findViewById(R.id.toolbar_addFriend);
        toolbar.setNavigationOnClickListener(v -> finish());
        editText = findViewById(R.id.add_friend_id);
        button = findViewById(R.id.btn_add_friend);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject jsonObject = new JSONObject();
                try {
                    add_id = Integer.parseInt(String.valueOf(editText.getText()));
                    jsonObject.put("belong_UserId", belong_userID);
                    jsonObject.put("friendId", add_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = "http://192.168.43.105:8090/friends/insertFriend";
                RequestQueue requestQueue = Volley.newRequestQueue(AddFriendActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");
                            if (msg.equals("添加成功")) {
                                Toast.makeText(AddFriendActivity.this,
                                        "添加成功", Toast.LENGTH_SHORT).show();
                            } else if (msg.equals("添加失败")) {
                                Toast.makeText(AddFriendActivity.this, "添加失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddFriendActivity.this,
                                "添加好友：网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                finish();
            }
        });
    }
}