package com.coderscampus.assignment;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.ArrayList;

public class Assignment8 implements Runnable {
    private List<Integer> numbers;
    private AtomicInteger currentIndex;
    private Map<Integer, Integer> frequencyMap;

    public Assignment8(List<Integer> numbers, AtomicInteger currentIndex, Map<Integer, Integer> frequencyMap) {
        this.numbers = numbers;
        this.currentIndex = currentIndex;
        this.frequencyMap = frequencyMap;
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
        synchronized (currentIndex) {
            start = currentIndex.get();
            end = currentIndex.addAndGet(1000);
            if (end > numbers.size()) {
                end = numbers.size();
            }
        //   System.out.println("Starting to fetch records " + start + " to " + (end));
        }  
        // Force thread to pause for half a second to simulate actual Http / API traffic delay
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        List<Integer> newList = new ArrayList<>();
        IntStream.range(start, end).forEach(n -> newList.add(numbers.get(n)));
        return newList;
    }

    @Override
    public void run() {
        while (true) {
            List<Integer> chunk = getNumbers();
            int index = currentIndex.getAndIncrement();
            if (chunk.isEmpty()) {
                break;
            }
            if (index >= numbers.size()) {
                break;
            }
            int number = numbers.get(index);
                frequencyMap.merge(number, 1, Integer::sum);
            
        }
    }
}

