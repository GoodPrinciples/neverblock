package org.goodprinciples.neverblock.task;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

import org.goodprinciples.neverblock.scheduler.Scheduler;

public class Task<REQ extends Serializable, RESP extends Serializable> implements Serializable {

	private static final long serialVersionUID = -8483137203782334895L;
	
	private static final String NONE = "NONE";

	public static enum Status {
		ACCEPTED, RECEIVED, SCHEDULED
	}
	
	//SECTION: instance variables
	
	private Calendar acceptedOn = null;
	private UUID id = null;
	private Calendar receivedOn = null;
	private REQ request = null;
	private String scheduledBy = NONE;
	private Calendar scheduledOn = null;
	private Status status = null;
	
	//SECTION: constructors
	
	public Task(REQ request) {
		received();
		setId(UUID.randomUUID());
		setRequest(request);
	}
	
	//SECTION: public methods
	
	public void accepted() {
		setStatus(Status.ACCEPTED);
		setAcceptedOn(new GregorianCalendar());
	}
	
	public Calendar acceptedOn() {
		if (acceptedOn == null) {
			throw new IllegalStateException("acceptedOn not available for task in " + status);
		}
		return (Calendar) acceptedOn.clone();
	}
	
	public UUID id() {
		return id;
	}
	
	public Calendar receivedOn() {
		if (receivedOn == null) {
			throw new IllegalStateException("receivedOn not available for task in " + status);
		}
		return (Calendar) receivedOn.clone();
	}
	
	public void released(Scheduler scheduler) {
		if (scheduler == null) {
			throw new IllegalArgumentException("scheduler can't be null!");
		}

		if (!scheduler.id().equals(scheduledBy)) {
			throw new IllegalArgumentException("task " + id + " can't be released by " + scheduler.id()
					+ " because is has been scheduled by " + scheduledBy + "!");
		}

		setStatus(Status.ACCEPTED);
		this.scheduledBy = NONE;
		this.scheduledOn = null;
	}

	public REQ request() {
		return request;
	}

	public void scheduled(Scheduler scheduler) {
		setStatus(Status.SCHEDULED);
		setScheduledBy(scheduler);
		setScheduledOn(new GregorianCalendar());
	}

	public String scheduledBy() {
		return scheduledBy;
	}

	public Calendar scheduledOn() {
		if (scheduledOn == null) {
			throw new IllegalStateException("scheduledOn not available for task in " + status);
		}
		return (Calendar) scheduledOn.clone();
	}

	public Status status() {
		return status;
	}
	
	@Override // inherited from java.lang.Object
	public String toString() {
		return new StringBuilder("Task {id = \"").append(id).append("\", status = \"").append(status).append("\"}")
				.toString();
	}

	//SECTION: private methods
	
	private boolean isLegalStatusTransition(Status nextStatus) {
		if (this.status == null) {
			return nextStatus == Status.RECEIVED;
		}
		
		switch (this.status) {
			case ACCEPTED:
				return nextStatus == Status.SCHEDULED;
			case RECEIVED:
				return nextStatus == Status.ACCEPTED;
			case SCHEDULED:
				return nextStatus == Status.ACCEPTED;
			default:
				return false;
		}
	}
	
	private void received() {
		setStatus(Status.RECEIVED);
		setReceivedOn(new GregorianCalendar());
	}

	private void setAcceptedOn(Calendar acceptedOn) {
		if (acceptedOn == null) {
			throw new IllegalArgumentException("acceptedOn can't be null!");
		}
		
		this.acceptedOn = acceptedOn;
	}
	
	private void setId(UUID id) {
		if (id == null) {
			throw new IllegalArgumentException("id can't be null!");
		}
		
		this.id = id;
	}
	
	private void setReceivedOn(Calendar receivedOn) {
		if (receivedOn == null) {
			throw new IllegalArgumentException("receivedOn can't be null!");
		}
		
		this.receivedOn = receivedOn;
	}
	
	private void setRequest(REQ request) {
		if (request == null) {
			throw new IllegalArgumentException("request can't be null!");
		}
		
		this.request = request;
	}
	
	private void setScheduledBy(Scheduler scheduler) {
		if (scheduler == null) {
			throw new IllegalArgumentException("scheduler can't be null!");
		}

		this.scheduledBy = scheduler.id();
	}

	private void setScheduledOn(Calendar scheduledOn) {
		if (scheduledOn == null) {
			throw new IllegalArgumentException("scheduledOn can't be null!");
		}

		this.scheduledOn = scheduledOn;
	}

	private void setStatus(Status nextStatus) {
		if (nextStatus == null) {
			throw new IllegalArgumentException("task status can't be null!");
		}
		
		if (!isLegalStatusTransition(nextStatus)) {
			throw new IllegalArgumentException("can't move task status from " + this.status + " to " + nextStatus + "!");
		}
		
		this.status = nextStatus;
	}

}
