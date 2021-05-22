package com.example.YunDays;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = "sqyl";
    private EditText register_account;
    private EditText register_password;
    private EditText register_username;
    private Button btn_gotoLogin;
    private Button btn_register_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_account = findViewById(R.id.register_account);
        register_password = findViewById(R.id.register_password);
        register_username = findViewById((R.id.register_username));
        Log.i(TAG, "onCreate: ");

        btn_gotoLogin = findViewById(R.id.btn_gotoLogin);
        btn_register_submit = findViewById(R.id.btn_register_submit);

        btn_register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userAccount = register_account.getText().toString().trim();
                String userPassword = register_password.getText().toString().trim();
                String username = register_username.getText().toString().trim();
                Log.i(TAG, "onClick: "+ username);
                if(userAccount.equals("") || userPassword.equals("") || username.equals("")) {
                    Toast.makeText(RegisterActivity.this, "请填写完整",
                            Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userAccount", userAccount);
                        jsonObject.put("userPassword", userPassword);
                        jsonObject.put("userName", username);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://192.168.31.220:8090/user/register";
                    RequestQueue requestQueue = Volley.newRequestQueue(RegisterActivity.this);
                    Log.i(TAG, "onClick: " + jsonObject.toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.i("Sqyl", "注册信息: " + response.toString());
                                String msg = response.getString("msg");
                                Toast.makeText(RegisterActivity.this, msg,
                                        Toast.LENGTH_SHORT).show();
                                if(msg.equals("注册成功")) {
                                    JSONObject detail = response.getJSONObject("detail");
                                    final String userAccount_toLogin =
                                            detail.getString("userAccount");
                                    btn_gotoLogin.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(RegisterActivity.this,
                                                    LoginActivity.class);
                                            intent.putExtra("userAccount", userAccount_toLogin);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("sqyl", "onErrorResponse: " + error.toString());
                            Toast.makeText(RegisterActivity.this, "网络出错",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }

            }
        });

        btn_gotoLogin.setOnClickListener(v ->
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }
}