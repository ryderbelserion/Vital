package com.ryderbelserion.vital.utils;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class Methods {

    public static String formatInteger(final int number) {
        return NumberFormat.getIntegerInstance(Locale.US).format(number);
    }

    public static String formatDouble(final double number) {
        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static String convertList(final List<String> list) {
        if (list.isEmpty()) return "";

        StringBuilder message = new StringBuilder();

        for (String line : list) {
            message.append(line).append("\n");
        }

        return chomp(message.toString());
    }

    public static String chomp(final String key) {
        if (key.isEmpty()) {
            return key;
        }

        char CR = '\r';
        char LF = '\n';

        if (key.length() == 1) {
            char ch = key.charAt(0);

            if (ch == CR || ch == LF) {
                return "";
            }

            return key;
        }

        int lastIdx = key.length() - 1;
        char last = key.charAt(lastIdx);

        if (last == LF) {
            if (key.charAt(lastIdx - 1) == CR) {
                lastIdx--;
            }
        } else if (last != CR) {
            lastIdx++;
        }

        return key.substring(0, lastIdx);
    }
}