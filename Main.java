package com.coderscampus.assignment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Running");

        Map<Integer, Integer> frequencyMap = new ConcurrentHashMap<>();

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i <= 1000; i++) {
            Assignment8 assignment8 = new Assignment8(frequencyMap);
            Thread thread = new Thread(assignment8);
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to finish
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

     

        // Print the frequencies after all threads are done
        printFrequencies(frequencyMap);
    }

    public static void printFrequencies(Map<Integer, Integer> frequencyMap) {
        frequencyMap.forEach((key, value) -> {
            System.out.println(key + " = " + value);
        });
    }
}
