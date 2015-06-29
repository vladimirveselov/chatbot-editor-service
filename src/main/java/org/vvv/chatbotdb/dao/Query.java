package org.vvv.chatbotdb.dao;

import java.util.Date;

public class Query {
    
    private Long id;
    
    private String text;
   
    private String session_id;
    
    private Date startDate;
    
    private String response;
    
    private String actions;
    
    private Long rule_id;
    
    private String request;
    
    private String chatbotName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public Long getRule_id() {
        return rule_id;
    }

    public void setRule_id(Long rule_id) {
        this.rule_id = rule_id;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }
    
    public String getChatbotName() {
        return chatbotName;
    }

    public void setChatbotName(String chatbotName) {
        this.chatbotName = chatbotName;
    }

    
}
