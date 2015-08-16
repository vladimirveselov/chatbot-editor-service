package org.vvv.chatbot.client.excel;

import java.util.List;

public class ExcelSheet {
    
    private String name;
    
    private List<List<String>> data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }

}
