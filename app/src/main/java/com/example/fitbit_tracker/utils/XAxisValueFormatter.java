package com.example.fitbit_tracker.utils;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class XAxisValueFormatter extends ValueFormatter {

    private final DateFormat dateFormat;
    private long firstTimestamp;

    public XAxisValueFormatter(long firstTimestamp) {
        this.firstTimestamp = firstTimestamp;
        this.dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
    }

    @Override
    public String getAxisLabel(float timeDelta, AxisBase axis) {
        return getFormattedLabel(timeDelta);
    }

    private String getFormattedLabel(float ts) {
        long actualTimestamp = firstTimestamp + (long) ts;
        return dateFormat.format(new Date(actualTimestamp));
    }

}
