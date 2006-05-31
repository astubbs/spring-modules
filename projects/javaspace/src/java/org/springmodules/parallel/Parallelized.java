package org.springmodules.parallel;

/**
 * Interface mixed into parallel return
 * 
 * TODO tie with transaction completion?
 * 
 * TODO parallel annotation
 * ParallelTemplate: Parallelized doParallel() ...
 * What about threading implications?? Don't really need
 * another thread
 * 
 * TODO should this be regular license,
 * or special license? (c) Interface21, open source but
 * cannot modify without permission, cannot copy or fork.
 * Cannot use in production without express permission of I21,
 * there may be a fee.
 * 
 * ISsue with success of first?
 * 
 * What about checked exceptions? When do they get thrown?
 * Isn't catch block meaningless, unless you can actually block
 * at the end of the catch block...but that changes Java language
 * semantics
 * 
 * must register parallel operations with transaction interceptor
 * 
 * @author Rod Johnson
 *
 */
public interface Parallelized {
    
    boolean isComplete();
    
    /**
     * Block until the task is complete.
     * @throws ParallelException
     */
    void join() throws ParallelException;
    
    /**
     * 
     * @return whether or not the task was killed
     * @throws ParallelException
     */
    boolean kill() throws ParallelException;
    
    // TODO any invocation on the target object causes join()
    // to be executed, will result in exception being thrown??

}
