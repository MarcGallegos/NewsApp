public class NewsEvent {
    private String segmentTitle, sectionName, author, date, segmentURL;

    public NewsEvent(String segmentTitle, String sectionName, String author, String date,
                     String segmentURL){
        this.segmentTitle=segmentTitle;
        this.sectionName=sectionName;
        this.author=author;
        this.date=date;
        this.segmentURL=segmentURL;
    }

    public String getSegmentTitle() {
        return segmentTitle;
    }

    public String getSectionName() {
        return sectionName;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getSegmentURL() {
        return segmentURL;
    }
}
