package com.example.vrc.assets.services;

import com.example.vrc.assets.services.jsonParser.JsonParser;
import com.example.vrc.assets.services.objects.PolyHavenModelsObjectsList;
import com.example.vrc.assets.services.objects.PolyPizzaObjectsList;
import com.example.vrc.assets.services.singleObject.Object;
import com.example.vrc.assets.services.singleObject.PolyHavenModelObject;
import com.example.vrc.assets.services.singleObject.PolyPizzaModelObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ModelsAssetsService {
    private List<Object> modelsObjects = new ArrayList<>();
    private List<Object> filterdObjects = new ArrayList<>();
    private String lastQuery = "";
    private final JsonParser jsonParser = new JsonParser();

    public JSONObject fetchObjects(String query , int pageNumber , int pageSize) throws IOException, InterruptedException {
        if(modelsObjects.isEmpty()){
            fetchAllObjects();
        }
        filter(query);
        int startIdx = (pageNumber - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, filterdObjects.size());
        JSONArray models = new JSONArray();
        for(int i = startIdx ; i < endIdx ; i++){
            JSONObject curObj = new JSONObject();
            filterdObjects.get(i).fetch();
            if(filterdObjects.get(i).getUrl().isEmpty()||
                    filterdObjects.get(i).getThumbnailUrl().isEmpty()||
                    filterdObjects.get(i).getName().isEmpty()){
                endIdx = Math.min(endIdx + 1 , filterdObjects.size());
                continue;
            }
            curObj.put("gltfUrl" , filterdObjects.get(i).getUrl());
            curObj.put("thumbnailUrl" , filterdObjects.get(i).getThumbnailUrl());
            curObj.put("name" , filterdObjects.get(i).getName());
            models.put(curObj);
        }
        JSONObject returnedObject = new JSONObject();
        returnedObject.put("hasNext" , endIdx < filterdObjects.size());
        returnedObject.put("models" , models);
        return returnedObject;


    }
    void fetchAllObjects() throws IOException, InterruptedException {
        List<String> polyPizzaIds = new ArrayList<>();
        List<String> polyHavenNames = new ArrayList<>();
        PolyHavenModelsObjectsList polyHavenModelsObjectsList = new PolyHavenModelsObjectsList();
        PolyPizzaObjectsList polyPizzaObjectsList = new PolyPizzaObjectsList();
        polyPizzaIds = polyPizzaObjectsList.getIds();
        polyHavenNames = polyHavenModelsObjectsList.getNames();
        for(int i = 0 ; i < polyPizzaIds.size() ; i++){
            jsonParser.fetch(polyPizzaIds.get(i));
            PolyPizzaModelObject polyPizzaModelObject = new PolyPizzaModelObject();
            polyPizzaModelObject.setId(polyPizzaIds.get(i));
            polyPizzaModelObject.setName(jsonParser.getName());
            polyPizzaModelObject.setUrl(jsonParser.getUrl());
            polyPizzaModelObject.setThumbnailUrl(jsonParser.getThumbnailUrl());
            modelsObjects.add(polyPizzaModelObject);
            filterdObjects.add(polyPizzaModelObject);
        }
        for(int i = 0 ; i < polyHavenNames.size() ; i++){
            PolyHavenModelObject polyHavenModelObject = new PolyHavenModelObject();
            polyHavenModelObject.setName(polyHavenNames.get(i));
            modelsObjects.add(polyHavenModelObject);
            filterdObjects.add(polyHavenModelObject);
        }
    }

    void filter(String query){
        query = query.replace('-' , '_');
        query = query.replace(' ' , '_');
        query = query.toLowerCase();
        if(query.equals(lastQuery)) return;
        filterdObjects.clear();
        for(int i = 0 ; i < modelsObjects.size() ; i++){
            String cur = modelsObjects.get(i).getName();
            if(cur.contains(query)){
                filterdObjects.add(modelsObjects.get(i));
            }
        }

    }

}
