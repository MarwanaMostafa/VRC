package com.example.vrc.assets.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Service
public class ModelsAssetsService {
    private boolean fetched = false;
    private static JSONArray objectsName;
    private static JSONArray models = new JSONArray();
    private static String curModelName;

    private void fetchObjectsName(){
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://api.polyhaven.com/assets?t=models")).build();
        client.sendAsync(request , HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenApply(ModelsAssetsService::parseObjectsName)
                .join();
        fetched = true;
    }
    public JSONArray fetchObjects(int pageNumber) {
        models.clear();
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
                    .thenApply(ModelsAssetsService::parseModelObject)
                    .join();

        }
        return models;

    }
    private static String parseObjectsName(String responseBody){
        JSONObject allObjects = new JSONObject(responseBody);
        objectsName = allObjects.names();
        return null;
    }
    private static String parseModelObject(String responseBody){
        JSONObject modelObject = new JSONObject(responseBody);
        JSONObject gltf = modelObject.getJSONObject("gltf");
        JSONObject lowQuality = gltf.getJSONObject("1k");
        JSONObject lastGltf = lowQuality.getJSONObject("gltf");
        String gltfUrl = lastGltf.getString("url");
        String thumbnailUrl = String
                .format("https://cdn.polyhaven.com/asset_img/thumbs/%s.png?format=png" , curModelName);
        JSONObject modifiedModelObject = new JSONObject();
        modifiedModelObject.put("name" ,curModelName);
        modifiedModelObject.put("gltfUrl" ,gltfUrl);
        modifiedModelObject.put("thumbnailUrl" , thumbnailUrl);
        models.put(modifiedModelObject);
        return null;
    }
}
