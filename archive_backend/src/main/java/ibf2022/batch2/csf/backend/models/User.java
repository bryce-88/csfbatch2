package ibf2022.batch2.csf.backend.models;

import java.util.Arrays;
import java.util.List;

public class User {
    private String bundleId;
    private String date;
    private String name;
    private String title;
    private String comments;
    private List<String> urls;

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getBundleId() {
        return bundleId;
    }
    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getComments() {
        return comments;
    }
    public void setComments(String comments) {
        this.comments = comments;
    }
    public List<String> getUrls() {
        return urls;
    }
    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
    public User() {
    }
    public User(String bundleId, String date, String name, String title, String comments, List<String> urls) {
        this.bundleId = bundleId;
        this.date = date;
        this.name = name;
        this.title = title;
        this.comments = comments;
        this.urls = urls;
    }
    @Override
    public String toString() {
        return "User [bundleId=" + bundleId + ", date=" + date + ", name=" + name + ", title=" + title + ", comments="
                + comments + ", urls=" + urls + "]";
    }


    
    
    



    
}
