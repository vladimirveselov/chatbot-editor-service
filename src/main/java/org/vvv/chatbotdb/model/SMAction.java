package org.vvv.chatbotdb.model;

public class SMAction {
	
	@Override
	public String toString() {
		return "SMAction [name=" + name + "]";
	}

	private Long id;
	
	private StateMachine stateMachine;
	
	private String name;
	
	private String actionScript;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
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

	public String getActionScript() {
		return actionScript;
	}

	public void setActionScript(String actionScript) {
		this.actionScript = actionScript;
	}

}
