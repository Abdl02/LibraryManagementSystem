package com.library.management.util;

import lombok.Getter;

import java.util.Date;


@Getter
public class LogEvent {
    private final Date timestamp;
    private final String actionType;
    private final String message;

    public LogEvent(String actionType, String message) {
        this.timestamp = new Date();
        this.actionType = actionType;
        this.message = message;
    }

    @Override
    public String toString() {
        return "[" + timestamp + "] " + actionType + ": " + message;
    }
}