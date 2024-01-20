package com.example.vrc.assets.services;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Asset {
    protected static String curModelName;
    private boolean fetched = false;
    private int numberOfModelsPerPage = 10;
    private JSONArray objectsName;
    private JSONArray searchedObjectsName = new JSONArray();
    protected JSONArray models = new JSONArray();
    private String lastSearchedQuery = "";
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
    public JSONObject putObjectsInModels(int pageNumber , JSONArray names) throws IOException, InterruptedException {
        models = new JSONArray();
        JSONObject returnObject = new JSONObject();
        int startIdx = (pageNumber - 1) * numberOfModelsPerPage;
        int endIdx = Math.min(startIdx + numberOfModelsPerPage, names.length());
        HttpClient client = HttpClient.newHttpClient();
        for(int i = startIdx ; i < endIdx ; i++){
            curModelName = names.get(i).toString();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(String.format("https://api.polyhaven.com/files/%s" ,curModelName)))
                    .build();
            HttpResponse<String> responseBody = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
            parseModelObject(responseBody.body());
        }
        returnObject.put("hasNext" , endIdx < names.length());
        returnObject.put("models" , models);
        return returnObject;
    }
    public JSONObject fetchObjects(String query , int pageNumber , int pageSize) throws IOException, InterruptedException {
        numberOfModelsPerPage = pageSize;
        if(!fetched){
            fetchObjectsName();
        }
        if(!query.isEmpty()){
            filterObjectsByName(query);
            return putObjectsInModels(pageNumber , searchedObjectsName);
        }
        return putObjectsInModels(pageNumber , objectsName);
    }
    private void parseObjectsName(String responseBody){
        JSONObject allObjects = new JSONObject(responseBody);
        objectsName = allObjects.names();
    }
    private void filterObjectsByName(String query) throws IOException, InterruptedException {
        query = query.replace('-' , '_');
        query = query.replace(' ' , '_');
        query = query.toLowerCase();
        if(query.equals(lastSearchedQuery)) return;
        lastSearchedQuery = query;
        if(!fetched){
            fetchObjectsName();
        }
        searchedObjectsName = new JSONArray();
        for(int i = 0 ; i < objectsName.length() ; i++){
            String currentName = objectsName.get(i).toString();
            if(currentName.contains(query)){
                searchedObjectsName.put(currentName);
            }
        }
    }
    protected void parseModelObject(String responseBody){
    }
}
