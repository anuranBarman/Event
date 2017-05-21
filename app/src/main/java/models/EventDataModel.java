package models;

/**
 * Created by Anuran on 5/19/2017.
 */

public class EventDataModel {
    String id,title,description,category,filename,hostID;
    String signedUpOrNot,liked;

    public EventDataModel(String id, String title, String description, String category, String filename, String hostID,String signedUpOrNot,String liked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.filename = filename;
        this.hostID = hostID;
        this.signedUpOrNot=signedUpOrNot;
        this.liked=liked;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public String isSignedUpOrNot() {
        return signedUpOrNot;
    }

    public void setSignedUpOrNot(String signedUpOrNot) {
        this.signedUpOrNot = signedUpOrNot;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }
}
