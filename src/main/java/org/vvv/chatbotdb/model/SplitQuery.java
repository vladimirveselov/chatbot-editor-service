package org.vvv.chatbotdb.model;

public class SplitQuery {

    private Long id;

    private String word;

    private String prevWord;

    private String nextWord;
    
    private Query query;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getPrevWord() {
        return prevWord;
    }

    public void setPrevWord(String prevWord) {
        this.prevWord = prevWord;
    }

    public String getNextWord() {
        return nextWord;
    }

    public void setNextWord(String nextWord) {
        this.nextWord = nextWord;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
    
}
