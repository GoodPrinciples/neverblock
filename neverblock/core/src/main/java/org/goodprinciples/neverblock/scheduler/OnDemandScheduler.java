package org.goodprinciples.neverblock.scheduler;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.goodprinciples.neverblock.task.Task;
import org.goodprinciples.neverblock.task.TaskRepository;

public class OnDemandScheduler implements Scheduler {
	
	private static final Logger LOGGER = LogManager.getLogger(OnDemandScheduler.class);

	// SECTION: instance variables
	
	private String id = null;
	private TaskRepository taskRepository = null;
	
	// SECTION: constructors
	
	/**
	 * Creates a new instance of this scheduler. The identifiers will be automatically generated using the host name of
	 * machine running the class.
	 * 
	 * @param taskRepository
	 *            task repository
	 */
	public OnDemandScheduler(TaskRepository taskRepository) {
		this(null, taskRepository);
	}

	/**
	 * Creates a new instance of this scheduler.
	 * 
	 * @param id
	 *            scheduler's identifier
	 * @param taskRepository
	 *            task repository
	 */
	public OnDemandScheduler(String id, TaskRepository taskRepository) {
		setId(id);
		setTaskRepository(taskRepository);
	}
	
	// SECTION: public methods

	/**
	 * {@inheritDoc}
	 */
	@Override // inherited from org.goodprinciples.neverblock.scheduler.Scheduler
	public void accept(Task<?, ?> task) {
		task.accepted();
		taskRepository.insert(task);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // inherited from org.goodprinciples.neverblock.scheduler.Scheduler
	public String id() {
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // inherited from org.goodprinciples.neverblock.scheduler.Scheduler
	public void release() {
		List<Task<?, ?>> scheduledTasks = taskRepository.findByStatusAndScheduler(Task.Status.SCHEDULED, this);
		for (Task<?, ?> task: scheduledTasks) {
			task.released(this);
			taskRepository.update(task);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override // inherited from org.goodprinciples.neverblock.scheduler.Scheduler
	public void schedule() {
		List<Task<?, ?>> acceptedTasks = taskRepository.findByStatus(Task.Status.ACCEPTED);
		for (Task<?, ?> task: acceptedTasks) {
			task.scheduled(this);
			taskRepository.update(task);
		}
	}

	// SECTION: private methods

	private String generateIdFromHostName() {
		try {
			return InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			LOGGER.warn("can't detect the hostname! the loopback address will be used.", e);
			return InetAddress.getLoopbackAddress().getHostName();
		}
	}

	private void setId(String id) {
		if (id == null || id.trim().isEmpty()) {
			this.id = generateIdFromHostName();
		} else {
			this.id = id.trim();
		}
	}

	private void setTaskRepository(TaskRepository taskRepository) {
		if (taskRepository == null) {
			throw new IllegalArgumentException();
		}

		this.taskRepository = taskRepository;
	}

}
