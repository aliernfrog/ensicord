package com.aliernfrog.EnsiBot;

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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.aliernfrog.EnsiBot.fragments.MessageSheet;
import com.aliernfrog.EnsiBot.fragments.OptionsSheet;
import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.EnsiUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

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
    SharedPreferences update;
    SharedPreferences.Editor dlcEdit;
    Boolean debugMode = false;

    String ensiAvatarPath;
    String avatarPath;

    Drawable ensiAvatar;
    Drawable userAvatar;
    Drawable systemAvatar;

    String ensiUsername;
    String userUsername;

    String messageColor;
    String messageAuthorColor;
    String messageContentColor;

    ArrayList<String> savedWords;
    ArrayList<String> savedVerbs;
    ArrayList<String> savedConcs;
    ArrayList<String> savedTypes;

    String chatHistoryPath;
    Boolean sendMessageAllowed;
    Boolean saveNewMessages;
    Boolean isStarboard;
    JSONArray chatHistory;

    Integer chosenMessage;
    Boolean ensiSaveWords = true;
    Boolean requiresProfileUpdate = false;

    Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        avatarPath = getExternalFilesDir(".saved").toString()+"/avatar.png";
        ensiAvatarPath = getExternalFilesDir(".saved").toString()+"/ensi.png";

        ensiAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ensi);
        userAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user);
        systemAvatar = ContextCompat.getDrawable(getApplicationContext(), R.drawable.system);

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
        update = getSharedPreferences("APP_UPDATE", MODE_PRIVATE);
        dlcEdit = dlc.edit();
        debugMode = config.getBoolean("debugMode", false);
        ensiSaveWords = dlc.getBoolean("ensiSaveWords", true);

        devLog("ChatActivity started");

        getDlcTheme();
        getSavedWordsAndVerbs();
        getChannel(null);
        getAvatarsAndUsernames();
        setListeners();
        getChatOptions();
        loadChatHistory();
        checkUpdates();
    }

    void sendMessage(@Nullable Drawable avatar, String username, String content, Boolean saveToHistory) {
        if (content.replaceAll(" ", "").equals("")) return;
        ViewGroup message = (ViewGroup) getLayoutInflater().inflate(R.layout.inflate_message, chatRoot, false);
        LinearLayout messageView = message.findViewById(R.id.message_linear);
        ImageView avatarView = message.findViewById(R.id.message_author_avatar);
        TextView usernameView = message.findViewById(R.id.message_author_username);
        TextView contentView = message.findViewById(R.id.message_content);
        avatarView.setImageDrawable(avatar);
        usernameView.setText(Html.fromHtml(username));
        contentView.setText(Html.fromHtml(AppUtil.fixTextForHtml(content)));
        messageView.setBackgroundColor(Color.parseColor(messageColor));
        usernameView.setTextColor(Color.parseColor(messageAuthorColor));
        contentView.setTextColor(Color.parseColor(messageContentColor));
        usernameView.setOnClickListener(view -> AppUtil.mentionUser(chatInput, usernameView.getText().toString()));
        message.setOnLongClickListener(view -> {
            openMessageSheet(message);
            return true;
        });
        chatRoot.addView(message);
        onMessage(username, content, saveToHistory);
    }

    void onMessage(String username, String content, Boolean saveToHistory) {
        scrollToBottom();
        if (requiresProfileUpdate) getAvatarsAndUsernames();
        if (saveToHistory) chatHistory.put(AppUtil.buildMessageData(username, content, userUsername, ensiUsername));
        if (!username.equals(ensiUsername) && saveToHistory && sendMessageAllowed) {
            sendMessage(ensiAvatar, ensiUsername, EnsiUtil.buildMessage(username, content, savedWords, savedVerbs, savedConcs, savedTypes), true);
            int randomInt = random.nextInt(10);
            if (randomInt == 3 && ensiSaveWords) {
                String[] args = content.split(" ");
                String word = args[random.nextInt(args.length)];
                saveWord(word);
            }
        }
    }

    public void deleteChosenMessage() {
        devLog("deleting message: "+chosenMessage);
        View message = getChosenMessage();
        chatRoot.removeView(message);
        chatHistory = AppUtil.removeIndexFromJsonArray(chatHistory, chosenMessage-1);
    }

    public void getChannel(@Nullable String name) {
        if (name == null) name = dlc.getString("channelName", "#general");
        devLog("getting channel name: "+name);
        String hint = getString(R.string.channelStart).replace("%NAME%", name);
        String inputHint = getString(R.string.sendMessage).replace("%NAME%", name);
        channelTitle.setText(name);
        channelHint.setText(hint);
        chatInput.setHint(inputHint);
    }

    public void getChatOptions() {
        try {
            chatHistoryPath = getIntent().getStringExtra("chatHistoryPath");
            sendMessageAllowed = getIntent().getBooleanExtra("sendMessageAllowed", true);
            saveNewMessages = getIntent().getBooleanExtra("saveNewMessages", true);
            isStarboard = getIntent().getBooleanExtra("isStarboard", false);
            if (chatHistoryPath != null) chatHistory = new JSONArray(FileUtil.readFile(chatHistoryPath));
            if (chatHistoryPath == null) chatHistory = new JSONArray("[]");
            devLog("chatHistoryPath: "+chatHistoryPath);
            devLog("sendMessageAllowed: "+sendMessageAllowed);
            devLog("saveNewMessages: "+saveNewMessages);
            devLog("isStarboard: "+isStarboard);
            applyChatOptions();
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    void applyChatOptions() {
        if (isStarboard) {
            avatar.setOnClickListener(view -> Toast.makeText(getApplicationContext(), R.string.starboard_optionsDisabled, Toast.LENGTH_SHORT).show());
            getChannel(getString(R.string.starboard));
        }
        if (!sendMessageAllowed) {
            chatInput.setHint(R.string.sendMessageDisabled);
            chatInput.setEnabled(false);
            chatSend.setVisibility(View.GONE);
        }
    }

    public void getAvatarsAndUsernames() {
        devLog("getting avatars and usernames");
        File avatarFile = new File(avatarPath);
        File ensiAvatarFile = new File(ensiAvatarPath);
        if (avatarFile.exists()) userAvatar = Drawable.createFromPath(avatarPath);
        if (ensiAvatarFile.exists()) ensiAvatar = Drawable.createFromPath(ensiAvatarPath);
        avatar.setImageDrawable(userAvatar);
        ensiUsername = dlc.getString("ensiName", "<font color=yellow>Ensi</font>");
        userUsername = config.getString("username", "Some frok");
        requiresProfileUpdate = false;
    }

    public void saveChatHistory() {
        if (!saveNewMessages) return;
        devLog("attempting to save chat history");
        try {
            File file = new File(chatHistoryPath);
            String parentPath = file.getParent();
            String fileName = file.getName();
            String history = chatHistory.toString();
            FileUtil.saveFile(parentPath, fileName, history);
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    public void loadChatHistory() {
        if (!saveNewMessages) return;
        devLog("attempting to load chat history");
        devLog("loading "+chatHistory.length()+" messages");
        for (int i = 0; i < chatHistory.length(); i++) {
            try {
                JSONObject message = chatHistory.getJSONObject(i);
                Drawable avatarDrawable = null;
                String avatar = message.getString("avatar");
                String author = message.getString("author");
                String content = message.getString("content");
                if (avatar.equals("user")) avatarDrawable = userAvatar;
                if (avatar.equals("ensi")) avatarDrawable = ensiAvatar;
                if (avatar.equals("other")) avatarDrawable = systemAvatar;
                sendMessage(avatarDrawable, author, content, false);
            } catch (Exception e) {
                e.printStackTrace();
                devLog(e.toString());
            }
        }
    }

    public void getDlcTheme() {
        devLog("attempting to load theme");
        String backgroundColor = dlc.getString("background", "#000000");
        String topBarColor = dlc.getString("topBar", "#FF18191D");
        String titleColor = dlc.getString("title", "#FFFFFF");
        String hintColor = dlc.getString("hint", "#8C8C8C");
        String chatBoxColor = dlc.getString("chatBox", "#FF18191D");
        String chatBoxHintColor = dlc.getString("chatBoxHint", "#636363");
        String chatBoxTextColor = dlc.getString("chatBoxText", "#FFFFFF");
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

    public void getSavedWordsAndVerbs() {
        devLog("attempting to get saved words");
        String words = dlc.getString("words", "");
        String verbs = dlc.getString("verbs", "");
        String _concs = dlc.getString("concs", "");
        String _types = dlc.getString("types", "");
        savedWords = new ArrayList<>(Arrays.asList(words.split("\n")));
        savedVerbs = new ArrayList<>(Arrays.asList(verbs.split("\n")));
        savedConcs = new ArrayList<>(Arrays.asList(_concs.split("\n")));
        savedTypes = new ArrayList<>(Arrays.asList(_types.split("\n")));
        if (_types.equals("")) applyDefaultDlc();
    }

    public void saveWord(String word) {
        devLog("attempting to save word: "+word);
        boolean isVerb = word.endsWith("ed") || word.endsWith("ing");
        devLog("isVerb: "+isVerb);
        if (!isVerb) {
            String fullStr = dlc.getString("words", "");
            String finalStr = fullStr+"\n"+word;
            dlcEdit.putString("words", finalStr);
        } else {
            String fullStr = dlc.getString("verbs", "");
            String finalStr = fullStr+"\n"+word;
            dlcEdit.putString("verbs", finalStr);
        }
        dlcEdit.commit();
        getSavedWordsAndVerbs();
    }

    public void checkUpdates() {
        if (isStarboard) return;
        devLog("checking updates");
        try {
            int currentVersion = AppUtil.getVersCode(getApplicationContext());
            int latestVersion = update.getInt("updateLatest", currentVersion);
            if (latestVersion > currentVersion) {
                String _changelog = update.getString("updateChangelog", "-");
                String _download = update.getString("updateDownload", "-");
                String _author = "New update!";
                String _content = _changelog+"<br>"+_download;
                sendMessage(systemAvatar, _author, _content, true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            devLog(e.toString());
        }
    }

    void openMessageSheet(View message) {
        chosenMessage = ((LinearLayout)message.getParent()).indexOfChild(message);
        MessageSheet messageSheet = new MessageSheet();
        messageSheet.show(getSupportFragmentManager(), "message_sheet");
    }

    void openOptionsSheet() {
        OptionsSheet optionsSheet = new OptionsSheet();
        optionsSheet.show(getSupportFragmentManager(), "options_sheet");
        requiresProfileUpdate = true;
        saveChatHistory();
    }

    public View getChosenMessage() {
        return chatRoot.getChildAt(chosenMessage);
    }

    public JSONObject getChosenMessageData() {
        try {
            return chatHistory.getJSONObject(chosenMessage-1);
        } catch (JSONException e) {
            e.printStackTrace();
            devLog(e.toString());
            return new JSONObject();
        }
    }

    public Boolean getIsStarboard() {
        return isStarboard;
    }

    void applyDefaultDlc() {
        Intent intent = new Intent(this, DlcApplyActivity.class);
        intent.putExtra("applyDefault", true);
        startActivity(intent);
    }

    void scrollToBottom() {
        Handler handler = new Handler();
        handler.postDelayed(() -> chatScroll.fullScroll(View.FOCUS_DOWN), 100);
    }

    void devLog(String text) {
        if (debugMode) AppUtil.devLog(text, getApplicationContext());
    }

    void setListeners() {
        avatar.setOnClickListener(v -> openOptionsSheet());

        AppUtil.handleOnPressEvent(chatSend, () -> {
            sendMessage(userAvatar, userUsername, chatInput.getText().toString(), true);
            chatInput.setText("");
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveChatHistory();
        finish();
    }
}
