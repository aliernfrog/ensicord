package com.aliernfrog.EnsiBot;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.aliernfrog.EnsiBot.fragments.OptionsSheet;
import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.ChatUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

public class ChatActivity extends AppCompatActivity {
    ConstraintLayout background;
    LinearLayout topBar;
    TextView channelTitle;
    ImageView avatar;
    ScrollView chatScroll;
    LinearLayout chatRoot;
    TextView channelHint;
    LinearLayout chatBox;
    EditText chatInput;
    ImageView chatSend;

    SharedPreferences config;
    SharedPreferences dlc;

    String avatarPath;

    Drawable ensiAvatar;
    Drawable userAvatar;

    String ensiUsername;
    String userUsername;

    String suggestionColor;
    String suggestionTextColor;
    String messageColor;
    String messageAuthorColor;
    String messageContentColor;

    String chatHistoryPath;
    Boolean sendMessageAllowed;
    Boolean saveNewMessages;
    Boolean saveNewWords;
    Boolean isStarboard;
    JSONArray chatHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        avatarPath = getExternalFilesDir("saved").toString()+"/avatar.png";

        ensiAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ensi);
        userAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user);

        background = findViewById(R.id.chat_constraint);
        topBar = findViewById(R.id.chat_topBar);
        channelTitle = findViewById(R.id.chat_channel_name);
        avatar = findViewById(R.id.chat_options);
        chatScroll = findViewById(R.id.chat_chatScroll);
        chatRoot = findViewById(R.id.chat_chatRoot);
        channelHint = findViewById(R.id.chat_chat_hint);
        chatBox = findViewById(R.id.chat_chatBox);
        chatInput = findViewById(R.id.chat_chatInput);
        chatSend = findViewById(R.id.chat_chatSend);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        dlc = getSharedPreferences("APP_DLC", MODE_PRIVATE);
        ensiUsername = dlc.getString("ensiName", "<font color=yellow>Ensi</font>");
        userUsername = config.getString("username", "Some frok");

        getDlcTheme();
        getChannel(null);
        getChatOptions();
        getAvatar();
        loadChatHistory();
        setListeners();
    }

    void sendMessage(Drawable avatar, String author, String content) {
        if (content.replaceAll(" ", "").equals("")) return;
        try {
            JSONObject data = new JSONObject()
                    .put("type", "message")
                    .put("author", author)
                    .put("content", content);
            ViewGroup message = (ViewGroup) ChatUtil.loadMessage(data, chatRoot, getLayoutInflater());
            LinearLayout messageView = message.findViewById(R.id.message_linear);
            ImageView avatarView = message.findViewById(R.id.message_author_avatar);
            TextView authorView = message.findViewById(R.id.message_author_username);
            TextView contentView = message.findViewById(R.id.message_content);
            avatarView.setImageDrawable(avatar);
            messageView.setBackgroundColor(Color.parseColor(messageColor));
            authorView.setTextColor(Color.parseColor(messageAuthorColor));
            contentView.setTextColor(Color.parseColor(messageContentColor));
            chatRoot.addView(message);
            scrollToBottom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void sendSuggestion(@Nullable Drawable icon, String title) {
        try {
            JSONObject data = new JSONObject()
                    .put("type", "suggestion")
                    .put("title", title);
            if (icon != null) data.put("icon", icon);
            ViewGroup suggestion = (ViewGroup) ChatUtil.loadMessage(data, chatRoot, getLayoutInflater());
            LinearLayout suggestionLinear = suggestion.findViewById(R.id.suggestion_linear);
            TextView suggestionText = suggestion.findViewById(R.id.suggestion_title);
            suggestionLinear.setBackgroundColor(Color.parseColor(suggestionColor));
            suggestionText.setTextColor(Color.parseColor(suggestionTextColor));
            chatRoot.addView(suggestion);
            scrollToBottom();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getChannel(@Nullable String name) {
        if (name == null) name = dlc.getString("channelName", "#general");
        String hint = getString(R.string.channelStart).replace("%NAME%", name);
        String inputHint = getString(R.string.sendMessage).replace("%NAME%", name);
        channelTitle.setText(name);
        channelHint.setText(hint);
        chatInput.setHint(inputHint);
    }

    void getChatOptions() {
        try {
            chatHistoryPath = getIntent().getStringExtra("chatHistoryPath");
            sendMessageAllowed = getIntent().getBooleanExtra("sendMessageAllowed", true);
            saveNewMessages = getIntent().getBooleanExtra("saveNewMessages", true);
            saveNewWords = getIntent().getBooleanExtra("saveNewWords", true);
            isStarboard = getIntent().getBooleanExtra("isStarboard", false);
            if (chatHistoryPath != null) chatHistory = new JSONArray(FileUtil.readFile(chatHistoryPath));
            if (chatHistoryPath == null) chatHistory = new JSONArray("[]");
            applyChatOptions();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    void applyChatOptions() {
        if (isStarboard) {
            avatar.setVisibility(View.GONE);
            getChannel(getString(R.string.starboard));
        }
        if (!sendMessageAllowed) {
            chatInput.setHint(R.string.sendMessageDisabled);
            chatInput.setEnabled(false);
            chatSend.setVisibility(View.GONE);
        }
    }

    void getAvatar() {
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            userAvatar = Drawable.createFromPath(avatarPath);
        }
        avatar.setImageDrawable(userAvatar);
    }

    void loadChatHistory() {
        try {
            for (int i = 0; i < chatHistory.length(); i++) {
                JSONObject message = chatHistory.getJSONObject(i);
                String author = message.getString("author");
                String content = message.getString("content");
                sendMessage(null, author, content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getDlcTheme() {
        String backgroundColor = dlc.getString("background", "#000000");
        String topBarColor = dlc.getString("topBar", "#FF18191D");
        String titleColor = dlc.getString("title", "#FFFFFF");
        String hintColor = dlc.getString("hint", "#8C8C8C");
        String chatBoxColor = dlc.getString("chatBox", "#FF18191D");
        String chatBoxHintColor = dlc.getString("chatBoxHint", "#636363");
        String chatBoxTextColor = dlc.getString("chatBoxText", "#FFFFFF");
        suggestionColor = dlc.getString("suggestion", "#FF18191D");
        suggestionTextColor = dlc.getString("suggestionText", "#FFFFFF");
        messageColor = dlc.getString("message", "#00000000");
        messageAuthorColor = dlc.getString("messageAuthor", "#FFFFFF");
        messageContentColor = dlc.getString("messageContent", "#DDDDDD");
        background.setBackgroundColor(Color.parseColor(backgroundColor));
        topBar.setBackgroundColor(Color.parseColor(topBarColor));
        channelTitle.setTextColor(Color.parseColor(titleColor));
        channelHint.setTextColor(Color.parseColor(hintColor));
        chatBox.setBackgroundColor(Color.parseColor(chatBoxColor));
        chatInput.setHintTextColor(Color.parseColor(chatBoxHintColor));
        chatInput.setTextColor(Color.parseColor(chatBoxTextColor));
    }

    void scrollToBottom() {
        Handler handler = new Handler();
        handler.postDelayed(() -> chatScroll.fullScroll(View.FOCUS_DOWN), 100);
    }

    void openOptionsSheet() {
        OptionsSheet optionsSheet = new OptionsSheet();
        optionsSheet.show(getSupportFragmentManager(), "options_sheet");
    }

    void setListeners() {
        avatar.setOnClickListener(v -> openOptionsSheet());

        AppUtil.handleOnPressEvent(chatSend, () -> {
            sendMessage(userAvatar, userUsername, chatInput.getText().toString());
            chatInput.setText("");
        });
    }
}
