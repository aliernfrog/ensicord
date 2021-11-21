package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

@SuppressLint({"CommitPrefEdits", "CustomSplashScreen"})
public class SplashActivity extends AppCompatActivity {
    String dataPath;

    SharedPreferences config;
    Boolean saveHistory = true;
    Boolean autoUpdate = true;
    Boolean debugMode = false;

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
        debugMode = config.getBoolean("debugMode", false);

        devLog("SplashActivity started");

        getUpdates();
        checkFiles();
        switchActivity();
    }

    public void getUpdates() {
        if (!autoUpdate) return;
        devLog("attempting to get updates");
        try {
            AppUtil.getUpdates(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void checkFiles() {
        devLog("attempting to check files");
        try {
            String historyPath = dataPath+"/history.json";
            String starboardPath = dataPath+"/starboard.json";
            String logPath = dataPath+"/log.txt";
            File historyFile = new File(historyPath);
            File starboardFile = new File(starboardPath);
            File logFile = new File(logPath);
            if (!historyFile.exists()) FileUtil.saveFile(dataPath, historyFile.getName(), "[]");
            if (!starboardFile.exists()) FileUtil.saveFile(dataPath, starboardFile.getName(), "[]");
            if (!logFile.exists()) FileUtil.saveFile(dataPath, logFile.getName(), "");
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void switchActivity() {
        devLog("switching activity");
        Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatHistoryPath", dataPath+"/history.json");
        intent.putExtra("saveNewMessages", saveHistory);
        startActivity(intent);
        finish();
    }

    void devLog(String text) {
        if (debugMode) AppUtil.devLog(text, getApplicationContext());
    }
}