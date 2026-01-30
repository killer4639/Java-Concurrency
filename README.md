# Java-Concurrency

A focused collection of Java concurrency experiments, examples, and notes aimed at building intuition for safe, performant multithreaded code.

## Goals

- Explore core concurrency primitives in Java  
- Demonstrate common patterns and pitfalls  
- Provide small, runnable examples for learning and reference  
- Track performance and GC behavior where relevant  

## What’s Inside

- Thread creation and lifecycle  
- Synchronization and visibility  
- Locks and atomics  
- Executors, futures, and thread pools  
- Coordination utilities (latches, barriers, semaphores)  
- Concurrent collections  
- Performance notes and micro-benchmarks  

## Requirements

- Java 17+ (or a modern LTS)  
- Gradle (wrapper or system Gradle)  

## Build

```bash
gradle build
```

## Run
```
gradle run
```

## Notes
- Examples are intentionally small and focused.
- Some classes may be used for experimentation and logging to observe GC and scheduling behavior.
