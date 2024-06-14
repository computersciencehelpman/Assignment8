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

        List<Integer> numbers;
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
        AtomicInteger currentIndex = new AtomicInteger(0);

        // Using a thread pool with a fixed number of threads
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThreads);

        for (int j = 0; j < numberOfThreads; j++) {
            Assignment8 assignment8 = new Assignment8(numbers, currentIndex, frequencyMap);
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
        //Ensure the frequencyMap is not null before attempting to print
        if (frequencyMap != null && !frequencyMap.isEmpty()) {
         // Print the frequencies after all threads are done
        printFrequencies(frequencyMap);	
        } else {
        	System.out.println("No frequencies to display.");
        }
       
    }

    public static void printFrequencies(Map<Integer, Integer> frequencyMap) {
        frequencyMap.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println("Element " + entry.getKey() + " occurs: " + entry.getValue() + " times"));
    }
}
