package com.example.YunDays.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.YunDays.LoginActivity;
import com.example.YunDays.R;
import com.example.YunDays.sqlite.EventSQLiteOperation;
import com.example.YunDays.userclass.UserClass;

public class UserInformationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private UserClass userClass;
    private TextView userAccount_show;
    private TextView userPassword_show;
    private TextView userName_show;
    private Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        EventSQLiteOperation operation = new EventSQLiteOperation();
        userClass = operation.searchUserClass(UserInformationActivity.this);


        toolbar = findViewById(R.id.toolbar_userInformation);
        toolbar.setNavigationOnClickListener(v -> finish());

        userAccount_show = findViewById(R.id.userAccount_show);
        userAccount_show.setText(userClass.getUserAccount());
        userPassword_show = findViewById(R.id.userPassword_show);
        userPassword_show.setText(userClass.getUserPassword());
        userName_show = findViewById(R.id.userName_show);
        userName_show.setText(userClass.getUserName());

        btn_logout = findViewById(R.id.btn_logout);
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(operation.deleteUserClass(UserInformationActivity.this)) {
                    startActivity(new Intent(UserInformationActivity.this, LoginActivity.class));
                    finish();
                }
            }
        });
    }
}