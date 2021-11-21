package com.aliernfrog.EnsiBot.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliernfrog.EnsiBot.ChatActivity;
import com.aliernfrog.EnsiBot.LogsActivity;
import com.aliernfrog.EnsiBot.OptionsActivity;
import com.aliernfrog.EnsiBot.ProfileActivity;
import com.aliernfrog.EnsiBot.R;
import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;

public class OptionsSheet extends BottomSheetDialogFragment {
    LinearLayout avatar_linear;
    ImageView avatar;
    TextView username;
    LinearLayout starboard;
    LinearLayout options;
    TextView viewLogs;

    String avatarPath;

    SharedPreferences config;
    Boolean debugMode = false;

    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_options, container, false);

        avatar_linear = view.findViewById(R.id.optionssheet_avatar);
        avatar = view.findViewById(R.id.optionssheet_avatar_avatar);
        username = view.findViewById(R.id.optionssheet_avatar_username);
        starboard = view.findViewById(R.id.optionssheet_starboard);
        options = view.findViewById(R.id.optionssheet_options);
        viewLogs = view.findViewById(R.id.optionssheet_logs);

        context = getActivity();

        if (context != null) avatarPath = context.getExternalFilesDir(".saved").toString()+"/avatar.png";
        if (context != null) config = context.getSharedPreferences("APP_CONFIG", Context.MODE_PRIVATE);
        debugMode = config.getBoolean("debugMode", false);
        if (debugMode) viewLogs.setVisibility(View.VISIBLE);

        devLog("OptionsSheet started");

        getUsername();
        getAvatar();
        setListeners();

        return view;
    }

    public void getUsername() {
        devLog("attempting to get username");
        String _username = config.getString("username", "Some frok");
        username.setText(_username);
    }

    public void getAvatar() {
        devLog("attempting to get avatar");
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            Drawable userAvatar = Drawable.createFromPath(avatarPath);
            avatar.setImageDrawable(userAvatar);
        }
    }

    public void switchStarboard() {
        devLog("starting starboard");
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("chatHistoryPath", context.getExternalFilesDir(".saved").getPath()+"/starboard.json");
        intent.putExtra("sendMessageAllowed", false);
        intent.putExtra("isStarboard", true);
        startActivity(intent);
        dismiss();
    }

    void switchActivity(Class activity) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        dismiss();
    }

    void devLog(String text) {
        if (debugMode) AppUtil.devLog(text, context.getApplicationContext());
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(avatar_linear, () -> switchActivity(ProfileActivity.class));
        AppUtil.handleOnPressEvent(starboard, this::switchStarboard);
        AppUtil.handleOnPressEvent(options, () -> switchActivity(OptionsActivity.class));
        AppUtil.handleOnPressEvent(viewLogs, () -> switchActivity(LogsActivity.class));
    }
}
