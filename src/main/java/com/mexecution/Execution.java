package com.mexecution;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Execution {
    private static final Execution INSTANCE = new Execution();

    private boolean printResult = false;

    private TimeFormat timeFormat = TimeFormat.MILLISECONDS;

    private LogLevel logLevel = LogLevel.MIN;

    private Execution() {

    }

    public static Execution create() {
        return INSTANCE;
    }

    @SafeVarargs
    public final <T> void of(Callable<T>... callables) {
        for (Callable<T> callable : callables) {
            single(callable);
        }
    }

    public final <T> void single(Callable<T> callable) {
        try {
            long startTime = System.currentTimeMillis();
            T call = callable.call();
            long endTime = System.currentTimeMillis();

            String log = createLog(startTime, endTime, logLevel, call);
            System.out.println(log);
        } catch (Exception e) {
            String errorLog = createErrorLog(e.getMessage());
            System.out.println(errorLog);
        }
    }

    public Execution printResult() {
        printResult = true;
        return this;
    }

    public Execution timeFormat(TimeFormat timeFormat) {
        this.timeFormat = timeFormat;
        return this;
    }

    public Execution logLevel(LogLevel logLevel) {
        this.logLevel = logLevel;
        return this;
    }

    private String createErrorLog(String errorMessage) {
        return String.format("[Error message: %s]", errorMessage);
    }

    private <T> String createLog(long startTime, long endTime, LogLevel logLevel, T result) {
        StringBuilder formattedStringBuilder = new StringBuilder();

        if (logLevel == LogLevel.MIN) {

            formattedStringBuilder
                    .append("[Execution time: ").append(formatTime(endTime - startTime)).append("]");
        } else if (logLevel == LogLevel.MAX) {

            formattedStringBuilder
                    .append("[Execution time: ").append(formatTime(endTime - startTime)).append("]")
                    .append(" ")
                    .append("[Start time: ").append(startTime).append("]")
                    .append(" ")
                    .append("[End time: ").append(endTime).append("]");
        } else {
            throw new UnsupportedOperationException("Unsupported log level");
        }

        if (printResult) {
            formattedStringBuilder
                    .append(" ")
                    .append("[Result: " ).append(result.toString()).append("]");
        }

        return formattedStringBuilder.toString();
    }

    private String formatTime(long milliseconds) {
        long timeValue = milliseconds;
        String timeUnit = "ms";

        switch (this.timeFormat) {
            case SECONDS -> {
                timeValue = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
                timeUnit = "s";
            }
            case NANOS -> {
                timeValue = TimeUnit.MILLISECONDS.toNanos(milliseconds) % 60;
                timeUnit = "ns";
            }
        }

        return String.format("%d%s", timeValue, timeUnit);
    }
}