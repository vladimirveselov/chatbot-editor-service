package org.vvv.chatbotdb.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Input {
    
    private Long id;
    
    private String text;
    
    private Rule rule;

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

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
    

}
