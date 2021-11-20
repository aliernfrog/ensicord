package com.aliernfrog.EnsiBot.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class EnsiUtil {
    public static String buildMention(String username) {
        return "<b><font color=#0098FF>@"+username+"</font></b>";
    }

    public static String buildMessage(String username, String content, List<String> words, List<String> verbs, List<String> concs, List<String> types) {
        content = content.toLowerCase();
        List<String> args = Arrays.asList(content.split(" "));

        String response = buildStory(words, verbs, concs, types);

        if (args.contains("hi") || args.contains("hello")) response = "wow hi bro";
        if (args.contains("gn")) response = buildMention(username)+", gn my,";

        return response;
    }

    public static String buildStory(List<String> words, List<String> verbs, List<String> concs, List<String> types) {
        Random random = new Random();
        String type = types.get(random.nextInt(types.size()));
        String[] typeArr = type.split("");
        StringBuilder full = new StringBuilder();
        for (String cur : typeArr) {
            switch (cur) {
                case "W":
                    full.append(randomize(words));
                    break;
                case "V":
                    full.append(randomize(verbs));
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
