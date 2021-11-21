package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;
import com.aliernfrog.EnsiBot.utils.WebUtil;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

@SuppressLint("CommitPrefEdits")
public class DlcApplyActivity extends AppCompatActivity {
    TextView info;

    SharedPreferences config;
    SharedPreferences dlc;
    SharedPreferences.Editor configEdit;
    SharedPreferences.Editor dlcEdit;
    Boolean debugMode = false;

    String dlcId;
    Boolean applyDefault;
    String dataPath;
    String ensiAvatarPath;
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
        debugMode = config.getBoolean("debugMode", false);

        dlcId = getIntent().getStringExtra("dlc_id");
        applyDefault = getIntent().getBooleanExtra("applyDefault", false);
        dataPath = getExternalFilesDir(".saved").toString();
        ensiAvatarPath = dataPath+"/ensi.png";

        devLog("DlcApplyActivity started");

        inform("Please wait..");
        Handler handler = new Handler();
        handler.postDelayed(this::getDlc, 1000);
    }

    public void getDlc() {
        if (dlcId != null) {
            getDlcFromWebsite();
        } else if (applyDefault) {
            getDlcFromFile();
        } else {
            devLog("no dlc to apply, finishing");
            finish();
        }
    }

    public void getDlcFromWebsite() {
        devLog("attempting to apply from website: "+dlcId);
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
            devLog(e.toString());
        }
    }

    public void getDlcFromFile() {
        devLog("attempting to apply from file");
        try {
            String content = FileUtil.readFileFromAssets(getApplicationContext(), "default.json");
            rawDlc = new JSONObject(content);
            applyDlc();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
            devLog(e.toString());
        }
    }

    public void applyDlc() {
        devLog("attempting to apply the dlc");
        try {
            if (rawDlc.has("words")) dlcEdit.putString("words", rawDlc.getString("words"));
            if (rawDlc.has("verbs")) dlcEdit.putString("verbs", rawDlc.getString("verbs"));
            if (rawDlc.has("concs")) dlcEdit.putString("concs", rawDlc.getString("concs"));
            if (rawDlc.has("types")) dlcEdit.putString("types", rawDlc.getString("types"));
            if (rawDlc.has("ensiSaveWords")) dlcEdit.putBoolean("ensiSaveWords", rawDlc.getBoolean("ensiSaveWords"));
            if (rawDlc.has("ensiName")) dlcEdit.putString("ensiName", rawDlc.getString("ensiName"));
            if (rawDlc.has("ensiAvatarUrl")) applyEnsiAvatar(rawDlc.getString("ensiAvatarUrl"));
            if (rawDlc.has("channelName")) dlcEdit.putString("channelName", rawDlc.getString("channelName"));
            if (rawDlc.has("background")) dlcEdit.putString("background", rawDlc.getString("background"));
            if (rawDlc.has("topBar")) dlcEdit.putString("topBar", rawDlc.getString("topBar"));
            if (rawDlc.has("title")) dlcEdit.putString("title", rawDlc.getString("title"));
            if (rawDlc.has("hint")) dlcEdit.putString("hint", rawDlc.getString("hint"));
            if (rawDlc.has("typingText")) dlcEdit.putString("typingText", rawDlc.getString("typingText"));
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
            devLog(e.toString());
        }
    }

    public void applyEnsiAvatar(String avatarUrl) {
        devLog("attempting to apply ensi avatar");
        try {
            File ensiAvatarFile = new File(ensiAvatarPath);
            Bitmap avatar = WebUtil.getBipmapFromURL(avatarUrl);
            OutputStream os = new BufferedOutputStream(new FileOutputStream(ensiAvatarFile));
            avatar.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
            devLog(e.toString());
        }
    }

    void finishApplying() {
        inform("Done!");
        Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        new Handler().postDelayed(() -> {
            finish();
            startActivity(intent);
        }, 1000);
    }

    void inform(String text) {
        info.setText(text);
    }

    void devLog(String text) {
        if (debugMode) AppUtil.devLog(text, getApplicationContext());
    }
}