package org.vvv.chatbotdb.dao;

import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Chatbot {
    
    private Long id;
    
    private String name;
    
    private Set<Topic> topics;

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

    public Set<Topic> getTopics() {
        return topics;
    }

    public void setTopics(Set<Topic> topics) {
        this.topics = topics;
    }
    
}
