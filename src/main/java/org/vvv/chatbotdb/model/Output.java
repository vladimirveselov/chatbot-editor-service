package org.vvv.chatbotdb.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Output {
    
    private Long id;
    
    private String text;
    
    private String request;
    
    private Rule rule;

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

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
    
    
}
