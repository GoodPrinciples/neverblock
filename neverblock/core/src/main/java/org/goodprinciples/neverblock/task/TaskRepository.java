package org.goodprinciples.neverblock.task;

import java.util.List;

public interface TaskRepository {
	
	List<Task<?, ?>> findByStatus(Task.Status status);

	void insert(Task<?, ?> task);

	void update(Task<?, ?> task);

}
