package org.vvv.chatbotdb.model;

import java.util.ArrayList;
import java.util.List;

public class StateMachine {
	
	private Long id;
	
	private String name;
	
	private List<SMVariable> variables = new ArrayList<SMVariable>();
	
	private List<SMAction> actions = new ArrayList<SMAction>();
	
	private List<SMRule> rules = new ArrayList<SMRule>();

	public List<SMVariable> getVariables() {
		return variables;
	}

	public void setVariables(List<SMVariable> variables) {
		this.variables = variables;
	}

	public List<SMAction> getActions() {
		return actions;
	}

	public void setActions(List<SMAction> actions) {
		this.actions = actions;
	}

	public List<SMRule> getRules() {
		return rules;
	}

	public void setRules(List<SMRule> rules) {
		this.rules = rules;
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
}
