package com.example.YunDays.request;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.event.classEvent;
import com.example.YunDays.event.dakaEvent;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DakaEventRequest {

    private static final String TAG = "Sqyl";
    private static Gson gson = new GsonBuilder().create();
    private static EventSQLiteOperation operation = new EventSQLiteOperation();

    public static void refreshDakaEvent(Context context) {
        int belong_userID = operation.searchUserClass(context).getUserID();
//刷新dakaEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_userID", belong_userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/dakaevent/getDakaEventByUserId";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.i(TAG, "onResponse: 1");
                    String msg = response.getString("msg");
                    if (msg.equals("查询到打卡项")) {
                        JSONArray detail = response.getJSONArray("detail");
                        Log.i(TAG, "onResponse: " + detail.toString());
                        List<dakaEvent> dakaEvents = gson.fromJson(detail.toString(),
                                new TypeToken<List<dakaEvent>>(){}.getType());


                        if(!operation.deleteAllDakaEvent(context)) {
                            Toast.makeText(context, "打卡项刷新失败", Toast.LENGTH_SHORT).show();
                        }
                        for (dakaEvent dakaEvent : dakaEvents) {
                            operation.insertDakaEvent(context, dakaEvent);
                        }
                    } else if (msg.equals("未查询到打卡项")) {
                        Toast.makeText(context, "未查询到打卡项",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: 2");
                Toast.makeText(context, "打卡项：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void insertDakaEvent(Context context, dakaEvent dakaEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //添加classEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_userID", belong_userID);
            jsonObject.put("name", dakaEvent.getName());
            jsonObject.put("type", dakaEvent.getType());
            jsonObject.put("last_days", dakaEvent.getLast_days());
            jsonObject.put("daka_days", dakaEvent.getDaka_days());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/dakaevent/insertDakaEvent";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("添加成功")) {
                        Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "添加失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "打卡：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void updateDakaEvent(Context context, dakaEvent dakaEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //更改dayEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", dakaEvent.get_id());
            jsonObject.put("belong_userID", belong_userID);
            jsonObject.put("name", dakaEvent.getName());
            jsonObject.put("type", dakaEvent.getType());
            jsonObject.put("last_days", dakaEvent.getLast_days());
            jsonObject.put("daka_days", dakaEvent.getDaka_days());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/dakaevent/updateDakaEvent";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    Log.i(TAG, "onResponse: " + response.toString());
                    if (msg.equals("修改成功")) {
                        Toast.makeText(context, "打卡成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "打卡失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "打卡：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void deleteDakaEvent(Context context, dakaEvent dakaEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //删除classevent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", dakaEvent.get_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/dakaevent/deleteDakaEvent";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    Log.i(TAG, "onResponse: " + response.toString());
                    if (msg.equals("删除成功")) {
                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "删除失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "打卡：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
