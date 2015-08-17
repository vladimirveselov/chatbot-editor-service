package org.vvv.chatbotdb.model;

import java.util.Date;

public class SMMemory {
	
	private Long id;
	
	private String sessionId;
	
	private String stateMachineName;
	
	private String smVariableName;
	
	private Boolean value;
	
	private Date lastModified;
	
	private String shortStringValue;
	
	private String longStringValue;

	public String getShortStringValue() {
        return shortStringValue;
    }

    public void setShortStringValue(String shortStringValue) {
        this.shortStringValue = shortStringValue;
    }

    public String getLongStringValue() {
        return longStringValue;
    }

    public void setLongStringValue(String longStringValue) {
        this.longStringValue = longStringValue;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Boolean getValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

    public String getStateMachineName() {
        return stateMachineName;
    }

    public void setStateMachineName(String stateMachineName) {
        this.stateMachineName = stateMachineName;
    }

    public String getSmVariableName() {
        return smVariableName;
    }

    public void setSmVariableName(String smVariableName) {
        this.smVariableName = smVariableName;
    }

    @Override
    public String toString() {
        return "SMMemory [id=" + id + ", sessionId=" + sessionId
                + ", stateMachineName=" + stateMachineName
                + ", smVariableName=" + smVariableName + ", value=" + value
                + ", lastModified=" + lastModified + ", shortStringValue="
                + shortStringValue + ", longStringValue=" + longStringValue
                + "]";
    }
    
    

}
