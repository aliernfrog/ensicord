package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;

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
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

@SuppressLint("CommitPrefEdits")
public class ProfileActivity extends AppCompatActivity implements PickiTCallbacks {
    LinearLayout profileLinear;
    ImageView avatar;
    TextInputLayout usernameInput;
    Button usernameConfirm;
    Button redirectDlcs;

    SharedPreferences config;
    SharedPreferences.Editor configEdit;

    String avatarPath;
    Integer REQUEST_PICK_AVATAR = 1;

    PickiT pickiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();

        avatarPath = getExternalFilesDir("saved").toString()+"/avatar.png";

        profileLinear = findViewById(R.id.profile_profile_linear);
        avatar = findViewById(R.id.profile_avatar);
        usernameInput = findViewById(R.id.profile_username_input);
        usernameConfirm = findViewById(R.id.profile_username_confirm);
        redirectDlcs = findViewById(R.id.profile_dlcs);

        pickiT = new PickiT(this, this, this);

        setListeners();
        updateOptions();
    }

    void changeName(String name) {
        if (name.replace(" ","").equals("")) name = "Some frok";
        configEdit.putString("username", name);
        configEdit.commit();
        Toast.makeText(getApplicationContext(), R.string.profile_username_changed, Toast.LENGTH_SHORT).show();
    }

    void pickAvatar() {
        if (!hasPerms()) return;
        Intent intent = new Intent(this, FilePickerActivity.class);
        intent.putExtra("FILE_TYPE", "image/*");
        startActivityForResult(intent, REQUEST_PICK_AVATAR);
    }

    void setAvatar(String path) {
        try {
            FileUtil.copyFile(path, avatarPath);
            getAvatar();
            Toast.makeText(getApplicationContext(), R.string.profile_avatar_changed, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getAvatar() {
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
        AppUtil.handleOnPressEvent(profileLinear);
        AppUtil.handleOnPressEvent(avatar, this::pickAvatar);
        AppUtil.handleOnPressEvent(usernameConfirm, () -> changeName(Html.fromHtml(usernameInput.getEditText().getText().toString()).toString()));
        AppUtil.handleOnPressEvent(redirectDlcs, () -> switchActivity(DlcActivity.class));
    }

    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        setAvatar(path);
    }

    @Override
    public void onBackPressed() {
        pickiT.deleteTemporaryFile(this);
        finish();
        super.onBackPressed();
    }
}