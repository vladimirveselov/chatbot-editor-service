package org.vvv.chatbotdb.model;

public class Word {
    
    private Long id;
    
    private String word;
    
    private Long synonymTo;

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

    public Long getSynonymTo() {
        return synonymTo;
    }

    public void setSynonymTo(Long synonymTo) {
        this.synonymTo = synonymTo;
    }

}
