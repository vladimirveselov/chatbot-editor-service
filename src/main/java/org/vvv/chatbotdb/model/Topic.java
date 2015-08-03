package org.vvv.chatbotdb.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Topic {

    private Long id;

    private String topicName;

    private Long rank = 100L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }
    
    public Topic withTopichName(String topicName) {
    	this.setTopicName(topicName);
    	return this;
    }

    public Topic withRank(Long rank) {
    	this.setRank(rank);
    	return this;
    }

    
}
