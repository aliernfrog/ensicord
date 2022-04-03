package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;
import com.google.android.material.textfield.TextInputLayout;

@SuppressLint("CommitPrefEdits")
public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    LinearLayout avatarLinear;
    ImageView avatar;
    LinearLayout usernameLinear;
    TextInputLayout usernameInput;
    Button usernameConfirm;
    LinearLayout moreLinear;
    Button redirectDlcs;
    Button redirectOptions;

    SharedPreferences config;
    SharedPreferences.Editor configEdit;
    Boolean debugMode = false;

    String avatarPath;
    Integer REQUEST_PICK_AVATAR = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();
        debugMode = config.getBoolean("debugMode", false);

        avatarPath = getExternalFilesDir(".saved").toString()+"/avatar.png";

        toolbar = findViewById(R.id.profile_toolbar);
        avatarLinear = findViewById(R.id.profile_avatar_linear);
        avatar = findViewById(R.id.profile_avatar);
        usernameLinear = findViewById(R.id.profile_username_linear);
        usernameInput = findViewById(R.id.profile_username_input);
        usernameConfirm = findViewById(R.id.profile_username_confirm);
        moreLinear = findViewById(R.id.profile_more);
        redirectDlcs = findViewById(R.id.profile_dlcs);
        redirectOptions = findViewById(R.id.profile_options);

        devLog("ProfileActivity started");

        setListeners();
        updateOptions();
    }

    public void changeName(String name) {
        if (name.replace(" ","").equals("")) name = "Some frok";
        devLog("changing name to: "+name);
        configEdit.putString("username", name);
        configEdit.commit();
        Toast.makeText(getApplicationContext(), R.string.profile_username_changed, Toast.LENGTH_SHORT).show();
    }

    public void pickAvatar() {
        if (!hasPerms()) return;
        devLog("picking avatar");
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra("FILE_TYPE", "image/*");
        startActivityForResult(intent, REQUEST_PICK_AVATAR);
    }

    public void setAvatar(String path) {
        devLog("attempting to set avatar");
        try {
            FileUtil.copyFile(path, avatarPath);
            getAvatar();
            Toast.makeText(getApplicationContext(), R.string.profile_avatar_changed, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void getAvatar() {
        devLog("getting avatar");
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            Drawable userAvatar = Drawable.createFromPath(avatarPath);
            avatar.setImageDrawable(userAvatar);
        }
    }

    void updateOptions() {
        getAvatar();
        usernameInput.getEditText().setText(config.getString("username", "Some frok"));
    }

    void switchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void devLog(String text) {
        if (debugMode) AppUtil.devLog(text, getApplicationContext());
    }

    Boolean hasPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_AVATAR) {
                String path = data.getStringExtra("path");
                setAvatar(path);
            }
        }
    }

    void setListeners() {
        toolbar.setNavigationOnClickListener(v -> finish());
        AppUtil.handleOnPressEvent(avatarLinear, this::pickAvatar);
        AppUtil.handleOnPressEvent(usernameLinear);
        AppUtil.handleOnPressEvent(usernameConfirm, () -> changeName(Html.fromHtml(usernameInput.getEditText().getText().toString()).toString()));
        AppUtil.handleOnPressEvent(moreLinear);
        AppUtil.handleOnPressEvent(redirectDlcs, () -> switchActivity(DlcActivity.class));
        AppUtil.handleOnPressEvent(redirectOptions, () -> switchActivity(OptionsActivity.class));
    }
}