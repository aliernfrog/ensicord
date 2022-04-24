package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.WebUtil;

import org.json.JSONArray;
import org.json.JSONObject;

public class DlcActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView errorText;
    ProgressBar loading;
    LinearLayout themeRoot;
    LinearLayout chatRoot;
    LinearLayout experimentalRoot;

    SharedPreferences config;
    Boolean debugMode = false;
    Boolean allowExperimentalDlcs = false;

    String url = "https://aliernfrog.repl.co";
    JSONArray rawDlcs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dlc);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        debugMode = config.getBoolean("debugMode", false);
        allowExperimentalDlcs = config.getBoolean("allowExperimentalDlcs", false);

        toolbar = findViewById(R.id.dlc_toolbar);
        errorText = findViewById(R.id.dlc_error);
        loading = findViewById(R.id.dlc_loading);
        themeRoot = findViewById(R.id.dlc_theme_root);
        chatRoot = findViewById(R.id.dlc_chat_root);
        experimentalRoot = findViewById(R.id.dlc_experimental_root);

        devLog("DlcActivity started");

        setListeners();

        Handler handler = new Handler();
        handler.postDelayed(this::getDlcs, 1000);
    }

    public void getDlcs() {
        devLog("attempting to fetch dlcs");
        try {
            JSONObject obj = new JSONObject();
            obj.put("type", "dlcGet");
            String res = WebUtil.doPostRequest(url, obj);
            rawDlcs = new JSONArray(res);
            loadDlcs();
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void loadDlcs() {
        devLog("attempting to load "+rawDlcs.length()+" dlcs");
        try {
            for (int i = 0; i < rawDlcs.length(); i++) {
                JSONObject current = rawDlcs.getJSONObject(i);
                LinearLayout root = chatRoot;
                if (current.getString("type").equals("theme")) root = themeRoot;
                if (current.getString("type").equals("experimental")) root = experimentalRoot;
                if (root.getVisibility() != View.VISIBLE) root.setVisibility(View.VISIBLE);
                if (root == experimentalRoot && !allowExperimentalDlcs) root.setVisibility(View.GONE);
                ViewGroup dlc = (ViewGroup) getLayoutInflater().inflate(R.layout.inflate_dlc, root, false);
                setDlcView(current, root, dlc);
            }
            loading.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void setDlcView(JSONObject object, LinearLayout root, ViewGroup dlc) {
        try {
            LinearLayout dlcLinear = dlc.findViewById(R.id.dlc_linear);
            ImageView thumbnailView = dlc.findViewById(R.id.dlc_thumbnail);
            TextView nameView = dlc.findViewById(R.id.dlc_name);
            TextView descView = dlc.findViewById(R.id.dlc_desc);
            String id = object.getString("_id");
            String name = object.getString("name");
            String desc = object.getString("description");
            nameView.setText(name);
            descView.setText(Html.fromHtml(desc));
            getDlcThumbnail(thumbnailView, object);
            AppUtil.handleOnPressEvent(dlcLinear, () -> applyDlc(id));
            root.addView(dlc);
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void getDlcThumbnail(ImageView thumbnaiView, JSONObject dlc) {
        try {
            if (dlc.has("thumbnailUrl")) {
                thumbnaiView.setImageBitmap(WebUtil.getBipmapFromURL(dlc.getString("thumbnailUrl")));
            } else {
                thumbnaiView.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    void applyDlc(String id) {
        Intent intent = new Intent(this, DlcApplyActivity.class);
        intent.putExtra("dlc_id", id);
        finish();
        startActivity(intent);
    }

    void informError(String err) {
        String fullText = getString(R.string.dlcs_somethingWentWrong)+":\n"+err;
        errorText.setText(fullText);
        errorText.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
    }

    void devLog(String text) {
        if (text.contains("Exception")) informError(text);
        if (debugMode) AppUtil.devLog(text, getApplicationContext());
    }

    void setListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
    }
}