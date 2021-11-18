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
    String dataPath;

    SharedPreferences config;
    Boolean saveHistory = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataPath = getExternalFilesDir(".saved").getPath();

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        saveHistory = config.getBoolean("saveHistory", true);

        checkFiles();
        switchActivity();
    }

    void checkFiles() {
        try {
            String historyPath = dataPath+"/history.json";
            File historyFile = new File(historyPath);
            if (!historyFile.exists()) FileUtil.saveFile(dataPath, historyFile.getName(), "[]");
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