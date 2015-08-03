package org.vvv.chatbotdb.model;

import java.util.Date;

public class SMMemory {
	
	private Long id;
	
	private String sessionId;
	
	private StateMachine stateMachine;
	
	private SMVariable smVariable;
	
	private Boolean value;
	
	private Date lastModified;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public SMVariable getSmVariable() {
		return smVariable;
	}

	public void setSmVariable(SMVariable smVariable) {
		this.smVariable = smVariable;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

}
