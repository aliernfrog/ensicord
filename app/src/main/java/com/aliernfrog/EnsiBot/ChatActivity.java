package com.aliernfrog.EnsiBot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
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
    TextView channelTitle;
    ImageView options;
    ScrollView chatScroll;
    LinearLayout chatRoot;
    TextView channelHint;
    EditText chatInput;
    ImageView chatSend;

    String avatarPath;

    Drawable ensiAvatar;
    Drawable userAvatar;

    String ensiUsername = "<font color=yellow>Ensi</font>";
    String userUsername;

    SharedPreferences config;

    ArrayList<String> savedWords;
    ArrayList<String> savedVerbs;
    ArrayList<String> concs;
    ArrayList<String> types;

    Boolean[] saveChances = {false,false,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        avatarPath = Environment.getExternalStorageDirectory().toString()+"/Android/data/com.aliernfrog.EnsiBot/files/saved/avatar.png";

        ensiAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ensi);
        userAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user);

        channelTitle = findViewById(R.id.main_channel_name);
        options = findViewById(R.id.main_options);
        chatScroll = findViewById(R.id.main_chatScroll);
        chatRoot = findViewById(R.id.main_chatRoot);
        channelHint = findViewById(R.id.main_chat_hint);
        chatInput = findViewById(R.id.main_chatInput);
        chatSend = findViewById(R.id.main_chatSend);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);
        userUsername = config.getString("username", "Some frok");

        sendMessage(ensiAvatar, ensiUsername, "hi bro");

        getSavedWordsAndVerbs();
        getAvatar();
        getChannel();
        setListeners();
        options.setImageDrawable(userAvatar);
    }

    void sendMessage(@Nullable Drawable avatar, String username, String content) {
        if (content.replaceAll(" ", "").equals("")) return;
        ViewGroup message = (ViewGroup) getLayoutInflater().inflate(R.layout.message, chatRoot, false);
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

    void getChannel() {
        String name = "#"+config.getString("channelName", "general");
        String hint = getString(R.string.channelStart).replace("%NAME%", name);
        String inputHint = getString(R.string.sendMessage).replace("%NAME%", name);
        channelTitle.setText(name);
        channelHint.setText(hint);
        chatInput.setHint(inputHint);
    }

    void getAvatar() {
        File avatarFile = new File(avatarPath);
        if (avatarFile.exists()) {
            userAvatar = Drawable.createFromPath(avatarPath);
        }
    }

    void getSavedWordsAndVerbs() {
        String words = config.getString("savedWords", "");
        String verbs = config.getString("savedVerbs", "");
        savedWords = new ArrayList<>(Arrays.asList(words.split("\n")));
        savedVerbs = new ArrayList<>(Arrays.asList(verbs.split("\n")));
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