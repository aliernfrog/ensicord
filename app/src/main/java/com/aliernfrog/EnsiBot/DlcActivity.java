package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.WebUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class DlcActivity extends AppCompatActivity {
    LinearLayout root;
    ProgressBar loading;

    JSONArray rawDlcs;

    String url = "https://ensibot-discord.aliernfrog.repl.co";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlc);

        root = findViewById(R.id.dlc_root);
        loading = findViewById(R.id.dlc_loading);

        Handler handler = new Handler();
        handler.postDelayed(this::getDlcs, 1000);
    }

    void getDlcs() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", "getDlc");
            String res = WebUtil.doPostRequest(url, obj);
            rawDlcs = new JSONArray(res);
            loadDlcs();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void loadDlcs() {
        try {
            for (int i = 0; i < rawDlcs.length(); i++) {
                JSONObject current = rawDlcs.getJSONObject(i);
                ViewGroup dlc = (ViewGroup) getLayoutInflater().inflate(R.layout.dlc, root, false);
                setDlcView(current, dlc);
            }
            loading.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setDlcView(JSONObject object, ViewGroup dlc) {
        try {
            TextView nameView = dlc.findViewById(R.id.dlc_name);
            TextView wordsView = dlc.findViewById(R.id.dlc_words);
            TextView verbsView = dlc.findViewById(R.id.dlc_verbs);
            TextView idView = dlc.findViewById(R.id.dlc_id);
            String name = object.getString("name");
            String words = object.getString("words");
            String verbs = object.getString("verbs");
            String id = object.getString("_id");
            nameView.setText(name);
            wordsView.setText(words);
            verbsView.setText(verbs);
            idView.setText(id);
            root.addView(dlc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}