package com.example.fitbit_tracker.domain_model;

import com.example.fitbit_tracker.model.Reading;

import java.util.HashMap;
import java.util.List;

public class ReadingBatchDM {

    private String name;

    private HashMap<String,List<ReadingDM>> readingList;

    public ReadingBatchDM() {
    }

    public ReadingBatchDM(String name, HashMap<String, List<ReadingDM>> readingList) {
        this.name = name;
        this.readingList = readingList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, List<ReadingDM>> getReadingList() {
        return readingList;
    }

    public void setReadingList(HashMap<String, List<ReadingDM>> readingList) {
        this.readingList = readingList;
    }

}
