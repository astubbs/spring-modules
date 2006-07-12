package org.springmodules.xt.test.domain;

/**
 * Load data into a memory store.
 *
 * @author Sergio Bossa
 */
public interface MemoryRepositoryLoader {
   public void loadInto(MemoryRepository store); 
}
