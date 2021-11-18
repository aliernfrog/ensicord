package com.aliernfrog.EnsiBot.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aliernfrog.EnsiBot.R;

import org.json.JSONObject;

public class ChatUtil {
    public static View loadMessage(JSONObject data, ViewGroup root, LayoutInflater inflater) throws Exception {
        String type = data.getString("type");
        View finalView = null;
        if (type.equals("message")) finalView = loadTypeMessage(data, root, inflater);
        if (type.equals("suggestion")) finalView = loadTypeSuggestion(data, root, inflater);
        return finalView;
    }

    static View loadTypeMessage(JSONObject data, ViewGroup root, LayoutInflater inflater) throws Exception {
        ViewGroup finalView = (ViewGroup) inflater.inflate(R.layout.inflate_message, root, false);
        TextView authorView = finalView.findViewById(R.id.message_author_username);
        TextView contentView = finalView.findViewById(R.id.message_content);
        String author = data.getString("author");
        String content = data.getString("content");
        authorView.setText(author);
        contentView.setText(content);
        return finalView;
    }

    static View loadTypeSuggestion(JSONObject data, ViewGroup root, LayoutInflater inflater) throws Exception {
        ViewGroup finalView = (ViewGroup) inflater.inflate(R.layout.inflate_suggestion, root, false);
        ImageView iconView = finalView.findViewById(R.id.suggestion_icon);
        TextView titleView = finalView.findViewById(R.id.suggestion_title);
        String title = data.getString("title");
        titleView.setText(title);
        if (data.has("icon")) {
            Drawable iconDrawable = (Drawable) data.get("icon");
            Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();
            iconView.setImageBitmap(iconBitmap);
            iconView.setVisibility(View.VISIBLE);
        }
        return finalView;
    }
}
