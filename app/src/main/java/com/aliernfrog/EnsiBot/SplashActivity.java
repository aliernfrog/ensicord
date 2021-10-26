package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

@SuppressLint("CommitPrefEdits")
public class SplashActivity extends AppCompatActivity {
    String dataPath;
    String wordsPath;
    String verbsPath;

    String wordsFileName = "words.txt";
    String verbsFileName = "verbs.txt";

    String defaultWords = "me\nyou\nwe\nthey\nalierns\nindinibee\nbees\nmomes\nfrogs\nmouse\nchicken\nfurries\nfrog\nExi's basement\nfree candies\nensi\nvan\nlaptop\nmarchmilos\nmouse\nuwu\nhackers\n\uD83D\uDE10\ntryhards\nmohamme\n\uD83C\uDDE7\uD83C\uDDF7\nat 3am\nat 3am *gone wrong*\ndaily\nopa\nfrok\nalizade\ndislike\nreply\ninfini\nmohamet\nsuicide\n ";
    String defaultVerbs = "went to\nate\ndrove\nkidnapped\ntalked\nhacked\ngermanized\nmoused\nstalked\ntrapped\nraped\nexported\nadded\nsnorted\nliked\nred\nmohammed\n ";

    SharedPreferences config;
    SharedPreferences.Editor configEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        dataPath = getExternalFilesDir("saved").toString();
        wordsPath = dataPath+"/"+wordsFileName;
        verbsPath = dataPath+"/"+verbsFileName;

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();

        checkFiles();
    }

    void checkFiles() {
        File wordsFile = new File(wordsPath);
        File verbsFile = new File(verbsPath);
        if (!wordsFile.exists()) saveFile(wordsFileName, defaultWords);
        if (!verbsFile.exists()) saveFile(verbsFileName, defaultVerbs);
        getSavedWordsAndVerbs();
    }

    void getSavedWordsAndVerbs() {
        try {
            String savedWords = FileUtil.readFile(wordsPath);
            String savedVerbs = FileUtil.readFile(verbsPath);
            configEdit.putString("savedWords", savedWords);
            configEdit.putString("savedVerbs", savedVerbs);
            configEdit.commit();
            switchActivity();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void saveFile(String name, String content) {
        try {
            FileUtil.saveFile(dataPath, name, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void switchActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
        finish();
    }
}