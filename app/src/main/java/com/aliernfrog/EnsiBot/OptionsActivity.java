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
import android.os.Environment;
import android.text.Html;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import com.aliernfrog.EnsiBot.utils.FileUtil;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

@SuppressLint("CommitPrefEdits")
public class OptionsActivity extends AppCompatActivity implements PickiTCallbacks {
    ImageView avatar;
    EditText usernameInput;
    Button usernameConfirm;
    Button redirectDlcs;

    SharedPreferences config;
    SharedPreferences.Editor configEdit;

    String avatarPath;
    Integer PICK_AVATAR = 1;

    PickiT pickiT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        configEdit = config.edit();

        avatarPath = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.aliernfrog.EnsiBot/files/saved/avatar.png";

        avatar = findViewById(R.id.options_avatar);
        usernameInput = findViewById(R.id.options_username_input);
        usernameConfirm = findViewById(R.id.options_username_confirm);
        redirectDlcs = findViewById(R.id.options_dlcs);

        pickiT = new PickiT(this, this, this);

        setListeners();
        updateOptions();
    }

    void changeName(String name) {
        if (name.replace(" ","").equals("")) name = "Some frok";
        configEdit.putString("username", name);
        configEdit.commit();
        Toast.makeText(getApplicationContext(), R.string.options_username_changed, Toast.LENGTH_SHORT).show();
    }

    void switchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    void pickAvatar() {
        if (!hasPerms()) return;
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_AVATAR);
    }

    void setAvatar(String path) {
        try {
            FileUtil.copyFile(path, avatarPath);
            getAvatar();
            Toast.makeText(getApplicationContext(), R.string.options_avatar_changed, Toast.LENGTH_SHORT).show();
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
        usernameInput.setText(config.getString("username", "Some frok"));
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
            if (requestCode == PICK_AVATAR) {
                pickiT.getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }
    }

    void setListeners() {
        avatar.setOnClickListener(v -> pickAvatar());
        usernameConfirm.setOnClickListener(v -> changeName(Html.fromHtml(usernameInput.getText().toString()).toString()));
        redirectDlcs.setOnClickListener(v -> switchActivity(DlcActivity.class));
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
        switchActivity(SplashActivity.class);
        super.onBackPressed();
    }
}