package com.aliernfrog.EnsiBot.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtil {
    public static Bitmap getBipmapFromURL(String urlString) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoInput(true);
        connection.connect();
        InputStream input = connection.getInputStream();
        return BitmapFactory.decodeStream(input);
    }

    public static String doPostRequest(String Url, JSONObject obj) throws Exception {
        URL url = new URL(Url);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);
        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = obj.toString().getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        String res = getResFromConnection(connection);
        connection.disconnect();
        return res;
    }

    public static String getResFromConnection(HttpURLConnection connection) throws Exception {
        StringBuilder res = new StringBuilder();
        String resLine;
        try(BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            while ((resLine = br.readLine()) != null) {
                if (res.length() > 0) resLine = "<br />"+resLine;
                res.append(resLine.trim());
            }
        }
        return res.toString();
    }
}
