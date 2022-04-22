package com.aliernfrog.EnsiBot.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONObject;

public class AppUtil {
    public static void mentionUser(EditText input, String username) {
        String current = input.getText().toString();
        String mention = EnsiUtil.buildMention(username);
        String finalText = current+mention;
        input.setText(finalText);
        input.setSelection(input.length());
    }

    public static JSONObject buildMessageData(String author, String content, String userUsername, String ensiUsername) {
        JSONObject object = new JSONObject();
        try {
            object.put("avatar", "other");
            if (author.equals(userUsername)) object.put("avatar", "user");
            if (author.equals(ensiUsername)) object.put("avatar", "ensi");
            object.put("author", author);
            object.put("content", content);
            return object;
        } catch (Exception e) {
            return object;
        }
    }

    public static void devLog(String text, Context context) {
        String logPath = context.getExternalFilesDir(".saved").getPath()+"/log.txt";
        StackTraceElement stackTrace = Thread.currentThread().getStackTrace()[4];
        String className = stackTrace.getClassName();
        String methodName = stackTrace.getMethodName();
        if (text.contains("Exception")) className = "[ERR] "+className;
        String full = className+"."+methodName+" || "+text;
        try {
            FileUtil.appendFile(logPath, full);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getVersName(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionName;
    }

    public static Integer getVersCode(Context context) throws Exception {
        PackageManager pm = context.getPackageManager();
        PackageInfo pInfo = pm.getPackageInfo(context.getPackageName(), 0);
        return pInfo.versionCode;
    }

    @SuppressLint("ApplySharedPref")
    public static Boolean getUpdates(Context context) throws Exception {
        SharedPreferences update = context.getSharedPreferences("APP_UPDATE", Context.MODE_PRIVATE);
        SharedPreferences config = context.getSharedPreferences("APP_CONFIG", Context.MODE_PRIVATE);
        SharedPreferences.Editor updateEdit = update.edit();
        String updateUrl = config.getString("updateUrl", "https://aliernfrog.github.io/ensicord/update.json");
        String rawUpdate = WebUtil.getContentFromURL(updateUrl);
        JSONObject object = new JSONObject(rawUpdate);
        updateEdit.putInt("updateLatest", object.getInt("latest"));
        updateEdit.putString("updateDownload", object.getString("download"));
        updateEdit.putString("updateChangelog", object.getString("changelog"));
        updateEdit.putString("updateChangelogVersion", object.getString("changelogVersion"));
        updateEdit.commit();
        return true;
    }

    public static String fixTextForHtml(String text) {
        text = text.replace("   ", "&nbsp;&nbsp;&nbsp;");
        text = text.replace("  ", "&nbsp;&nbsp;");
        text = text.replace("\n", "<br>");
        return text;
    }

    public static int dpToPx(int dp, float density) {
        return (int) (dp*density+0.5f);
    }

    public static JSONArray removeIndexFromJsonArray(JSONArray array, Integer index) {
        JSONArray newArray = new JSONArray();
        try {
            for (int i = 0; i < array.length(); i++) {
                if (i != index) newArray.put(array.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newArray;
    }

    public static void handleOnPressEvent(View view, MotionEvent event, @Nullable Runnable onClick) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ObjectAnimator scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.9f);
                ObjectAnimator scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.9f);
                scaleDownX.setDuration(100);
                scaleDownY.setDuration(100);
                AnimatorSet scaleDown = new AnimatorSet();
                scaleDown.play(scaleDownX).with(scaleDownY);
                scaleDown.start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (onClick != null && event.getAction() == MotionEvent.ACTION_UP) onClick.run();
                ObjectAnimator scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f);
                ObjectAnimator scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f);
                scaleUpX.setDuration(100);
                scaleUpY.setDuration(100);
                AnimatorSet scaleUp = new AnimatorSet();
                scaleUp.play(scaleUpX).with(scaleUpY);
                scaleUp.start();
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void handleOnPressEvent(View view, @Nullable Runnable onClick) {
        view.setOnTouchListener((v, event) -> {
            handleOnPressEvent(v, event, onClick);
            return true;
        });
    }

    public static void handleOnPressEvent(View view) {
        handleOnPressEvent(view, (Runnable) null);
    }
}
