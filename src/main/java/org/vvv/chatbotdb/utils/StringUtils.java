package org.vvv.chatbotdb.utils;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    private static final String punct = ".,?!={}\\/<>";
    private static final String splitignore = " ";
    
    
    public List<String> split(String string) {
        List<String> list = new ArrayList<String>();
        StringBuffer word = new StringBuffer();
        boolean wordStarted = false;
        for (int i=0; i<string.length(); i++) {
            char ch = string.charAt(i);
            if (punct.indexOf(ch)>-1) {
                if (wordStarted) {
                    wordStarted = false;
                    list.add(word.toString());
                    list.add(ch + "");
                    word = new StringBuffer();
                } else {
                    list.add(ch + "");
                }
                continue;
            }
            if (splitignore.indexOf(ch)>-1) {
                if(wordStarted) {
                    wordStarted = false;
                    list.add(word.toString());
                    word = new StringBuffer();
                }
                continue;
            }
            if (wordStarted) {
                word.append(ch);
            } else {
                wordStarted = true;
                word.append(ch);
            }
        }
        if (wordStarted) {
            list.add(word.toString());
        }
        return list;
    }
}
