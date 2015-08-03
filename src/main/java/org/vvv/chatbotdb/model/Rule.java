package org.vvv.chatbotdb.model;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
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
    
    public Rule withName(String name) {
    	this.setName(name);
    	return this;
    }

    public Rule withRank(Long rank) {
    	this.setRank(rank);
    	return this;
    }
    
    public Rule withResponse(String response) {
    	this.setResponse(response);
    	return this;
    }
    
    public Rule withInputs(String ...inputs) {
    	for (String inputText : inputs) {
    		Input input = new Input();
    		input.setText(inputText);
    		input.setRule(this);
    		this.inputs.add(input);
    	}
    	return this;
    }
    

    public Rule withOutputs(String ...outputs) {
    	for (String outputText : outputs) {
    		Output output = new Output();
    		output.setText(outputText);
    		output.setRule(this);
    		this.outputs.add(output);
    	}
    	return this;
    }

    public Rule withOutputs(Output ...outputs) {
    	for (Output output : outputs) {
    		output.setRule(this);
    		this.outputs.add(output);
    	}
    	return this;
    }
    
    public Rule withTopic(Topic topic) {
    	this.setTopic(topic);
    	return this;
    }

	@Override
	public String toString() {
		return "Rule [id=" + id + ", topic=" + topic + ", name=" + name
				+ ", response=" + response + ", rank=" + rank + "]";
	}


}
