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

import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint("CommitPrefEdits")
public class DlcApplyActivity extends AppCompatActivity {
    TextView info;

    SharedPreferences config;
    SharedPreferences.Editor configEdit;

    String dlcId;
    String dataPath;
    String wordsFileName = "words.txt";
    String verbsFileName = "verbs.txt";
    String url = "https://ensiapp.aliernfrog.repl.co";

    JSONObject rawDlc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlc_apply);

        info = findViewById(R.id.dlcApply_info);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();

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
        inform("Getting DLC type");
        try {
            String type = rawDlc.getString("type");
            switch(type) {
                case "chat":
                    applyChatDlc();
                    break;
                case "theme":
                    applyThemeDlc();
                    break;
                default:
                    inform("Not a valid DLC type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            inform(e.toString());
        }
    }

    void applyChatDlc() {
        inform("Applying chat DLC");
        try {
            String words = null;
            String verbs = null;
            String concs = null;
            String types = null;
            String ensiName = null;
            if (rawDlc.has("words")) words = rawDlc.getString("words");
            if (rawDlc.has("verbs")) verbs = rawDlc.getString("verbs");
            if (rawDlc.has("concs")) concs = rawDlc.getString("concs");
            if (rawDlc.has("types")) types = rawDlc.getString("types");
            if (rawDlc.has("ensiName")) ensiName = rawDlc.getString("ensiName");
            if (words != null) saveFile(wordsFileName, words);
            if (verbs != null) saveFile(verbsFileName, verbs);
            configEdit.putString("concs", concs);
            configEdit.putString("types", types);
            configEdit.putString("ensiName", ensiName);
            configEdit.commit();
            finishApplying();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
        }
    }

    void applyThemeDlc() {
        inform("Applying theme DLC");
        try {
            String background = rawDlc.getString("background");
            String topBar = rawDlc.getString("topBar");
            String title = rawDlc.getString("title");
            String hint = rawDlc.getString("hint");
            String chatBox = rawDlc.getString("chatBox");
            String chatBoxHint = rawDlc.getString("chatBoxHint");
            String chatBoxText = rawDlc.getString("chatBoxText");
            String message = rawDlc.getString("message");
            String messageAuthor = rawDlc.getString("messageAuthor");
            String messageContent = rawDlc.getString("messageContent");
            configEdit.putString("theme_background", background);
            configEdit.putString("theme_topBar", topBar);
            configEdit.putString("theme_title", title);
            configEdit.putString("theme_hint", hint);
            configEdit.putString("theme_chatBox", chatBox);
            configEdit.putString("theme_chatBoxHint", chatBoxHint);
            configEdit.putString("theme_chatBoxText", chatBoxText);
            configEdit.putString("theme_message", message);
            configEdit.putString("theme_message_author", messageAuthor);
            configEdit.putString("theme_message_content", messageContent);
            configEdit.commit();
            finishApplying();
        } catch (Exception e) {
            e.printStackTrace();
            inform(e.toString());
        }
    }

    void finishApplying() {
        inform("Applied the DLC!");
        Intent intent = new Intent(this, SplashActivity.class);
        Handler handler = new Handler();
        handler.postDelayed(() -> {
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