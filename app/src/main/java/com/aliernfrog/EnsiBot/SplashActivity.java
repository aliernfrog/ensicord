package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

@SuppressLint("CommitPrefEdits")
public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        switchActivity();
    }

    void switchActivity() {
        Intent intent = new Intent(this, OldChatActivity.class);
        startActivity(intent);
        finish();
    }
}