package com.example.vrc.assets.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Asset {
    private boolean fetched = false;
    protected static String curModelName;
    private JSONArray objectsName;
    protected JSONArray models = new JSONArray();
    protected String type = "";

    private void fetchObjectsName() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.polyhaven.com/assets?t=%s" , type))).build();
        HttpResponse<String> responseBody = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        parseObjectsName(responseBody.body());
        fetched = true;
    }
    public JSONArray fetchObjects(int pageNumber) throws IOException, InterruptedException {
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
            HttpResponse<String> responseBody = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            parseModelObject(responseBody.body());
        }
        return models;
    }
    private void parseObjectsName(String responseBody){
        JSONObject allObjects = new JSONObject(responseBody);
        objectsName = allObjects.names();
    }
    protected void parseModelObject(String responseBody){
    }
}
