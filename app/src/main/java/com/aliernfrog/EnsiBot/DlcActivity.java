package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
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

    String url = "https://ensiapp.aliernfrog.repl.co";
    JSONArray rawDlcs;

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
            obj.put("type", "dlcGet");
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
            LinearLayout dlcLinear = dlc.findViewById(R.id.dlc_linear);
            TextView nameView = dlc.findViewById(R.id.dlc_name);
            TextView descView = dlc.findViewById(R.id.dlc_desc);
            String id = object.getString("_id");
            String name = object.getString("name");
            String desc = object.getString("description");
            nameView.setText(name);
            descView.setText(Html.fromHtml(desc));
            dlcLinear.setOnClickListener(v -> applyDlc(id));
            root.addView(dlc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void applyDlc(String id) {
        Intent intent = new Intent(this, DlcApplyActivity.class);
        intent.putExtra("dlc_id", id);
        finish();
        startActivity(intent);
    }
}