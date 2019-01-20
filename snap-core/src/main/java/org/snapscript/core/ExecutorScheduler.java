package org.snapscript.core;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicReference;

import org.snapscript.common.Consumer;
import org.snapscript.core.error.InternalStateException;
import org.snapscript.core.variable.Value;

public class ExecutorScheduler implements TaskScheduler {

   private final Executor executor;

   public ExecutorScheduler(Executor executor) {
      this.executor = executor;
   }

   @Override
   public <T> Promise<T> schedule(Consumer<Object, T> consumer) {
      FuturePromise<T> promise = new FuturePromise<T>(consumer);

      if(consumer != null) {
         promise.execute(executor);
      }
      return promise;
   }

   private static class FuturePromise<T> implements Promise<T> {

      private final Set<Consumer<Throwable, Object>> failures;
      private final Set<Consumer<T, Object>> listeners;
      private final AtomicReference<Throwable> error;
      private final AtomicReference<Value> success;
      private final FutureTask<T> future;
      private final Callable<T> task;

      public FuturePromise(Consumer<Object, T> consumer) {
         this.failures = new CopyOnWriteArraySet<Consumer<Throwable, Object>>();
         this.listeners = new CopyOnWriteArraySet<Consumer<T, Object>>();
         this.task = new FutureExecution<T>(this, consumer);
         this.error = new AtomicReference<Throwable>();
         this.success = new AtomicReference<Value>();
         this.future = new FutureTask<T>(task);
      }

      @Override
      public T get() {
         try {
            return (T)future.get();
         } catch(Exception e) {
            throw new InternalStateException("Could not get value", e);
         }
      }

      @Override
      public T get(long wait) {
         try {
            return (T)future.get(wait, MILLISECONDS);
         } catch(Exception e) {
            throw new InternalStateException("Could not get value", e);
         }
      }

      @Override
      public Promise<T>  block() {
         try {
            future.get();
         } catch(Exception e) {
            return this;
         }
         return this;
      }

      @Override
      public Promise<T>  block(long wait) {
         try {
            future.get(wait, MILLISECONDS);
         } catch(Exception e) {
            return this;
         }
         return this;
      }

      @Override
      public Promise<T> fail(Consumer<Throwable, Object> consumer) {
         Throwable value = error.get();

         if(value != null) {
            consumer.consume(value);
         }
         failures.add(consumer);
         return this;
      }

      @Override
      public Promise<T> then(Consumer<T, Object> consumer) {
         Value value = success.get();

         if(value != null) {
            T object = value.getValue();
            consumer.consume(object);
         }
         listeners.add(consumer);
         return this;
      }

      public void execute(Executor executor) {
         if(executor != null) {
            executor.execute(future);
         } else {
            future.run();
         }
      }

      public void complete(Value value) {
         T object = value.getValue();

         for(Consumer<T, Object> listener : listeners) {
            listener.consume(object);
         }
         success.compareAndSet(null, value);
      }

      public void error(Throwable cause) {
         for(Consumer<Throwable, Object> failure : failures) {
            failure.consume(cause);
         }
         error.compareAndSet(null, cause);
      }
   }

   private static class FutureExecution<T> implements Callable<T> {

      private final Consumer<Object, T> consumer;
      private final FuturePromise<T> promise;

      public FutureExecution(FuturePromise<T> promise, Consumer<Object, T> consumer) {
         this.consumer = consumer;
         this.promise = promise;
      }

      @Override
      public T call() throws Exception {
         try {
            T result = consumer.consume(null);
            Value value = Value.getTransient(result);

            promise.complete(value);
            return result;
         } catch(Exception cause) {
            promise.error(cause);
            throw cause;
         }
      }
   }
}