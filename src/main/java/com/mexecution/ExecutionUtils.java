package com.mexecution;

public class ExecutionUtils {
    public static void nullCheck(Object object) {
        if (object == null) {
            throw new UnsupportedOperationException("Param can't be null");
        }
    }
}
