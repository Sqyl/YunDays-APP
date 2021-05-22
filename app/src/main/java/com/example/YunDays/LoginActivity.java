package com.example.YunDays;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.textclassifier.ConversationActions;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.userclass.UserClass;

public class LoginActivity extends AppCompatActivity {

    private EditText input_account;
    private EditText input_password;
    private Button btn_login;
    private Button btn_register;
    private EventSQLiteOperation operation = new EventSQLiteOperation();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!(operation.searchUserClass(LoginActivity.this) == null)) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);

        input_account = findViewById(R.id.input_account);
        input_password = findViewById(R.id.input_password);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        Intent intent = getIntent();
        String userAccount_fromRegister = intent.getStringExtra("userAccount");
        input_account.setText(userAccount_fromRegister);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String UserAccountStr = input_account.getText().toString().trim();
                String UserPasswordStr = input_password.getText().toString().trim();
                if(UserAccountStr.equals("") || UserPasswordStr.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名密码不能为空！",
                            Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("userAccount", UserAccountStr);
                        jsonObject.put("userPassword", UserPasswordStr);
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "http://192.168.31.220:8090/user/login";
                    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                            url, jsonObject, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String msg = response.getString("msg");
                                if (msg.equals("登录成功")) {
                                    JSONObject detail = response.getJSONObject("detail");
                                    String userAccount = detail.getString("userAccount");
                                    Integer userID = detail.getInt("userID");
                                    String userPassword = detail.getString("userPassword");
                                    String userName = detail.getString("userName");
                                    EventSQLiteOperation operation = new EventSQLiteOperation();
                                    operation.insertUserClass(LoginActivity.this, new UserClass(
                                            userID, userAccount, userPassword, userName));
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else if (msg.equals("用户名或密码错误")) {
                                    Toast.makeText(LoginActivity.this, "用户名或密码错误",
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(LoginActivity.this, "网络错误",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
        btn_register.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
        });
    }
}