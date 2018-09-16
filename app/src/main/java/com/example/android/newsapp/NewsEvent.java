package com.example.android.newsapp;

public class NewsEvent {
    private String segmentTitle, sectionName, segmentURL, date, byline;

    public NewsEvent(String segmentTitle, String sectionName, String author, String date,
                     String segmentURL){
        this.segmentTitle=segmentTitle;
        this.sectionName=sectionName;
        this.byline=author;
        this.date=date;
        this.segmentURL=segmentURL;
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

