package org.goodprinciples.neverblock.scheduler;

import org.goodprinciples.neverblock.task.Task;

public interface Scheduler {
	
	void accept(Task<?, ?> task);

}
