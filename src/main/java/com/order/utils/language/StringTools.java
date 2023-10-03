package com.order.utils.language;

public final class StringTools {

    public static String notNull(String value) {
        return notNull(value, "");
    }

    public static String notNull(String value, String defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static String escapeForRegex(String input) {
        return input.replace("\\", "\\\\")
                .replace(".", "\\.")
                .replace("*", "\\*")
                .replace("+", "\\+")
                .replace("?", "\\?")
                .replace("{", "\\{")
                .replace("}", "\\}")
                .replace("(", "\\(")
                .replace(")", "\\)")
                .replace("[", "\\[")
                .replace("]", "\\]")
                .replace("^", "\\^")
                .replace("$", "\\$")
                .replace("|", "\\|");
    }
}
