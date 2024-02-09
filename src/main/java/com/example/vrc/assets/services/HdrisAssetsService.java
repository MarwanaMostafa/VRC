package com.example.vrc.assets.services;

import com.example.vrc.assets.services.objects.PolyHavenHdrisObjectsList;
import com.example.vrc.assets.services.singleObject.PolyHavenHdriObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class HdrisAssetsService {
    private List<String> hdrisObjectsNames = new ArrayList<>();
    private ConcurrentHashMap<String , List<String>> filteredNames = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String , String> lastQuery = new ConcurrentHashMap<>();

    public JSONObject fetchObjects(String userEmail,String query , int pageNumber , int pageSize) throws IOException, InterruptedException {
        if(hdrisObjectsNames.isEmpty()){
            PolyHavenHdrisObjectsList polyHavenHdrisObjectsList = new PolyHavenHdrisObjectsList();
            hdrisObjectsNames = polyHavenHdrisObjectsList.getNames();
        }
        filteredNames.putIfAbsent(userEmail , new ArrayList<>(hdrisObjectsNames));
        filter(userEmail , query);
        int startIdx = (pageNumber - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, filteredNames.get(userEmail).size());
        JSONArray models = new JSONArray();
        for(int i = startIdx ; i < endIdx ; i++){
            PolyHavenHdriObject polyHavenHdriObject = new PolyHavenHdriObject();
            polyHavenHdriObject.setName(filteredNames.get(userEmail).get(i));
            polyHavenHdriObject.fetch();
            JSONObject curObj = new JSONObject();
            if(polyHavenHdriObject.getUrl().isEmpty() ||
                    polyHavenHdriObject.getThumbnailUrl().isEmpty() ||
                    polyHavenHdriObject.getName().isEmpty()){
                endIdx = Math.min(endIdx + 1 , filteredNames.get(userEmail).size());
                continue;
            }
            curObj.put("hdriUrl" , polyHavenHdriObject.getUrl());
            curObj.put("thumbnailUrl" , polyHavenHdriObject.getThumbnailUrl());
            curObj.put("name" , polyHavenHdriObject.getName());
            curObj.put("id" , polyHavenHdriObject.getId());
            models.put(curObj);
        }
        JSONObject returnedObject = new JSONObject();
        returnedObject.put("hasNext" , endIdx < filteredNames.get(userEmail).size());
        returnedObject.put("models" , models);
        return returnedObject;
    }

    void filter(String userEmail , String query){
        query = query.replace('-' , ' ');
        query = query.replace('_' , ' ');
        query = query.toLowerCase();
        query = " " + query + " ";
        if(lastQuery.containsKey(userEmail)){
            if(query.equals(lastQuery.get(userEmail))) return;
        }
        filteredNames.get(userEmail).clear();
        lastQuery.remove(userEmail);
        lastQuery.put(userEmail , query);
        for(int i = 0 ; i < hdrisObjectsNames.size() ; i++){
            String cur = hdrisObjectsNames.get(i);
            cur = cur.replace('-' , ' ');
            cur = cur.replace('_' , ' ');
            cur = cur.toLowerCase();
            cur = " " + cur + " ";
            if(query.isBlank() || cur.contains(query)){
                filteredNames.get(userEmail).add(hdrisObjectsNames.get(i));
            }
        }
    }

}
