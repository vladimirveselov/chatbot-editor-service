package org.vvv.chatbotdb.model;

import java.util.HashSet;
import java.util.Set;

public class SMRule {
	
	private Long id;
	
	private String name;
	
	private StateMachine stateMachine;
	
	private Set<SMCondition> conditions = new HashSet<SMCondition>();
	
	private Set<SMAction> actions = new HashSet<SMAction>();	

	public Set<SMCondition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<SMCondition> conditions) {
		this.conditions = conditions;
	}

	public Set<SMAction> getActions() {
		return actions;
	}

	public void setActions(Set<SMAction> actions) {
		this.actions = actions;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StateMachine getStateMachine() {
		return stateMachine;
	}

	public void setStateMachine(StateMachine stateMachine) {
		this.stateMachine = stateMachine;
	}

	@Override
	public String toString() {
		return "SMRule [name=" + name + ", conditions=" + conditions
				+ ", actions=" + actions + "]";
	}
	
}
