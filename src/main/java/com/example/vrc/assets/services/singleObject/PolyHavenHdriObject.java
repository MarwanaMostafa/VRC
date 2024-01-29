package com.example.vrc.assets.services.singleObject;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class PolyHavenHdriObject implements Object{
    private String name =  "";
    private String url = "";
    private String thumbnailUrl = "";
    private String id = "";
    public void fetch() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.polyhaven.com/files/%s" ,name)))
                .build();
        HttpResponse<String> responseBody = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        parse(responseBody.body());
    }
    @Override
    public void parse(String responseBody) {
        JSONObject modelObject = new JSONObject(responseBody);
        String thumbnailUrl = String.format("https://cdn.polyhaven.com/asset_img/thumbs/%s.png?format=jpeg" ,
                this.name);
        JSONObject hdri = modelObject.getJSONObject("hdri");
        JSONObject lowQuality = hdri.getJSONObject("1k");
        JSONObject lasthdri = lowQuality.getJSONObject("hdr");
        String hdriUrl = lasthdri.getString("url");
        this.url = hdriUrl;
        this.thumbnailUrl = thumbnailUrl;
        this.id = name;
    }

    public String getName(){
        return name;
    }
    public String getUrl(){
        return url;
    }
    public String getThumbnailUrl(){
        return thumbnailUrl;
    }
    public String getId() {
        return id;
    }
    public void setName(String name){
        this.name = name;
    }

}
