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
import com.example.YunDays.event.dayEvent;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ClassEventRequest {

    private static final String TAG = "Sqyl";
    private static Gson gson = new GsonBuilder().create();
    private static EventSQLiteOperation operation = new EventSQLiteOperation();

    public static void refreshClassEvent(Context context) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //刷新classEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_userID", belong_userID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/classevent/getClassEventByUserId";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    if (msg.equals("查询到课程")) {
                        JSONArray detail = response.getJSONArray("detail");
                        Log.i(TAG, "onResponse: " + detail.toString());
                        List<classEvent> classEvents = gson.fromJson(detail.toString(),
                                new TypeToken<List<classEvent>>(){}.getType());

                        if(!operation.deleteAllClassEvent(context)) {
                            Toast.makeText(context, "课程刷新失败", Toast.LENGTH_SHORT).show();
                        }
                        for (classEvent classEvent : classEvents) {
                            operation.insertClassEvent(context, classEvent);
                        }
                    } else if (msg.equals("未查询到课程")) {
                        Toast.makeText(context, "未查询到课程",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "课程：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void insertClassEvent(Context context, classEvent classEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //添加classEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("belong_userID", belong_userID);
            jsonObject.put("name", classEvent.getName());
            jsonObject.put("class_date", classEvent.getClass_date());
            jsonObject.put("class_teacher", classEvent.getClass_teacher());
            jsonObject.put("classRoom", classEvent.getClassRoom());
            jsonObject.put("begin_class", classEvent.getBegin_class());
            jsonObject.put("end_class", classEvent.getEnd_class());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/classevent/insertClassEvent";
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
                Toast.makeText(context, "课程：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void updateClassEvent(Context context, classEvent classEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //更改dayEvent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", classEvent.get_id());
            jsonObject.put("belong_userID", belong_userID);
            jsonObject.put("name", classEvent.getName());
            jsonObject.put("class_date", classEvent.getClass_date());
            jsonObject.put("class_teacher", classEvent.getClass_teacher());
            jsonObject.put("classRoom", classEvent.getClassRoom());
            jsonObject.put("begin_class", classEvent.getBegin_class());
            jsonObject.put("end_class", classEvent.getEnd_class());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/classevent/updateClassEvent";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String msg = response.getString("msg");
                    Log.i(TAG, "onResponse: " + response.toString());
                    if (msg.equals("修改成功")) {
                        Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, "修改失败",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "课程：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public static void deleteClassEvent(Context context, classEvent classEvent) {
        int belong_userID = operation.searchUserClass(context).getUserID();
        //删除classevent
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("_id", classEvent.get_id());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = RequestIP.IP_HOTSPOT + "/classevent/deleteClassEvent";
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
                Toast.makeText(context, "课程：网络错误", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
