package ru.dreamworkerln.spring.utils.common;

import org.slf4j.helpers.MessageFormatter;

/**
 * Extends org.springframework.util.StringUtils to apache.commons.lang3.StringUtils.isBlank
 */
public class StringUtils extends org.springframework.util.StringUtils {

    /**
     * Checks if a String is null or empty ("") or whitespace only.
     * <br>Like org.apache.commons.lang3.StringUtils.isBlank
     * <br>(against - Spring StringUtils.isEmpty doesn't trim whitespaces)
     */
    public static boolean isBlank(String s) {
        return !org.springframework.util.StringUtils.hasLength(org.springframework.util.StringUtils.trimWhitespace(s));
    }

    public static boolean notBlank(String s) {
        return org.springframework.util.StringUtils.hasLength(org.springframework.util.StringUtils.trimWhitespace(s));
    }

    /**
     * Format string replacing {} to actual params as slf4j log.debug() do
     * @param format string to format
     * @param params actual params replacement
     * @return
     */
    public static String formatMsg(String format, Object... params) {
        return MessageFormatter.arrayFormat(format, params).getMessage();
    }

    public static void throwIfBlank(String s, String message) {
        if (isBlank(s)) {
            throw new IllegalArgumentException(message);
        }
    }


}
