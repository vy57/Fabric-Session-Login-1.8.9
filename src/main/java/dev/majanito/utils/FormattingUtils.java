package dev.majanito.utils;

public final class FormattingUtils {
    private FormattingUtils() {
    }

    public static String surroundWithObfuscated(String text, int count) {
        StringBuilder padding = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            padding.append('@');
        }
        return "\u00a7k" + padding + " \u00a7r" + text + " \u00a7k " + padding + "\u00a7r";
    }
}
