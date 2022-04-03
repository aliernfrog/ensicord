package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.AppUtil;

public class OptionsActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout otherOptions;
    Button redirectProfile;
    Button redirectDlcs;
    LinearLayout chatOptions;
    CheckBox saveHistory;
    LinearLayout moreOptions;
    CheckBox autoUpdate;
    CheckBox debugMode;
    LinearLayout experimentalOptions;
    CheckBox allowExperimentalDlcs;
    LinearLayout changelogLinear;
    TextView changelogText;

    String appVers;
    Integer appVersCode;

    SharedPreferences config;
    SharedPreferences update;
    SharedPreferences.Editor configEdit;

    Integer changelogClick = 0;
    Boolean restart = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        toolbar = findViewById(R.id.options_toolbar);
        otherOptions = findViewById(R.id.options_other_linear);
        redirectProfile = findViewById(R.id.options_profile);
        redirectDlcs = findViewById(R.id.options_dlcs);
        chatOptions = findViewById(R.id.options_chat_linear);
        saveHistory = findViewById(R.id.options_chat_saveHistory);
        moreOptions = findViewById(R.id.options_more_linear);
        autoUpdate = findViewById(R.id.options_more_autoUpdate);
        debugMode = findViewById(R.id.options_more_debugMode);
        experimentalOptions = findViewById(R.id.options_experimental_linear);
        allowExperimentalDlcs = findViewById(R.id.options_experimental_allowExperimentalDlcs);
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
        allowExperimentalDlcs.setChecked(config.getBoolean("allowExperimentalDlcs", false));
    }

    void changeBoolean(String name, Boolean value, Boolean requiresRestart) {
        configEdit.putBoolean(name, value);
        configEdit.commit();
        if (requiresRestart) restart = true;
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

    void restartApp() {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    void switchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void exit() {
        finish();
        if (restart) restartApp();
    }

    void setListeners() {
        toolbar.setNavigationOnClickListener(v -> exit());
        AppUtil.handleOnPressEvent(otherOptions);
        AppUtil.handleOnPressEvent(chatOptions);
        AppUtil.handleOnPressEvent(moreOptions);
        AppUtil.handleOnPressEvent(experimentalOptions);
        AppUtil.handleOnPressEvent(changelogLinear, () -> {
            changelogClick = changelogClick+1;
            if (changelogClick > 15) experimentalOptions.setVisibility(View.VISIBLE);
        });
        AppUtil.handleOnPressEvent(redirectProfile, () -> switchActivity(ProfileActivity.class));
        AppUtil.handleOnPressEvent(redirectDlcs, () -> switchActivity(DlcActivity.class));
        saveHistory.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("saveHistory", b, true));
        autoUpdate.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("autoUpdate", b, false));
        debugMode.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("debugMode", b, false));
        allowExperimentalDlcs.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("allowExperimentalDlcs", b, false));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exit();
    }
}