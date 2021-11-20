package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Toast;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

@SuppressLint("CommitPrefEdits")
public class SplashActivity extends AppCompatActivity {
    String dataPath;

    SharedPreferences config;
    Boolean saveHistory = true;
    Boolean autoUpdate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataPath = getExternalFilesDir(".saved").getPath();

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        saveHistory = config.getBoolean("saveHistory", true);
        autoUpdate = config.getBoolean("autoUpdate", true);

        getUpdates();
        checkFiles();
        switchActivity();
    }

    void getUpdates() {
        if (!autoUpdate) return;
        try {
            AppUtil.getUpdates(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void checkFiles() {
        try {
            String historyPath = dataPath+"/history.json";
            String starboardPath = dataPath+"/starboard.json";
            File historyFile = new File(historyPath);
            File starboardFile = new File(starboardPath);
            if (!historyFile.exists()) FileUtil.saveFile(dataPath, historyFile.getName(), "[]");
            if (!starboardFile.exists()) FileUtil.saveFile(dataPath, starboardFile.getName(), "[]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void switchActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatHistoryPath", dataPath+"/history.json");
        intent.putExtra("saveNewMessages", saveHistory);
        startActivity(intent);
        finish();
    }
}