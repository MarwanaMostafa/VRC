package com.example.vrc.assets.services;

import com.example.vrc.assets.services.jsonParser.JsonParser;
import com.example.vrc.assets.services.objects.PolyHavenModelsObjectsList;
import com.example.vrc.assets.services.objects.PolyPizzaObjectsList;
import com.example.vrc.assets.services.singleObject.Object;
import com.example.vrc.assets.services.singleObject.PolyHavenModelObject;
import com.example.vrc.assets.services.singleObject.PolyPizzaModelObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ModelsAssetsService {
    private List<Object> modelsObjects = new ArrayList<>();
    private ConcurrentHashMap<String , List<Object>> filteredObjects = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String , String> lastQuery = new ConcurrentHashMap<>();
    private final JsonParser jsonParser = new JsonParser();

    public JSONObject fetchObjects(String userEmail , String query , int pageNumber , int pageSize) throws IOException, InterruptedException {
        if(modelsObjects.isEmpty()){
            fetchAllObjects();
        }
        filteredObjects.putIfAbsent(userEmail , new ArrayList<>(modelsObjects));
        filter(userEmail , query);
        int startIdx = (pageNumber - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, filteredObjects.get(userEmail).size());
        JSONArray models = new JSONArray();
        for(int i = startIdx ; i < endIdx ; i++){
            JSONObject curObj = new JSONObject();
            filteredObjects.get(userEmail).get(i).fetch();
            if(filteredObjects.get(userEmail).get(i).getUrl().isEmpty()||
                    filteredObjects.get(userEmail).get(i).getThumbnailUrl().isEmpty()||
                    filteredObjects.get(userEmail).get(i).getName().isEmpty()){
                endIdx = Math.min(endIdx + 1 , filteredObjects.get(userEmail).size());
                continue;
            }
            curObj.put("gltfUrl" , filteredObjects.get(userEmail).get(i).getUrl());
            curObj.put("thumbnailUrl" , filteredObjects.get(userEmail).get(i).getThumbnailUrl());
            curObj.put("name" , filteredObjects.get(userEmail).get(i).getName());
            curObj.put("id" , filteredObjects.get(userEmail).get(i).getId());
            models.put(curObj);
        }
        JSONObject returnedObject = new JSONObject();
        returnedObject.put("hasNext" , endIdx < filteredObjects.get(userEmail).size());
        returnedObject.put("models" , models);
        return returnedObject;


    }
    void fetchAllObjects() throws IOException, InterruptedException {
        List<String> polyPizzaIds;
        List<String> polyHavenNames;
        PolyHavenModelsObjectsList polyHavenModelsObjectsList = new PolyHavenModelsObjectsList();
        PolyPizzaObjectsList polyPizzaObjectsList = new PolyPizzaObjectsList();
        polyPizzaIds = polyPizzaObjectsList.getIds();
        polyHavenNames = polyHavenModelsObjectsList.getNames();
        for (String polyPizzaId : polyPizzaIds) {
            jsonParser.fetch(polyPizzaId);
            PolyPizzaModelObject polyPizzaModelObject = new PolyPizzaModelObject();
            polyPizzaModelObject.setId(polyPizzaId);
            polyPizzaModelObject.setName(jsonParser.getName());
            polyPizzaModelObject.setUrl(jsonParser.getUrl());
            polyPizzaModelObject.setThumbnailUrl(jsonParser.getThumbnailUrl());
            modelsObjects.add(polyPizzaModelObject);
        }
        for (String polyHavenName : polyHavenNames) {
            PolyHavenModelObject polyHavenModelObject = new PolyHavenModelObject();
            polyHavenModelObject.setName(polyHavenName);
            modelsObjects.add(polyHavenModelObject);
        }
        Collections.shuffle(modelsObjects);
    }
    void filter(String userEmail , String query){
        query = query.replace('-' , ' ');
        query = query.replace('_' , ' ');
        query = query.toLowerCase();
        query = " " + query + " ";
        if(lastQuery.containsKey(userEmail)){
            if(query.equals(lastQuery.get(userEmail))) return;
        }
        filteredObjects.get(userEmail).clear();
        lastQuery.remove(userEmail);
        lastQuery.put(userEmail , query);
        for (Object modelsObject : modelsObjects) {
            String cur = modelsObject.getName();
            cur = cur.replace('-' , ' ');
            cur = cur.replace('_' , ' ');
            cur = cur.toLowerCase();
            cur = " " + cur + " ";
            if (query.isBlank() || cur.contains(query)) {
                filteredObjects.get(userEmail).add(modelsObject);
            }
        }
    }



}
