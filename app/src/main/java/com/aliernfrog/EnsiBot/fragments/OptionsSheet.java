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

    String avatarPath;

    SharedPreferences config;

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

        context = getActivity();

        if (context != null) avatarPath = context.getExternalFilesDir("saved").toString()+"/avatar.png";
        if (context != null) config = context.getSharedPreferences("APP_CONFIG", Context.MODE_PRIVATE);

        getUsername();
        getAvatar();
        setListeners();

        return view;
    }

    void getUsername() {
        String _username = config.getString("username", "Some frok");
        username.setText(_username);
    }

    void getAvatar() {
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            Drawable userAvatar = Drawable.createFromPath(avatarPath);
            avatar.setImageDrawable(userAvatar);
        }
    }

    void switchActivity(Class activity, Boolean dismiss) {
        Intent intent = new Intent(context, activity);
        startActivity(intent);
        if (dismiss) dismiss();
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(avatar_linear, () -> switchActivity(ProfileActivity.class, true));
        AppUtil.handleOnPressEvent(options, () -> switchActivity(ProfileActivity.class, true));
    }
}
