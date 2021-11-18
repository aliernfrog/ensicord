package com.aliernfrog.EnsiBot.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public class AppUtil {
    public static JSONObject buildMessageData(String author, String content) {
        JSONObject object = new JSONObject();
        try {
            object.put("author", author);
            object.put("content", content);
            return object;
        } catch (Exception e) {
            return object;
        }
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
