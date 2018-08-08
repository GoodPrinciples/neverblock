package org.goodprinciples.neverblock.task;

import org.goodprinciples.neverblock.scheduler.Scheduler;
import org.goodprinciples.neverblock.task.Task.Status;
import org.junit.Assert;
import org.junit.Test;

public class TaskTest {
	
	@Test
	public void received() {
		String requestPayload = "request payload";
		Task<String, String> task = new Task<String, String>(requestPayload);
		
		Assert.assertEquals("when a new task is created it must be in RECEIVED status!", Status.RECEIVED, task.status());
		Assert.assertNotNull("task in RECEIVED status must have receivedOn date!", task);
		Assert.assertEquals("request payload must not be modified!", requestPayload, task.request());
	}

	@Test
	public void accepted() {
		String requestPayload = "request payload";
		Task<String, String> task = new Task<String, String>(requestPayload);

		task.accepted();

		Assert.assertEquals("when a task is accepted it must be in ACCEPTED status!", Status.ACCEPTED, task.status());
	}

	@Test
	public void released() {
		Task<String, String> task = new Task<String, String>("sample task");
		task.accepted();
		task.scheduled(new DummyScheduler());
		task.released(new DummyScheduler());

		Assert.assertEquals("released tasks must be in ACCEPTED status!", Task.Status.ACCEPTED, task.status());
		Assert.assertEquals("released tasks must have submittedBy = NONE", "NONE", task.scheduledBy());

		try {
			task.scheduledOn();
		} catch (Throwable t) {
			if (!IllegalStateException.class.isInstance(t)) {
				Assert.fail("released tasks must have submittedOn = null");
			}
		}
	}

	@Test
	public void scheduled() {
		Task<String, String> task = new Task<String, String>("sample task");
		task.accepted();
		task.scheduled(new DummyScheduler());

		Assert.assertEquals("when scheduled the task must be in SCHEULED status!", Task.Status.SCHEDULED,
				task.status());
		Assert.assertNotNull("scheduledOn must not be null!", task.scheduledOn());
		Assert.assertEquals("scheduledBy must be equals to the scheduler id!", "ID", task.scheduledBy());
	}

	private static class DummyScheduler implements Scheduler {

		@Override
		public void schedule() {
			// TODO Auto-generated method stub

		}

		@Override
		public String id() {
			return "ID";
		}

		@Override
		public void accept(Task<?, ?> task) {
			// TODO Auto-generated method stub

		}

		@Override
		public void release() {
			// TODO Auto-generated method stub

		}

	}

}
