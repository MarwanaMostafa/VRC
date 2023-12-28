package com.example.vrc.assets.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Service
public class HdrisAssetsService {
    private boolean fetched = false;
    private static JSONArray objectsName;
    private static JSONArray Hdris = new JSONArray();
    private static String curModelName;
    private void fetchObjectsName(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.polyhaven.com/assets?t=hdris")).build();
        client.sendAsync(request , HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(HdrisAssetsService::parseObjectsName)
                .join();
        fetched = true;
    }
    public JSONArray fetchObjects(int pageNumber) {
        //models.clear();
        if (!fetched) {
            fetchObjectsName();
        }
        int numberOfModelsPerPage = 10;
        int startIdx = (pageNumber - 1) * numberOfModelsPerPage;
        int endIdx = Math.min(startIdx + numberOfModelsPerPage, objectsName.length());
        HttpClient client = HttpClient.newHttpClient();
        for(int i = startIdx ; i < endIdx ; i++){
            curModelName = objectsName.get(i).toString();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://api.polyhaven.com/files/%s" ,curModelName)))
                    .build();
            client.sendAsync(request , HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(HdrisAssetsService::parseModelObject)
                    .join();

        }
        return Hdris;

    }
    private static String parseObjectsName(String responseBody){
        JSONObject allObjects = new JSONObject(responseBody);
        objectsName = allObjects.names();
        return null;
    }
    private static String parseModelObject(String responseBody){
        JSONObject modelObject = new JSONObject(responseBody);
        JSONObject tonemapped = modelObject.getJSONObject("tonemapped");
        String thumbnailUrl = tonemapped.getString("url");
        JSONObject hdri = modelObject.getJSONObject("hdri");
        JSONObject lowQuality = hdri.getJSONObject("1k");
        JSONObject lasthdri = lowQuality.getJSONObject("hdr");
        String hdriUrl = lasthdri.getString("url");
        JSONObject modifiedModelObject = new JSONObject();
        modifiedModelObject.put("name" ,curModelName);
        modifiedModelObject.put("thumbnailUrl" ,thumbnailUrl);
        modifiedModelObject.put("hdriUrl" ,hdriUrl);
        Hdris.put(modifiedModelObject);
        return null;
    }
}
