package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.AppUtil;

public class OptionsActivity extends AppCompatActivity {
    ImageView goBack;
    LinearLayout chatOptions;
    CheckBox saveHistory;
    LinearLayout moreOptions;
    CheckBox autoUpdate;
    CheckBox debugMode;
    LinearLayout changelogLinear;
    TextView changelogText;

    String appVers;
    Integer appVersCode;

    SharedPreferences config;
    SharedPreferences update;
    SharedPreferences.Editor configEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        goBack = findViewById(R.id.options_goBack);
        chatOptions = findViewById(R.id.options_chat_linear);
        saveHistory = findViewById(R.id.options_chat_saveHistory);
        moreOptions = findViewById(R.id.options_more_linear);
        autoUpdate = findViewById(R.id.options_more_autoUpdate);
        debugMode = findViewById(R.id.options_more_debugMode);
        changelogLinear = findViewById(R.id.options_changelog_linear);
        changelogText = findViewById(R.id.options_changelog);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        update = getSharedPreferences("APP_UPDATE", MODE_PRIVATE);
        configEdit = config.edit();

        getConfig();
        getVersion();
        getChangelog();
        setListeners();
    }

    void getConfig() {
        saveHistory.setChecked(config.getBoolean("saveHistory", true));
        autoUpdate.setChecked(config.getBoolean("autoUpdate", true));
        debugMode.setChecked(config.getBoolean("debugMode", false));
    }

    void changeBoolean(String name, Boolean value) {
        configEdit.putBoolean(name, value);
        configEdit.commit();
    }

    void getVersion() {
        try {
            appVers = AppUtil.getVersName(getApplicationContext());
            appVersCode = AppUtil.getVersCode(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getChangelog() {
        String versInfo = "<b>Current version:</b> "+appVers+" ("+appVersCode+")";
        String _changelog = update.getString("updateChangelog", "No changelog has been found");
        String _changelogVers = update.getString("updateChangelogVersion", "-");
        String _full = _changelog+"<br /><br />"+"<b>Changelog version:</b> "+_changelogVers+"<br />"+versInfo;
        changelogText.setText(Html.fromHtml(_full));
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(goBack, this::finish);
        AppUtil.handleOnPressEvent(chatOptions);
        AppUtil.handleOnPressEvent(moreOptions);
        AppUtil.handleOnPressEvent(changelogLinear);
        saveHistory.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("saveHistory", b));
        autoUpdate.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("autoUpdate", b));
        debugMode.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("debugMode", b));
    }
}