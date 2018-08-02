package org.goodprinciples.neverblock.scheduler;

import org.easymock.EasyMock;
import org.goodprinciples.neverblock.task.Task;
import org.goodprinciples.neverblock.task.TaskRepository;
import org.junit.Assert;
import org.junit.Test;

public class TestOnDemandScheduler {

	private OnDemandScheduler classUnderTest = null;

	private TaskRepository taskRepositoryMock = null;

	@Test
	public void accept() {
		taskRepositoryMock = EasyMock.createMock(TaskRepository.class);
		taskRepositoryMock.save(EasyMock.isA(Task.class));

		classUnderTest = new OnDemandScheduler(taskRepositoryMock);

		Task<String, String> task = new Task<String, String>("sample task");

		EasyMock.replay(taskRepositoryMock);

		classUnderTest.accept(task);

		EasyMock.verify(taskRepositoryMock);

		Assert.assertEquals("after scheduler acceptante the task must be in ACCEPTED status!", Task.Status.ACCEPTED,
				task.status());
	}

}
