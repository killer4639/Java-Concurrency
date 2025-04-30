package org.concurrency.zeroGCLogger;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class BufferPool {
    private final BlockingQueue<ByteBuffer> pool;
    private final int bufferSize;

    public BufferPool(int poolSize, int bufferSize) {
        this.pool = new ArrayBlockingQueue<>(poolSize);
        this.bufferSize = bufferSize;
        for (int i = 0; i < poolSize; i++) {
            pool.offer(createBuffer());
        }
    }

    private ByteBuffer createBuffer() {
        return ByteBuffer.allocateDirect(bufferSize);
    }

    public ByteBuffer acquireBuffer() {
        ByteBuffer buffer = null;
        try {
            buffer = pool.take();
        } catch (InterruptedException exception) {
            System.out.println("Interrupted waiting for buffer");
        }
        return buffer;
    }

    public void releaseBuffer(ByteBuffer buffer) {
        if (buffer != null) {
            buffer.clear(); // Reset buffer state for reuse
            pool.offer(buffer); // Return to pool
        }
    }
}
