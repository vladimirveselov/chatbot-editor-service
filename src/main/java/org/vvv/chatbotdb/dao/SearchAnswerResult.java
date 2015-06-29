package org.vvv.chatbotdb.dao;

public class SearchAnswerResult {
    
    private Long queryId;
    
    private Long ruleId;
    
    private Long inputId;
    
    private Double score;

    public Long getQueryId() {
        return queryId;
    }

    public void setQueryId(Long queryId) {
        this.queryId = queryId;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }

    public Long getInputId() {
        return inputId;
    }

    public void setInputd(Long inputId) {
        this.inputId = inputId;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }


}
