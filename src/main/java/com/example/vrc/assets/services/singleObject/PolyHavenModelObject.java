package com.example.vrc.assets.services.singleObject;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class PolyHavenModelObject implements Object{
    private String name;
    private String url;
    private String thumbnailUrl;
    @Override
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
        if(!modelObject.has("gltf")) return;
        JSONObject gltf = modelObject.getJSONObject("gltf");
        JSONObject lowQuality = gltf.getJSONObject("1k");
        JSONObject lastGltf = lowQuality.getJSONObject("gltf");
        String gltfUrl = lastGltf.getString("url");
        String thumbnailUrl = String
                .format("https://cdn.polyhaven.com/asset_img/thumbs/%s.png?format=jpeg" , name);
        this.url = gltfUrl;
        this.thumbnailUrl = thumbnailUrl;
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
    public void setName(String name){
        this.name = name;
    }

}
