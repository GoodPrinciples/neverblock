package org.goodprinciples.neverblock.scheduler;

import java.util.LinkedList;
import java.util.List;

import org.easymock.Capture;
import org.easymock.EasyMock;
import org.goodprinciples.neverblock.task.Task;
import org.goodprinciples.neverblock.task.TaskRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestOnDemandScheduler {

	private OnDemandScheduler classUnderTest = null;

	private TaskRepository taskRepositoryMock = null;

	@Before
	public void setUp() {
		taskRepositoryMock = EasyMock.createMock(TaskRepository.class);

		classUnderTest = new OnDemandScheduler(taskRepositoryMock);
	}

	@Test
	public void accept() {
		taskRepositoryMock.insert(EasyMock.isA(Task.class));

		Task<String, String> task = new Task<String, String>("sample task");

		EasyMock.replay(taskRepositoryMock);

		classUnderTest.accept(task);

		EasyMock.verify(taskRepositoryMock);

		Assert.assertEquals("after scheduler acceptante the task must be in ACCEPTED status!", Task.Status.ACCEPTED,
				task.status());
	}

	@Test
	public void release() {
		Task<String, String> task = new Task<String, String>("sample task");
		task.accepted();
		task.scheduled(classUnderTest);

		List<Task<?, ?>> scheduledTasks = new LinkedList<>();
		scheduledTasks.add(task);

		EasyMock.expect(taskRepositoryMock.findByStatusAndScheduler(EasyMock.eq(Task.Status.SCHEDULED),
				EasyMock.eq(classUnderTest))).andReturn(scheduledTasks);

		Capture<Task<?, ?>> capturedTask = EasyMock.newCapture();
		taskRepositoryMock.update(EasyMock.capture(capturedTask));

		EasyMock.replay(taskRepositoryMock);

		classUnderTest.release();

		EasyMock.verify(taskRepositoryMock);

		Assert.assertEquals("released tasks must be in ACCEPTED status!", Task.Status.ACCEPTED,
				capturedTask.getValue().status());
	}

	@Test
	public void schedule() {
		Task<String, String> task = new Task<String, String>("sample task");
		task.accepted();

		List<Task<?, ?>> acceptedTasks = new LinkedList<>();
		acceptedTasks.add(task);

		EasyMock.expect(taskRepositoryMock.findByStatus(EasyMock.eq(Task.Status.ACCEPTED))).andReturn(acceptedTasks);

		Capture<Task<?, ?>> capturedTask = EasyMock.newCapture();
		taskRepositoryMock.update(EasyMock.capture(capturedTask));
		
		EasyMock.replay(taskRepositoryMock);

		classUnderTest.schedule();
		
		EasyMock.verify(taskRepositoryMock);

		Assert.assertEquals("when scheduled the task must be in SCHEULED status!", Task.Status.SCHEDULED,
				capturedTask.getValue().status());
	}

}
