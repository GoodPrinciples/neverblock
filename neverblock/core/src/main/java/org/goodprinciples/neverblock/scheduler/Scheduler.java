package org.goodprinciples.neverblock.scheduler;

import org.goodprinciples.neverblock.task.Task;

public interface Scheduler {

	/**
	 * Accept the input task.
	 * 
	 * @param task
	 *            task to be accepted
	 */
	void accept(Task<?, ?> task);

	/**
	 * Returns the identifier for this scheduler.
	 * 
	 * @return scheduler's identifier
	 */
	String id();
	
	/**
	 * Releases all scheduled tasks. In practice all {@link Task} in {@link Task.Status#SCHEDULED} where
	 * {@link Task#scheduledBy()} is equals to {@link #id()} will be moved in {@link Task.Status#ACCEPTED} status.
	 */
	void release();

	/**
	 * Schedule all task in {@link Task.Status#ACCEPTED} status.
	 */
	void schedule();

}
