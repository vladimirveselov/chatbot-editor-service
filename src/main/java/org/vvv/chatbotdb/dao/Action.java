package org.vvv.chatbotdb.dao;

public class Action {
    
    private Long id;
    
    private Long priority;
    
    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    private Rule rule;
    
    private String actionBody;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getActionBody() {
        return actionBody;
    }

    public void setActionBody(String actionBody) {
        this.actionBody = actionBody;
    }
    
}
