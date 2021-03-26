package com.example.fitbit_tracker.domain_model;

import java.util.HashMap;
import java.util.List;

public class Batch {

    private String batchSensorName;
    private HashMap<String,List<ReadingDM>> batchMap;

    public Batch() {
    }

    public Batch(String batchSensorName, HashMap<String, List<ReadingDM>> batchMap) {
        this.batchSensorName = batchSensorName;
        this.batchMap = batchMap;
    }

    public String getBatchSensorName() {
        return batchSensorName;
    }

    public void setBatchSensorName(String batchSensorName) {
        this.batchSensorName = batchSensorName;
    }

    public HashMap<String, List<ReadingDM>> getBatchMap() {
        return batchMap;
    }

    public void setBatchMap(HashMap<String, List<ReadingDM>> batchMap) {
        this.batchMap = batchMap;
    }

}
