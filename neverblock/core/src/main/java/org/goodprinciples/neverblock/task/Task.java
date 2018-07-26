package org.goodprinciples.neverblock.task;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class Task<REQ extends Serializable, RESP extends Serializable> implements Serializable {

	private static final long serialVersionUID = -8483137203782334895L;
	
	public static enum Status {
		ACCEPTED,
		RECEIVED
	}
	
	//SECTION: instance variables
	
	private Calendar acceptedOn = null;
	private UUID id = null;
	private Calendar receivedOn = null;
	private REQ request = null;
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
		return (Calendar) acceptedOn.clone();
	}
	
	public UUID id() {
		return id;
	}
	
	public Calendar receivedOn() {
		return (Calendar) receivedOn.clone();
	}
	
	public REQ request() {
		return request;
	}
	
	public Status status() {
		return status;
	}
	
	//SECTION: private methods
	
	private boolean isLegalStatusTransition(Status nextStatus) {
		if (this.status == null) {
			return nextStatus == Status.RECEIVED;
		}
		
		switch (this.status) {
			case ACCEPTED:
				return false;
			case RECEIVED:
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
