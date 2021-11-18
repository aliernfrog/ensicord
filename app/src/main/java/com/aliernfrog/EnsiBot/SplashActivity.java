package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

@SuppressLint("CommitPrefEdits")
public class SplashActivity extends AppCompatActivity {
    String dataPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataPath = getExternalFilesDir(".saved").getPath();

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
        startActivity(intent);
        finish();
    }
}