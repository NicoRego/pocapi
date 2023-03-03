package com.nicorego.nhs.pocapi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class LoggerWrapper {
    private static final Logger logger = LoggerFactory.getLogger(LoggerWrapper.class);

    public static void initialize() {
        // Initialize the logging framework
    }

    public static void setLoggingLevel(String level) {
        // Set the logging level
    }

    public static void setLoggingDestination(String destination) {
        // Set the logging destination
    }

    public static void logInfo(String message) {
        logger.info(message);
    }

    public static void logWarning(String message) {
        logger.warn(message);
    }

    public static void logError(Level level,String message, Throwable t) {
        logger.error(message, t);
    }
}

