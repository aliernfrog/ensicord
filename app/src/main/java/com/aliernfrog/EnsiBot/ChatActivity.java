package com.aliernfrog.EnsiBot;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

@SuppressLint("CommitPrefEdits")
public class ChatActivity extends AppCompatActivity {
    ImageView options;
    ScrollView chatScroll;
    LinearLayout chatRoot;
    EditText chatInput;
    ImageView chatSend;

    Drawable ensiAvatar;
    Drawable userAvatar;

    String ensiUsername = "<font color=yellow>Ensi</font>";
    String userUsername = "Some frok";

    SharedPreferences config;

    ArrayList<String> savedWords;
    ArrayList<String> savedVerbs;

    Boolean[] saveChances = {false,false,true};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ensiAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ensi);
        userAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user);

        options = findViewById(R.id.main_options);
        chatScroll = findViewById(R.id.main_chatScroll);
        chatRoot = findViewById(R.id.main_chatRoot);
        chatInput = findViewById(R.id.main_chatInput);
        chatSend = findViewById(R.id.main_chatSend);

        config = getSharedPreferences("APP_CONFIG", MODE_PRIVATE);

        sendMessage(ensiAvatar, ensiUsername, "hi bro");

        getSavedWordsAndVerbs();
        setListeners();
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
        sendMessage(ensiAvatar, ensiUsername, EnsiUtil.buildMessage(username, content, savedWords, savedVerbs));
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
        startActivity(intent);
    }

    void setListeners() {
        options.setOnClickListener(v -> switchActivity(DlcActivity.class));

        chatSend.setOnClickListener(v -> {
            sendMessage(userAvatar, userUsername, chatInput.getText().toString());
            chatInput.setText("");
        });
    }
}