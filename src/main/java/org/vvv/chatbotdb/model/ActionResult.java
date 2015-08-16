package org.vvv.chatbotdb.model;

import java.util.HashMap;
import java.util.Map;

public class ActionResult {
    
    private String response;
    
    private String content;
    
    private Map<String, String> memory = new HashMap<String, String>();
    
    private String sessionId;
    
    private String request;
    
    private StringBuffer script = new StringBuffer();

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String> getMemory() {
        return memory;
    }

    public void setMemory(Map<String, String> memory) {
        this.memory = memory;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getScript() {
        return this.script.toString();
    }
    
    public void append(String script) {
        this.script.append(script);
        this.script.append(";\r\n");
    }

}
