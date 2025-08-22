package com.hcl.snowflake;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EightDigitIdGenerator {

    private static final Set<Long> existingIds = new HashSet<>();
    private static final Random random = new Random();

    public static synchronized Long nextId() {
        Long id;
        do {
            id = 10000000L + (long) (random.nextDouble() * 90000000L); 
        } while (existingIds.contains(id));
        existingIds.add(id);
        return id;
    }
}
