package com.coderscampus.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Assignment8 implements Runnable {
    private List<Integer> numbers = null;
    private AtomicInteger i = new AtomicInteger(0);
    private Map<Integer, Integer> frequencyMap;

    public Assignment8(Map<Integer, Integer> frequencyMap) {
        this.frequencyMap = frequencyMap;
        try {
            // Make sure you download the output.txt file for Assignment 8
            // and place the file in the root of your Java project
            numbers = Files.readAllLines(Paths.get("output.txt"))
                    .stream()
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Integer> getNumbers() {
        int start, end;
        synchronized (i) {
            start = i.get();
            end = i.addAndGet(1000);
            System.out.println("Starting to fetch records " + start + " to " + (end));
        }

        // Simulate processing delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        List<Integer> newList = numbers.subList(start, Math.min(end, numbers.size()));
        System.out.println("Done Fetching records " + start + " to " + (end));
        return newList;
    }

    @Override
    public void run() {
        List<Integer> asyncNumbers = getNumbers();
        asyncNumbers.forEach(i -> {
            frequencyMap.merge(i, 1, Integer::sum);
        });
    }
}
