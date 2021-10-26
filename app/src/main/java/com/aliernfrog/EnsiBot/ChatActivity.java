package com.aliernfrog.EnsiBot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.utils.EnsiUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@SuppressLint("CommitPrefEdits")
public class ChatActivity extends AppCompatActivity {
    ConstraintLayout background;
    LinearLayout topBar;
    TextView channelTitle;
    ImageView options;
    ScrollView chatScroll;
    LinearLayout chatRoot;
    TextView channelHint;
    LinearLayout chatBox;
    EditText chatInput;
    ImageView chatSend;

    SharedPreferences config;

    String avatarPath;

    Drawable ensiAvatar;
    Drawable userAvatar;

    String ensiUsername;
    String userUsername;

    ArrayList<String> savedWords;
    ArrayList<String> savedVerbs;
    ArrayList<String> concs;
    ArrayList<String> types;

    String messageColor;
    String messageAuthorColor;
    String messageContentColor;

    Boolean[] saveChances = {false,false,false,false,false,false,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        avatarPath = getExternalFilesDir("saved").toString()+"/avatar.png";

        ensiAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ensi);
        userAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user);

        background = findViewById(R.id.main_constraint);
        topBar = findViewById(R.id.main_topBar);
        channelTitle = findViewById(R.id.main_channel_name);
        options = findViewById(R.id.main_options);
        chatScroll = findViewById(R.id.main_chatScroll);
        chatRoot = findViewById(R.id.main_chatRoot);
        channelHint = findViewById(R.id.main_chat_hint);
        chatBox = findViewById(R.id.main_chatBox);
        chatInput = findViewById(R.id.main_chatInput);
        chatSend = findViewById(R.id.main_chatSend);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        ensiUsername = config.getString("ensiUsername", "<font color=yellow>Ensi</font>");
        userUsername = config.getString("username", "Some frok");

        getSavedWordsAndVerbs();
        getAvatar();
        getChannel();
        getDlcTheme();
        setListeners();

        sendMessage(ensiAvatar, ensiUsername, "hi bro");
    }

    void sendMessage(@Nullable Drawable avatar, String username, String content) {
        if (content.replaceAll(" ", "").equals("")) return;
        ViewGroup message = (ViewGroup) getLayoutInflater().inflate(R.layout.inflate_message, chatRoot, false);
        LinearLayout messageView = message.findViewById(R.id.message_linear);
        ImageView avatarView = message.findViewById(R.id.message_author_avatar);
        TextView usernameView = message.findViewById(R.id.message_author_username);
        TextView contentView = message.findViewById(R.id.message_content);
        if (avatar != null) avatarView.setImageDrawable(avatar);
        usernameView.setText(Html.fromHtml(username));
        usernameView.setOnClickListener(v -> mention(usernameView.getText().toString()));
        contentView.setText(Html.fromHtml(content));
        messageView.setOnLongClickListener(v -> {
            deleteMessage(message);
            return true;
        });

        messageView.setBackgroundColor(Color.parseColor(messageColor));
        usernameView.setTextColor(Color.parseColor(messageAuthorColor));
        contentView.setTextColor(Color.parseColor(messageContentColor));

        chatRoot.addView(message);
        scrollToBottom();
        onMessage(username, content);
    }

    void deleteMessage(ViewGroup message) {
        chatRoot.removeView(message);
    }

    void sendStory(String username, String content) {
        sendMessage(ensiAvatar, ensiUsername, EnsiUtil.buildMessage(username, content, savedWords, savedVerbs, concs, types));
        Random random = new Random();
        Boolean doSave = saveChances[random.nextInt(saveChances.length)];
        if (doSave) {
            String[] args = content.split(" ");
            String word = args[random.nextInt(args.length)];
            saveWord(word);
        }
    }

    void mention(String username) {
        String currentText = chatInput.getText().toString();
        String mention = EnsiUtil.buildMention(username);
        String finalText = currentText+mention;
        chatInput.setText(finalText);
        chatInput.setSelection(chatInput.length());
    }

    void scrollToBottom() {
        Handler handler = new Handler();
        handler.postDelayed(() -> chatScroll.fullScroll(View.FOCUS_DOWN), 100);
    }

    void onMessage(String username, String content) {
        if (username.equals(ensiUsername) || username.equals("")) return;
        Handler handler = new Handler();
        handler.postDelayed(() -> sendStory(username, content), 150);
    }

    void getSavedWordsAndVerbs() {
        String words = config.getString("savedWords", "");
        String verbs = config.getString("savedVerbs", "");
        String _concs = config.getString("concs", null);
        String _types = config.getString("types", null);
        String ensiName = config.getString("ensiName", null);
        savedWords = new ArrayList<>(Arrays.asList(words.split("\n")));
        savedVerbs = new ArrayList<>(Arrays.asList(verbs.split("\n")));
        if (_concs != null) concs = new ArrayList<>(Arrays.asList(_concs.split("\n")));
        if (_types != null) types = new ArrayList<>(Arrays.asList(_types.split("\n")));
        if (ensiName != null) ensiUsername = ensiName;
    }

    void getAvatar() {
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            userAvatar = Drawable.createFromPath(avatarPath);
        }
        options.setImageDrawable(userAvatar);
    }

    void getChannel() {
        String name = "#"+config.getString("channelName", "general");
        String hint = getString(R.string.channelStart).replace("%NAME%", name);
        String inputHint = getString(R.string.sendMessage).replace("%NAME%", name);
        channelTitle.setText(name);
        channelHint.setText(hint);
        chatInput.setHint(inputHint);
    }

    void getDlcTheme() {
        String backgroundColor = config.getString("theme_background", "#000000");
        String topBarColor = config.getString("theme_topBar", "#FF18191D");
        String titleColor = config.getString("theme_title", "#FFFFFF");
        String hintColor = config.getString("theme_hint", "#8C8C8C");
        String chatBoxColor = config.getString("theme_chatBox", "#FF18191D");
        String chatBoxHintColor = config.getString("theme_chatBoxHint", "#636363");
        String chatBoxTextColor = config.getString("theme_chatBoxText", "#FFFFFF");
        messageColor = config.getString("theme_message", "#00000000");
        messageAuthorColor = config.getString("theme_message_author", "#FFFFFF");
        messageContentColor = config.getString("theme_message_content", "#DDDDDD");
        background.setBackgroundColor(Color.parseColor(backgroundColor));
        topBar.setBackgroundColor(Color.parseColor(topBarColor));
        channelTitle.setTextColor(Color.parseColor(titleColor));
        channelHint.setTextColor(Color.parseColor(hintColor));
        chatBox.setBackgroundColor(Color.parseColor(chatBoxColor));
        chatInput.setHintTextColor(Color.parseColor(chatBoxHintColor));
        chatInput.setTextColor(Color.parseColor(chatBoxTextColor));
    }

    void saveWord(String word) {
        word = word.toLowerCase();
        try {
            if (savedWords.contains(word) || savedVerbs.contains(word)) return;
            String folder = getExternalFilesDir("saved").toString();
            boolean isVerb = word.endsWith("ed");
            if (!isVerb) {
                savedWords.add(word);
                FileUtil.saveFile(folder, "words.txt", EnsiUtil.listJoin("\n", savedWords));
            } else {
                savedVerbs.add(word);
                FileUtil.saveFile(folder, "verbs.txt", EnsiUtil.listJoin("\n", savedVerbs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void switchActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        finish();
        startActivity(intent);
    }

    void setListeners() {
        options.setOnClickListener(v -> switchActivity(OptionsActivity.class));

        chatSend.setOnClickListener(v -> {
            sendMessage(userAvatar, userUsername, chatInput.getText().toString());
            chatInput.setText("");
        });
    }
}