package com.coderscampus.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        System.out.println("Running");

        List<Integer> numbers = null;
        try {
            numbers = Files.readAllLines(Paths.get("output.txt"))
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Ensure numbers are initialized
        if (numbers == null || numbers.isEmpty()) {
            System.out.println("No numbers to process");
            return;
        }

        Map<Integer, Integer> frequencyMap = new ConcurrentHashMap<>();
        AtomicInteger index = new AtomicInteger(0);

        // Using a thread pool with a fixed number of threads
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (int i = 0; i <= 1000; i++) {
            Assignment8 assignment8 = new Assignment8(numbers, index, frequencyMap);
            executor.execute(assignment8);
        }

        executor.shutdown();
        try {
            if (!executor.awaitTermination(10, java.util.concurrent.TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }

        System.out.println("Done");

        // Print the frequencies after all threads are done
        printFrequencies(frequencyMap);
    }

    public static void printFrequencies(Map<Integer, Integer> frequencyMap) {
        frequencyMap.forEach((key, value) -> System.out.println("Element " + key + " occurs: " + value + " times"));
    }
}

