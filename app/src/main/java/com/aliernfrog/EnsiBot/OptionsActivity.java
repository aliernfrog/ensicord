package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aliernfrog.EnsiBot.utils.AppUtil;

public class OptionsActivity extends AppCompatActivity {
    ImageView goBack;
    LinearLayout chatOptions;
    CheckBox saveHistory;
    LinearLayout moreOptions;

    SharedPreferences config;
    SharedPreferences.Editor configEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        goBack = findViewById(R.id.options_goBack);
        chatOptions = findViewById(R.id.options_chat_linear);
        saveHistory = findViewById(R.id.options_chat_saveHistory);
        moreOptions = findViewById(R.id.options_more_linear);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();

        getConfig();
        setListeners();
    }

    void getConfig() {
        saveHistory.setChecked(config.getBoolean("saveHistory", true));
    }

    void changeBoolean(String name, Boolean value) {
        configEdit.putBoolean(name, value);
        configEdit.commit();
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(goBack, this::finish);
        AppUtil.handleOnPressEvent(chatOptions);
        saveHistory.setOnCheckedChangeListener((compoundButton, b) -> changeBoolean("saveHistory", b));
        AppUtil.handleOnPressEvent(moreOptions);
    }
}