package com.twins.common.util;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicReference;

public class UniqueLocalDateTime {

    private static final AtomicReference<LocalDateTime> lastTimestamp = new AtomicReference<>();


    public static LocalDateTime now() {
        LocalDateTime now = LocalDateTime.now();

        while (true) {
            LocalDateTime last = lastTimestamp.get();

            if (last == null || now.isAfter(last)) {
                if (lastTimestamp.compareAndSet(last, now)) {
                    return now;
                }
            } else {
                // Se o now não é maior, incrementa o último timestamp em 1 nanossegundo
                LocalDateTime incremented = last.plusNanos(1);
                if (lastTimestamp.compareAndSet(last, incremented)) {
                    return incremented;
                }
            }
            // Se compareAndSet falhar, tenta de novo no loop
        }
    }
}
