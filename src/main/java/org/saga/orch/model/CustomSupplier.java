package org.saga.orch.model;

@FunctionalInterface
public interface CustomSupplier<T> {

   T get() throws RuntimeException;
}
