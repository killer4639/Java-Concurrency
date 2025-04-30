package org.concurrency.zeroGCLogger;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

// Background task for writing buffers to disk
class AsyncAppender implements Runnable {
    private final BlockingQueue<ByteBuffer> bufferQueue;
    private final FileChannel fileChannel;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final BufferPool bufferPool; // Need pool access to release buffers

    public AsyncAppender(String filePath, BlockingQueue<ByteBuffer> bufferQueue, BufferPool bufferPool) throws Exception {
        this.bufferQueue = bufferQueue;
        this.bufferPool = bufferPool;
        this.fileChannel = FileChannel.open(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND, StandardOpenOption.WRITE);
    }

    public void append(ByteBuffer buffer) {
        // This method is called by the logging thread.
        // It needs to handle the case where the queue is full (backpressure).
        // offer() is non-blocking, tryPut() could block or timeout.
        boolean offered = bufferQueue.offer(buffer);
        if (!offered) {
            // Handle backpressure:
            // Option 1: Drop the log message
            // Option 2: Block the logging thread (bad for latency!)
            // Option 3: Log to stderr/console (still creates garbage)
            System.err.println("WARN: Log queue full. Message dropped.");
            // If we drop, we must release the buffer back to the pool
            bufferPool.releaseBuffer(buffer);
        }
    }

    @Override
    public void run() {
        while (running.get() || !bufferQueue.isEmpty()) {
            try {
                ByteBuffer buffer = bufferQueue.poll(100, TimeUnit.MILLISECONDS);

                if (buffer != null) {
                    try {
                        buffer.flip();
                        while (buffer.hasRemaining()) {
                            fileChannel.write(buffer);
                        }
                    } finally {
                        bufferPool.releaseBuffer(buffer);
                    }
                }

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("AsyncAppender interrupted.");
                break;
            } catch (Exception e) {
                // Handle write exceptions
                System.err.println("Error writing log buffer: " + e.getMessage());
            }
        }
        try {
            fileChannel.close();
        } catch (Exception e) {
            System.err.println("Error closing log file channel: " + e.getMessage());
        }
        System.out.println("AsyncAppender finished.");
    }

    public void stop() {
        running.set(false);
    }
}

