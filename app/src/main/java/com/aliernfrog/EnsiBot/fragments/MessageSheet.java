package com.aliernfrog.EnsiBot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aliernfrog.EnsiBot.ChatActivity;
import com.aliernfrog.EnsiBot.R;
import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MessageSheet extends BottomSheetDialogFragment {
    TextView message_author;
    TextView message_content;
    Button addToStarboard;
    Button deleteMessage;

    ChatActivity context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sheet_message, container, false);

        message_author = view.findViewById(R.id.messageSheet_message_author);
        message_content = view.findViewById(R.id.messageSheet_message_content);
        addToStarboard = view.findViewById(R.id.messageSheet_starboard);
        deleteMessage = view.findViewById(R.id.messageSheet_delete);

        context = (ChatActivity) getActivity();

        loadMessage();
        setListeners();

        return view;
    }

    void deleteMessage() {
        context.deleteChosenMessage();
        dismiss();
    }

    void loadMessage() {
        View view = context.getChosenMessage();
        TextView authorView = view.findViewById(R.id.message_author_username);
        TextView contentView = view.findViewById(R.id.message_content);
        String author = authorView.getText().toString();
        String content = contentView.getText().toString();
        message_author.setText(author);
        message_content.setText(content);
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(deleteMessage, this::deleteMessage);
    }
}
