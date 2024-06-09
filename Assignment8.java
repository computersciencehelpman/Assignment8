package com.coderscampus.assignment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Assignment8 implements Runnable {
    private List<Integer> numbers;
    private AtomicInteger index;
    private Map<Integer, Integer> frequencyMap;

    public Assignment8(List<Integer> numbers, AtomicInteger index, Map<Integer, Integer> frequencyMap) {
        this.numbers = numbers;
        this.index = index;
        this.frequencyMap = frequencyMap;
    }

    public List<Integer> getNumbers() {
        int start, end;
        synchronized (index) {
            start = index.get();
            end = index.addAndGet(1000);
            System.out.println("Starting to fetch records " + start + " to " + end);
        }

        // Avoid artificial delay if possible
        // try {
        //     Thread.sleep(500);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }

        List<Integer> newList = numbers.subList(start, Math.min(end, numbers.size()));
        System.out.println("Done Fetching records " + start + " to " + end);
        return newList;
    }

    @Override
    public void run() {
        List<Integer> asyncNumbers = getNumbers();
        asyncNumbers.forEach(i -> frequencyMap.merge(i, 1, Integer::sum));
    }
}

