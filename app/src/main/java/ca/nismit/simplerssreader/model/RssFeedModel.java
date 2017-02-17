package ca.nismit.simplerssreader.model;

public class RssFeedModel {
    private String title;
    private String link;
    private String summary;
    private String content;
    private String thumbnail;
    private String date;
    private long published;

    public RssFeedModel(String title, String summary, String content, String link, String thumbnail, String date, long published) {
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.link = link;
        this.thumbnail = thumbnail;
        this.date = date;
        this.published = published;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }
}
