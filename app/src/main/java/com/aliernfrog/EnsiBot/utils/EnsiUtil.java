package com.aliernfrog.EnsiBot.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnsiUtil {
    static String[] defaultConcs = {"and","then","or","becans","like"};
    static String[] defaultTypes = {"W V W","W V","V W","W","V","W V W C W V W","V W V V W","V W V V W C V W V V W","W + W = W"};

    public static String buildMention(String username) {
        return "<b><font color=#0098FF>@"+username+"</font></b>";
    }

    public static String buildMessage(String username, String content, List<String> savedWords, List<String> savedVerbs, List<String> concs, List<String> types) {
        content = content.toLowerCase();
        List<String> args = Arrays.asList(content.split(" "));

        String response = buildStory(savedWords, savedVerbs, concs, types);

        if (args.contains("hi") || args.contains("hello")) response = "wow hi bro";
        if (args.contains("gn")) response = buildMention(username)+", gn my,";

        return response;
    }

    public static String buildStory(List<String> savedWords, List<String> savedVerbs, List<String> concs, List<String> types) {
        List<String> defaultC = new ArrayList<>(Arrays.asList(defaultConcs));
        List<String> defaultT = new ArrayList<>(Arrays.asList(defaultTypes));
        if (concs == null || concs.size() < 1) concs = defaultC;
        if (types == null || types.size() < 1) types = defaultT;
        Random random = new Random();
        String type = types.get(random.nextInt(types.size()));
        String[] typeArr = type.split("");
        StringBuilder full = new StringBuilder();
        for (String cur : typeArr) {
            switch (cur) {
                case "W":
                    full.append(randomize(savedWords));
                    break;
                case "V":
                    full.append(randomize(savedVerbs));
                    break;
                case "C":
                    full.append(randomize(concs));
                    break;
                default:
                    full.append(cur);
            }
        }
        return full.toString();
    }

    static String randomize(List<String> list) {
        Random random = new Random();
        return list.get(random.nextInt(list.size()));
    }
}
