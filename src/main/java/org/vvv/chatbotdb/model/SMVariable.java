package org.vvv.chatbotdb.model;

public class SMVariable {
	
	private Long id;
	
	private StateMachine stateMachine;
	
	private String name;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	@Override
	public String toString() {
		return "SMVariable [name=" + name + "]";
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
