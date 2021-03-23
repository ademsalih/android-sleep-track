package com.example.fitbit_tracker.model;

import com.example.fitbit_tracker.model.reading.Reading;

import java.util.List;

public class ReadingBatch {

    private String sensorName;
    private List<List<? extends Reading>> readings;

    public ReadingBatch() {

    }

    public ReadingBatch(String sensorName, List<List<? extends Reading>> readings) {
        this.sensorName = sensorName;
        this.readings = readings;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public List<List<? extends Reading>> getReadings() {
        return readings;
    }

    public void setReadings(List<List<? extends Reading>> readings) {
        this.readings = readings;
    }

}
