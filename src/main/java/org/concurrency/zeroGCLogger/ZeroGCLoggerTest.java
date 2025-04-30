package org.concurrency.zeroGCLogger;

import org.concurrency.AutomatedTest;

public class ZeroGCLoggerTest implements AutomatedTest {
    @Override
    public void run() {
        try {
            ZeroGcLogger logger = new ZeroGcLogger("app.log", 16, 1024 * 4, 1024); // Pool=16, Buffer=4KB, Queue=1024

            long start = System.nanoTime();
            int iterations = 1_000_000;
            for (int i = 0; i < iterations; i++) {
                logger.log("Logging message number: " + i); // Note: String concat here creates garbage!
                logger.log(i);
                logger.log(System.nanoTime());
            }
            long end = System.nanoTime();
            System.out.printf("Logging %d iterations took %.2f ms%n", iterations, (end - start) / 1_000_000.0);

            logger.shutdown();
        } catch (Exception exception) {
            System.out.println("Error while running logger");
        }
    }
}
