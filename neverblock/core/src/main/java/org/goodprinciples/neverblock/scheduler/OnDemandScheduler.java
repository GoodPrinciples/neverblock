package org.goodprinciples.neverblock.scheduler;

import org.goodprinciples.neverblock.task.Task;
import org.goodprinciples.neverblock.task.TaskRepository;

public class OnDemandScheduler implements Scheduler {
	
	//SECTION: instance variables
	
	private TaskRepository taskRepository = null;
	
	//SECTION: constructors
	
	public OnDemandScheduler(TaskRepository taskRepository) {
		if (taskRepository == null) {
			throw new IllegalArgumentException();
		}
		
		this.taskRepository = taskRepository;
	}
	
	//SECTION: public methods

	@Override //inherited from org.goodprinciples.neverblock.scheduler.Scheduler
	public void accept(Task<?, ?> task) {
		task.accepted();
		taskRepository.save(task);
	}

}
