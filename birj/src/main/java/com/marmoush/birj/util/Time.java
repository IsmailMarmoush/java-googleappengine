package com.marmoush.birj.util;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Time {

    private String timezone;
    private final int year;
    private final int month;
    private final int day;
    private final int hour;
    private final int minute;
    private final int second;

    public Time() {
	this(TimeZone.getDefault());
    }

    public Time(TimeZone timezone) {
	final Calendar now = Calendar.getInstance(timezone);

	this.timezone = now.getTimeZone().getDisplayName();
	year = now.get(Calendar.YEAR);
	month = now.get(Calendar.MONTH) + 1;
	day = now.get(Calendar.DATE);
	hour = now.get(Calendar.HOUR);
	minute = now.get(Calendar.MINUTE);
	second = now.get(Calendar.SECOND);
    }

    public int getDay() {
	return day;
    }

    public int getHour() {
	return hour;
    }

    public int getMinute() {
	return minute;
    }

    public int getMonth() {
	return month;
    }

    public int getSecond() {
	return second;
    }

    public String getTimezone() {
	return timezone;
    }

    public int getYear() {
	return year;
    }
}
