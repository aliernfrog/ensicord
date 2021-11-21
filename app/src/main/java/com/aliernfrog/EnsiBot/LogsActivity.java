package com.aliernfrog.EnsiBot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aliernfrog.EnsiBot.utils.AppUtil;
import com.aliernfrog.EnsiBot.utils.FileUtil;

import java.io.File;

public class LogsActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;
    ImageView goBack;
    LinearLayout root;
    Button clearLogs;

    String logPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        logPath = getExternalFilesDir(".saved").getPath()+"/log.txt";

        nestedScrollView = findViewById(R.id.logs_nestedScrollView);
        goBack = findViewById(R.id.logs_goBack);
        root = findViewById(R.id.logs_root);
        clearLogs = findViewById(R.id.logs_clear);

        loadLogs();
        setListeners();
    }

    void loadLogs() {
        try {
            String content = FileUtil.readFile(logPath);
            String[] lines = content.split("\n");
            for (String line : lines) {
                if (line.contains(" ")) {
                    String[] array = line.split(" \\|\\| ");
                    String methodName = array[0].replace(getPackageName(), "");
                    String text = array[1];
                    addLogView(methodName, text);
                }
            }
            scrollToBottom();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    void addLogView(String methodName, String text) {
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.inflate_log, root, false);
        LinearLayout linear = viewGroup.findViewById(R.id.log_linear);
        TextView methodView = viewGroup.findViewById(R.id.log_method);
        TextView textView = viewGroup.findViewById(R.id.log_log);
        methodView.setText(methodName);
        textView.setText(text);
        if (methodName.startsWith("[ERR]")) linear.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.linear_red));
        root.addView(viewGroup);
    }

    void deleteLogs() {
        try {
            File logFile = new File(logPath);
            String parentPath = logFile.getParent();
            String fileName = logFile.getName();
            FileUtil.saveFile(parentPath, fileName, "");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
        root.removeAllViews();
        loadLogs();
    }

    void scrollToBottom() {
        Handler handler = new Handler();
        handler.postDelayed(() -> nestedScrollView.fullScroll(View.FOCUS_DOWN), 100);
    }

    void setListeners() {
        AppUtil.handleOnPressEvent(goBack, this::finish);
        AppUtil.handleOnPressEvent(clearLogs, this::deleteLogs);
    }
}