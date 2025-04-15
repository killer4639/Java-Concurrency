package org.concurrency;

import org.concurrency.unisexBathroomProblem.UnisexBathroomTest;

public class Main {
    public static void main(String[] args) {
        AutomatedTest test = new UnisexBathroomTest();
        test.run();
    }
}