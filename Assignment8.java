package com.coderscampus.assignment;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Assignment8 implements Runnable {
    private List<Integer> numbers = null;
    private AtomicInteger i = new AtomicInteger(0);
    private Map<Integer, Integer> frequencyMap;

    public Assignment8(List<Integer> numbers, AtomicInteger currentIndex, Map<Integer, Integer> frequencyMap) {
        this.numbers = numbers;
        this.i = currentIndex;
        this.frequencyMap = frequencyMap;

        try {
            // Make sure you download the output.txt file for Assignment 8
            // and place the file in the root of your Java project
            numbers = Files.readAllLines(Paths.get("output.txt"))
                    .stream()
                    .map(n -> Integer.parseInt(n))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method will return the numbers that you'll need to process from the list
     * of Integers. However, it can only return 1000 records at a time. You will
     * need to call this method multiple times to retrieve all numbers from the list.
     *
     * @return Integers from the parsed txt file, 1,000 numbers at a time
     */
    public List<Integer> getNumbers() {
        int start, end;
        synchronized (i) {
            start = i.get();
            end = i.addAndGet(1000);
            if (end > numbers.size()) {
                end = numbers.size();
            }
            System.out.println("Starting to fetch records " + start + " to " + (end));
        }
        // Force thread to pause for half a second to simulate actual Http / API traffic delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
        }

        List<Integer> newList = new ArrayList<>();
        IntStream.range(start, end).forEach(n -> newList.add(numbers.get(n)));
        System.out.println("Done Fetching records " + start + " to " + (end));
        return newList;
    }

    @Override
    public void run() {
        while (true) {
            List<Integer> chunk = getNumbers();
            if (chunk.isEmpty()) {
                break;
            }
            for (int number : chunk) {
                frequencyMap.merge(number, 1, Integer::sum);
            }
        }
    }
}
