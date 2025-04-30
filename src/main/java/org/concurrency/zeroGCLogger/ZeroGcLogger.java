package org.concurrency.zeroGCLogger;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;


public class ZeroGcLogger {
    private final BufferPool bufferPool;
    private final AsyncAppender asyncAppender;
    private final ExecutorService writerExecutor;
    private final BlockingQueue<ByteBuffer> bufferQueue;

    public ZeroGcLogger(String filePath, int poolSize, int bufferSize, int queueCapacity) throws Exception {
        this.bufferPool = new BufferPool(poolSize, bufferSize);
        this.bufferQueue = new ArrayBlockingQueue<>(queueCapacity);
        this.asyncAppender = new AsyncAppender(filePath, bufferQueue, bufferPool);
        this.writerExecutor = Executors.newSingleThreadExecutor(r -> new Thread(r, "AsyncLogWriter"));
        this.writerExecutor.submit(asyncAppender);
    }

    private ByteBuffer beginEntry() {
        ByteBuffer buffer = bufferPool.acquireBuffer();
        long timestamp = System.currentTimeMillis();
        buffer.putLong(timestamp);
        return buffer;
    }

    private void endEntry(ByteBuffer buffer) {
        if (buffer != null) {
            asyncAppender.append(buffer);
        }
    }

    public void log(int value) {
        ByteBuffer buffer = beginEntry();
        if (buffer.remaining() >= Integer.BYTES) {
            // If the space is not available, then study the strategies to tackle it
        }

        buffer.putInt(value);

        endEntry(buffer);
    }

    public void log(long value) {
        ByteBuffer buffer = beginEntry();
        if (buffer.remaining() >= Long.BYTES) {
            // If the space is not available, then study the strategies to tackle it
        }
        buffer.putLong(value);
        endEntry(buffer);
    }

    public void log(CharSequence message) {
        ByteBuffer buffer = beginEntry();

        byte[] messageBytes = message.toString().getBytes(StandardCharsets.UTF_8);
        buffer.putInt(messageBytes.length);
        buffer.put(messageBytes);
        endEntry(buffer);
    }

    // --- Shutdown ---
    public void shutdown() throws InterruptedException {
        asyncAppender.stop();
        writerExecutor.shutdown();
        if (!writerExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.err.println("Log writer did not terminate gracefully.");
            writerExecutor.shutdownNow();
        }
    }
}