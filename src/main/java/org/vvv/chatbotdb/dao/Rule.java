package org.vvv.chatbotdb.dao;

import java.util.HashSet;
import java.util.Set;

public class Rule {
    
    private Long id;
    
    private Topic topic;
    
    private String name;
    
    private String response;
    
    private Long rank = 100L;
    
    private Set<Input> inputs = new HashSet<Input>();
    
    private Set<Output> outputs = new HashSet<Output>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Long getRank() {
        return rank;
    }

    public void setRank(Long rank) {
        this.rank = rank;
    }

    public Set<Input> getInputs() {
        return inputs;
    }

    public void setInputs(Set<Input> inputs) {
        this.inputs = inputs;
    }

    public Set<Output> getOutputs() {
        return outputs;
    }

    public void setOutputs(Set<Output> outputs) {
        this.outputs = outputs;
    }
    
    

}
