package com.mexecution;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Execution {
    private static final Execution INSTANCE = new Execution();

    private boolean logReturn = false;

    private boolean logTime = true;

    private boolean logTotalTime = false;

    private TimeFormat timeFormat = TimeFormat.MILLISECONDS;

    private LogLevel logLevel = LogLevel.MIN;

    private Execution() {

    }

    public static Execution create() {
        return INSTANCE;
    }

    @SafeVarargs
    public final <T> void of(Callable<T>... callables) {
        long totalTime = 0;
        for (Callable<T> callable : callables) {
            long executionTime = single(callable);
            if (logTotalTime) {
                totalTime += executionTime;
            }
        }
        if (logTotalTime) {
            String log = logTotalTime(totalTime);
            System.out.println(log);
        }
    }

    public final void of(List<Callable<?>> callables) {
        long totalTime = 0;
        for (Callable<?> callable : callables) {
            long executionTime = single(callable);
            if (logTotalTime) {
                totalTime += executionTime;
            }
        }
        if (logTotalTime) {
            String log = logTotalTime(totalTime);
            System.out.println(log);
        }
    }

    public final <T> void of(Collection<Callable<T>> callables) {
        long totalTime = 0;
        for (Callable<T> callable : callables) {
            long executionTime = single(callable);
            if (logTotalTime) {
                totalTime += executionTime;
            }
        }
        if (logTotalTime) {
            String log = logTotalTime(totalTime);
            System.out.println(log);
        }
    }

    public <T> long single(Callable<T> callable) {
        try {
            long startTime = System.currentTimeMillis();
            T call = callable.call();
            long endTime = System.currentTimeMillis();

            if (logTime) {
                String log = log(startTime, endTime, logLevel, call);
                System.out.println(log);
            }

            return endTime - startTime;
        } catch (Exception e) {
            String errorLog = logError(e.getMessage());
            System.out.println(errorLog);
        }
        return 0;
    }

    public Execution logReturn(boolean logReturn) {
        this.logReturn = logReturn;
        return this;
    }

    public Execution logTotalTime(boolean logTotalTime) {
        this.logTotalTime = logTotalTime;
        return this;
    }

    public Execution logTime(boolean logTime) {
        this.logTime = logTime;
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

    private String logTotalTime(long totalTime) {
        return String.format("[Total execution time: %s]", formatTime(totalTime));
    }

    private String logError(String errorMessage) {
        return String.format("[Error message: %s]", errorMessage);
    }

    private <T> String log(long startTime, long endTime, LogLevel logLevel, T result) {
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

        if (logReturn) {
            formattedStringBuilder
                    .append(" ")
                    .append("[Return: ").append(result.toString()).append("]");
        }

        return formattedStringBuilder.toString();
    }

    private String formatTime(long milliseconds) {
        StringBuilder formattedStringBuilder = new StringBuilder();

        switch (this.timeFormat) {
            case SECONDS -> formattedStringBuilder.append(TimeUnit.MILLISECONDS.toSeconds(milliseconds)).append("s");
            case NANOSECONDS -> formattedStringBuilder.append(TimeUnit.MILLISECONDS.toNanos(milliseconds)).append("ns");
            default -> formattedStringBuilder.append(milliseconds).append("ms");
        }

        return formattedStringBuilder.toString();
    }
}