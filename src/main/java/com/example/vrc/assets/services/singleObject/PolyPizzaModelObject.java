package com.example.vrc.assets.services.singleObject;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PolyPizzaModelObject implements Object{
    private String id = "";
    private String name = "";
    private String url = "";
    private String thumbnailUrl = "";

    @Override
    public void fetch() throws IOException, InterruptedException {


    }

    @Override
    public void parse(String responseBody) {
        System.out.println(responseBody);
        if(responseBody.charAt(0) != '{') return;
        JSONObject modelObject = new JSONObject(responseBody);
        this.name = modelObject.getString("Title");
        this.url = modelObject.getString("Download");
        this.thumbnailUrl = modelObject.getString("Thumbnail");

    }

    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    @Override
    public String getUrl() {
        return url;
    }
    public void setId(String id){
        this.id = id;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public void setThumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
    }

}
