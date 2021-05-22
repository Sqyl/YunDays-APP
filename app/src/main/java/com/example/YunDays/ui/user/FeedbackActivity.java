package com.example.YunDays.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.YunDays.R;

public class FeedbackActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText feedback_content;
    private Button submit_feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        toolbar = findViewById(R.id.toolbar_feedback);
        toolbar.setNavigationOnClickListener(v -> finish());

        feedback_content = findViewById(R.id.feedback_content);
        submit_feedback = findViewById(R.id.submit_feedback);
        submit_feedback.setOnClickListener(v -> {
            Uri uri = Uri.parse("mailto:aaronwongsakura@outlook.com");
            String[] email = {"aaronwongsakura@outlook.com"};
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra(Intent.EXTRA_CC, email);
            intent.putExtra(Intent.EXTRA_SUBJECT, "云日程-意见反馈");
            intent.putExtra(Intent.EXTRA_TEXT, "正文：" + feedback_content.getText().toString());
            startActivity(intent.createChooser(intent, "请选择邮件类应用"));
        });
    }
}