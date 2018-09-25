package com.example.android.newsapp;

public class NewsEvent {
    private String segmentTitle, sectionName, segmentURL, date, byline;

    public NewsEvent(String segmentTitle, String sectionName, String segmentURL, String date,
                     String author){
        this.segmentTitle=segmentTitle;
        this.sectionName=sectionName;
        this.segmentURL=segmentURL;
        this.date=date;
        this.byline=author;
    }

    public String getSegmentTitle() {
        return segmentTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getSegmentURL() {
        return segmentURL;
    }

    public String getDate() {
        return date;
    }

    public String getByline() {
        return byline;
    }

}

