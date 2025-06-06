package main.java.com.cxresolution.utils.helper;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private final String prefix;
    private final AtomicInteger counter;

    public IdGenerator(String prefix) {
        this.prefix = prefix;
        this.counter = new AtomicInteger(1);
    }

    public String generateId() {
        return prefix + counter.getAndIncrement();
    }
}
