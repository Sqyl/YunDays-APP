package com.example.YunDays.ui.daka;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
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
import com.example.YunDays.request.RequestIP;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class SetDakaMsgActivity extends AppCompatActivity {

    private static final String TAG = "Sqyl";
    private Toolbar toolbar;
    private EditText record_dakamsg;
    private Button btn_record_dakamsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Intent intent = getIntent();
        int _id = intent.getIntExtra("_id", -1);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_daka_msg);
        toolbar = findViewById(R.id.toolbar_setDakaMsg);
        toolbar.setNavigationOnClickListener(v -> finish());
        record_dakamsg = findViewById(R.id.record_dakamsg);
        btn_record_dakamsg = findViewById(R.id.btn_record_dakamsg);
        btn_record_dakamsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                String dakamsg_date = c.get(Calendar.YEAR) + "-" + (c.get(Calendar.MONTH) + 1) + "-"
                        + c.get(Calendar.DAY_OF_MONTH);
                String dakamsg_time = c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("belong_dakaID", _id);
                    jsonObject.put("date", dakamsg_date);
                    jsonObject.put("time", dakamsg_time);
                    jsonObject.put("comment", record_dakamsg.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String url = RequestIP.IP_HOTSPOT + "/dakamsg/insertDakaMsg";
                RequestQueue requestQueue = Volley.newRequestQueue(SetDakaMsgActivity.this);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                        url, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String msg = response.getString("msg");
                            Log.i(TAG, "onResponse: " + response.toString());
                            if (msg.equals("添加成功")) {
                                Toast.makeText(SetDakaMsgActivity.this,
                                        "记录成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SetDakaMsgActivity.this, "记录失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetDakaMsgActivity.this,
                                "打卡：网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
                requestQueue.add(jsonObjectRequest);
                finish();
            }

        });
    }
}