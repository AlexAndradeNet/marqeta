/* BlankFactor (C)2023 */
package com.blankfactor.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerUtil {

    private LoggerUtil() {
        // Private constructor to prevent instantiation
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz);
    }

    public static void info(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).info(message, args);
    }

    public static void error(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).error(message, args);
    }
}
