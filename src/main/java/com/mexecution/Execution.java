package com.mexecution;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static com.mexecution.ExecutionUtils.nullCheck;

public class Execution {
    private static final Execution INSTANCE = new Execution();

    private boolean logReturn = false;

    private boolean logTime = true;

    private boolean logTotalTime = false;

    private TimeFormat timeFormat = TimeFormat.MILLISECONDS;

    private LogLevel logLevel = LogLevel.MIN;

    private long totalTime = 0;

    private Execution() {

    }

    public static Execution create() {
        return INSTANCE;
    }

    public final void ofVoid(Runnable... runnables) {
        resetTotalTime();
        nullCheck(runnables);

        voidIterate(runnables);

        logTotalTime();
    }

    public final void ofVoid(List<Runnable> runnables) {
        nullCheck(runnables);
        ofVoid(runnables.toArray(Runnable[]::new));
    }

    public final void of(Callable<?>... callables) {
        resetTotalTime();
        nullCheck(callables);

        iterate(callables);

        logTotalTime();
    }

    public final void of(Collection<Callable<?>> callables) {
        nullCheck(callables);
        of(callables.toArray(Callable[]::new));
    }

    private void iterate(Callable<?>[] callables) {
        for (Callable<?> callable : callables) {
            single(callable);
        }
    }

    private void voidIterate(Runnable[] runnables) {
        for (Runnable runnable : runnables) {
            voidSingle(runnable);
        }
    }

    private void voidSingle(Runnable runnable) {
        if (runnable == null) {
            logError("Null runnable");
            return;
        }
        try {
            long startTime = System.currentTimeMillis();
            runnable.run();
            long endTime = System.currentTimeMillis();

            if (logTime) {
                String log = log(startTime, endTime, logLevel);
                System.out.println(log);
            }

            if (logTotalTime) {
                totalTime += endTime - startTime;
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
    }

    private <T> void single(Callable<T> callable) {
        if (callable == null) {
            logError("Null callable");
            return;
        }
        try {
            long startTime = System.currentTimeMillis();
            T call = callable.call();
            long endTime = System.currentTimeMillis();

            if (logTime) {
                String log = log(startTime, endTime, logLevel, call);
                System.out.println(log);
            }

            if (logTotalTime) {
                totalTime += endTime - startTime;
            }
        } catch (Exception e) {
            logError(e.getMessage());
        }
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

    private void logTotalTime() {
        if (logTotalTime) {
            System.out.printf("[Total execution time: %s]%n", formatTime(totalTime));
        }
    }

    private void logError(String errorMessage) {
        System.out.printf("[Error message: %s]%n", errorMessage);
    }

    @SafeVarargs
    private <T> String log(long startTime, long endTime, LogLevel logLevel, T... result) {
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
            String resultMessage = result == null || result.length == 0 ? "" : result[0].toString();
            formattedStringBuilder
                    .append(" ")
                    .append("[Return: ").append(resultMessage).append("]");
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

    private void resetTotalTime() {
        this.totalTime = 0;
    }
}