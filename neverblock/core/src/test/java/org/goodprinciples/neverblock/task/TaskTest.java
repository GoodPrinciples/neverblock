package org.goodprinciples.neverblock.task;

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

}
