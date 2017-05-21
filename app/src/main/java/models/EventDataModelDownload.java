package models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Anuran on 5/21/2017.
 */

public class EventDataModelDownload implements Serializable {
    @SerializedName("id")
    public String id;

    @SerializedName("title")
    public String title;

    @SerializedName("description")
    public String description;

    @SerializedName("category")
    public String category;

    @SerializedName("file_name")
    public String file_name;

    @SerializedName("host_id")
    public String host_id;


    public EventDataModelDownload(String id,String title,String description,String category,String file_name,String host_id){
        this.id=id;
        this.title=title;
        this.description=description;
        this.category=category;
        this.file_name=file_name;
        this.host_id=host_id;
    }

}
