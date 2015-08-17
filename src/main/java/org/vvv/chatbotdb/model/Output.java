package org.vvv.chatbotdb.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Output {
    
    private Long id;
    
    private String text;
    
    private String request;
    
    private String ruleName;
    
    private String topicName;

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
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
    
    public Output withText(String text) {
    	this.setText(text);
    	return this;
    }

    public Output withRequest(String request) {
    	this.setRequest(request);
    	return this;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    @Override
    public String toString() {
        return "Output [id=" + id + ", text=" + text + ", request=" + request
                + ", ruleName=" + ruleName + "]";
    }

    
}
