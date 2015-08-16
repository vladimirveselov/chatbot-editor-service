package org.vvv.chatbotdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Topic {

    private Long id;

    private String topicName;

    private Long rank = 100L;
    
    private Set<Rule> rules = new HashSet<Rule>();

    public Set<Rule> getRules() {
        return rules;
    }

    public void setRules(Set<Rule> rules) {
        this.rules = rules;
    }

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

    @Override
    public String toString() {
        return "Topic [id=" + id + ", topicName=" + topicName + ", rank="
                + rank + ", rules=" + rules + "]";
    }

    
}
