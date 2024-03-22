package com.test.demo;

import java.text.Collator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PinyinSort {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("ec", "ab", "ac", "ba"); // 需要排序的单词列表

        Collator collator = Collator.getInstance(); // 创建Collator对象
        Collections.sort(words, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return collator.compare(getPinyin(s1), getPinyin(s2)); // 比较两个字符串的拼音大小写不敏感
            }

            private String getPinyin(String word) {
                StringBuilder pinyinBuilder = new StringBuilder();
                for (char c : word.toCharArray()) {
                    if (Character.isLetterOrDigit(c)) {
                        pinyinBuilder.append(c);
                    } else {
                        break;
                    }
                }

                return pinyinBuilder.toString().toLowerCase(); // 转换为小写并返回拼音部分
            }
        });

        System.out.println(words); // 输出结果
    }
}
