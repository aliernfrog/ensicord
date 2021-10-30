package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.FileUtil;
import com.aliernfrog.EnsiBot.utils.WebUtil;

import org.json.JSONObject;

@SuppressLint("CommitPrefEdits")
public class DlcApplyActivity extends AppCompatActivity {
    TextView info;

    SharedPreferences config;
    SharedPreferences dlc;
    SharedPreferences.Editor configEdit;
    SharedPreferences.Editor dlcEdit;

    String dlcId;
    String dataPath;
    String wordsFileName = "words.txt";
    String verbsFileName = "verbs.txt";
    String url = "https://aliernfrog.repl.co";

    JSONObject rawDlc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlc_apply);

        info = findViewById(R.id.dlcApply_info);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        dlc = getSharedPreferences("APP_DLC", MODE_PRIVATE);
        configEdit = config.edit();
        dlcEdit = dlc.edit();

        dlcId = getIntent().getStringExtra("dlc_id");
        dataPath = getExternalFilesDir("saved").toString();

        inform("Downloading DLC");
        Handler handler = new Handler();
        handler.postDelayed(this::getDlc, 1000);
    }

    void getDlc() {
        try {
            JSONObject object = new JSONObject();
            object.put("type", "dlcGet");
            object.put("dlc_id", dlcId);
            String res = WebUtil.doPostRequest(url, object);
            rawDlc = new JSONObject(res);
            applyDlc();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
        }
    }

    void applyDlc() {
        inform("Applying the DLC");
        try {
            if (rawDlc.has("words")) saveFile(wordsFileName, rawDlc.getString("words"));
            if (rawDlc.has("verbs")) saveFile(verbsFileName, rawDlc.getString("verbs"));
            if (rawDlc.has("concs")) dlcEdit.putString("concs", rawDlc.getString("concs"));
            if (rawDlc.has("types")) dlcEdit.putString("types", rawDlc.getString("types"));
            if (rawDlc.has("ensiName")) dlcEdit.putString("ensiName", rawDlc.getString("ensiName"));
            //TODO if (rawDlc.has("ensiAvatarUrl"))
            if (rawDlc.has("channelName")) dlcEdit.putString("channelName", rawDlc.getString("channelName"));
            if (rawDlc.has("background")) dlcEdit.putString("background", rawDlc.getString("background"));
            if (rawDlc.has("topBar")) dlcEdit.putString("topBar", rawDlc.getString("topBar"));
            if (rawDlc.has("title")) dlcEdit.putString("title", rawDlc.getString("title"));
            if (rawDlc.has("hint")) dlcEdit.putString("hint", rawDlc.getString("hint"));
            if (rawDlc.has("chatBox")) dlcEdit.putString("chatBox", rawDlc.getString("chatBox"));
            if (rawDlc.has("chatBoxHint")) dlcEdit.putString("chatBoxHint", rawDlc.getString("chatBoxHint"));
            if (rawDlc.has("chatBoxText")) dlcEdit.putString("chatBoxText", rawDlc.getString("chatBoxText"));
            if (rawDlc.has("message")) dlcEdit.putString("message", rawDlc.getString("message"));
            if (rawDlc.has("messageAuthor")) dlcEdit.putString("messageAuthor", rawDlc.getString("messageAuthor"));
            if (rawDlc.has("messageContent")) dlcEdit.putString("messageContent", rawDlc.getString("messageContent"));
            dlcEdit.commit();
            finishApplying();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
        }
    }

    void finishApplying() {
        inform("Applied the DLC!");
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new Handler().postDelayed(() -> {
            finish();
            startActivity(intent);
        }, 2000);
    }

    void inform(String text) {
        info.setText(text);
    }

    void saveFile(String name, String content) {
        try {
            FileUtil.saveFile(dataPath, name, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}