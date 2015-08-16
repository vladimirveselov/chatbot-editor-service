package org.vvv.chatbotdb.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SMRule {
	
	private Long id;
	
	private String name;
	
	private String stateMachineName;
	
	private Set<SMCondition> conditions = new HashSet<SMCondition>();
	
	private List<String> actions = new ArrayList<String>();
	
	private List<String> actionNames = new ArrayList<String>();    

	public Set<SMCondition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<SMCondition> conditions) {
		this.conditions = conditions;
	}

	public List<String> getActions() {
		return actions;
	}

	public void setActions(List<String> actions) {
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

    public List<String> getActionNames() {
        return actionNames;
    }

    public void setActionNames(List<String> actionNames) {
        this.actionNames = actionNames;
    }

    public String getStateMachineName() {
        return stateMachineName;
    }

    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }

	
}
