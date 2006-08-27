package org.springmodules.asynch;

/**
 * @author Rod Johnson
 *
 */
public interface ResponseTask {

    /**
     * Implemented to return the object that should be processed
     * @return the object that should be processed
     */
    Object getWhat() throws Throwable;

    /**
     * TODO what about transactional capabilities?
     * What about failure of call? Instigation API will handle that
     * @param what
     */
    void handle(Object what) ;//throws ;

}


/*
Parallel p1 = doParallel(new Parallel() {
    public Object what() {
    }
    );
}*/