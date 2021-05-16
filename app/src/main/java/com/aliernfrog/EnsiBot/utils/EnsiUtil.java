package com.aliernfrog.EnsiBot.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnsiUtil {
    static String[] responses = {"yes bro","no bro","wow bro","frog","tank","story"};
    static String[] concs = {"and","but","then","becans"};
    static Boolean[] chances = {false,false,false,false,true};

    public static String buildMention(String username) {
        return "<b><font color=#0098FF>@"+username+"</font></b>";
    }

    public static String buildMessage(String username, String content, List<String> savedWords, List<String> savedVerbs) {
        content = content.toLowerCase();
        List<String> args = Arrays.asList(content.split(" "));

        String response;

        Random random = new Random();
        response = responses[random.nextInt(responses.length)];

        if (args.contains("hi") || args.contains("hello")) response = "wow hi bro";
        if (args.contains("gn")) response = buildMention(username)+", gn my,";

        if (response.equals("story")) response = buildStory(savedWords, savedVerbs);

        return response;
    }

    public static String buildStory(List<String> savedWords, List<String> savedVerbs) {
        Random random = new Random();
        Boolean twice = chances[random.nextInt(chances.length)];
        String finalStr = buildStorySentence(savedWords, savedVerbs);
        if (twice) {
            String str = buildStorySentence(savedWords, savedVerbs);
            String conc = concs[random.nextInt(concs.length)];
            finalStr = finalStr+" "+conc+" "+str;
        }
        return finalStr;
    }

    static String buildStorySentence(List<String> savedWords, List<String> savedVerbs) {
        Random random = new Random();
        String w1 = savedWords.get(random.nextInt(savedWords.size()));
        String w2 = savedVerbs.get(random.nextInt(savedVerbs.size()));
        String w3 = savedWords.get(random.nextInt(savedWords.size()));
        return w1+" "+w2+" "+w3;
    }

    public static String listJoin(String chars, List<String> list) {
        String finalStr = "";
        for (int i = 0; i < list.size(); i++) {
            String current = list.get(i);
            if (finalStr.equals("")) {
                finalStr = finalStr+current;
            } else {
                finalStr = finalStr+chars+current;
            }
        }
        return finalStr;
    }
}
