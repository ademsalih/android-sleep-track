package com.example.fitbit_tracker.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtils {

    public static String timeDeltaLabel(long ms) {
        long seconds = ms/1000;

        if (seconds < 10) {
            return "now";
        } else if (seconds < 60) {
            return seconds + " seconds ago";
        } else if ( seconds < 3600) {
            int minutes = (int) seconds / 60;
            if (minutes > 1) {
                return minutes + " minutes ago";
            } else {
                return "1 minute ago";
            }
        } else if (seconds < 86400) {
            int hours = (int) seconds / 3600;
            if (hours > 1) {
                return hours + " hours ago";
            } else {
                return "1 hour ago";
            }
        } else if (seconds < 7*86400) {
            int days = (int) seconds / 86400;
            if (days > 1) {
                return days + " days ago";
            } else {
                return "1 day ago";
            }
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMANY);
            return simpleDateFormat.format(new Date(ms));
        }
    }

    public static String formattedTimeLabel(long ms) {
        long seconds = ms/1000;

        if (seconds < 60) {
            return seconds + " seconds";
        } else if (seconds < 3600) {
            int minute = (int) seconds / 60;
            int remainderSeconds = (int) seconds % 60;
            String duration = String.valueOf(minute);

            if (minute > 1) {
                duration += " minutes";
            } else {
                duration += " minute";
            }

            if (remainderSeconds > 1) {
                duration += ", " + remainderSeconds + " seconds";
            } else if (remainderSeconds > 0) {
                duration += ", " + remainderSeconds + " second";
            }

            return duration;
        } else {
            int hour = (int) seconds/3600;
            int remainderSeconds = (int) seconds % 3600;
            int remainderMinutes = (int) remainderSeconds / 60;
            String duration = String.valueOf(hour);

            if (hour > 1 ) {
                duration += " hours";
            } else {
                duration += " hour";
            }

            if (remainderMinutes > 1) {
                duration += ", " + remainderMinutes + " minutes";
            } else if (remainderMinutes > 0) {
                duration += ", " + remainderMinutes + " minute";
            }

            return duration;
        }
    }
}
