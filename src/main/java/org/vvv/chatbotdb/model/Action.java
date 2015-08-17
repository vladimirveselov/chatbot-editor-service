package org.vvv.chatbotdb.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Action {
    
    private Long id;
    
    private Long priority = 0l;
    
    private String ruleName;
    
    private String topicName;
    
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    private String actionBody;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActionBody() {
        return actionBody;
    }

    public void setActionBody(String actionBody) {
        this.actionBody = actionBody;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Action [id=" + id + ", priority=" + priority + ", ruleName="
                + ruleName + ", topicName=" + topicName + ", actionBody="
                + actionBody + "]";
    }


   
}
