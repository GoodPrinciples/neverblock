package org.goodprinciples.neverblock.task;

public interface TaskRepository {
	
	void save(Task<?, ?> task);

}
