package org.goodprinciples.neverblock.task;

import java.util.List;

import org.goodprinciples.neverblock.scheduler.Scheduler;

public interface TaskRepository {
	
	List<Task<?, ?>> findByStatus(Task.Status status);

	List<Task<?, ?>> findByStatusAndScheduler(Task.Status status, Scheduler scheduler);

	void insert(Task<?, ?> task);

	void update(Task<?, ?> task);

}
