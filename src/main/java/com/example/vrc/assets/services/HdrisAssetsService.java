package com.example.vrc.assets.services;

import com.example.vrc.assets.services.objects.PolyHavenHdrisObjectsList;
import com.example.vrc.assets.services.singleObject.PolyHavenHdriObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class HdrisAssetsService {
    private List<String> hdrisObjectsNames = new ArrayList<>();
    private List<String> filterdNames = new ArrayList<>();
    private String lastQuery = "";

    public JSONObject fetchObjects(String query , int pageNumber , int pageSize) throws IOException, InterruptedException {
        if(hdrisObjectsNames.isEmpty()){
            PolyHavenHdrisObjectsList polyHavenHdrisObjectsList = new PolyHavenHdrisObjectsList();
            hdrisObjectsNames = polyHavenHdrisObjectsList.getNames();
            for(int i = 0 ; i < hdrisObjectsNames.size() ; i++){
                filterdNames.add(hdrisObjectsNames.get(i));
            }
        }
        filter(query);
        int startIdx = (pageNumber - 1) * pageSize;
        int endIdx = Math.min(startIdx + pageSize, filterdNames.size());
        JSONArray models = new JSONArray();
        for(int i = startIdx ; i < endIdx ; i++){
            PolyHavenHdriObject polyHavenHdriObject = new PolyHavenHdriObject();
            polyHavenHdriObject.setName(filterdNames.get(i));
            polyHavenHdriObject.fetch();
            JSONObject curObj = new JSONObject();
            if(polyHavenHdriObject.getUrl().isEmpty() ||
                    polyHavenHdriObject.getThumbnailUrl().isEmpty() ||
                    polyHavenHdriObject.getName().isEmpty()){
                endIdx = Math.min(endIdx + 1 , filterdNames.size());
                continue;
            }
            curObj.put("hdriUrl" , polyHavenHdriObject.getUrl());
            curObj.put("thumbnailUrl" , polyHavenHdriObject.getThumbnailUrl());
            curObj.put("name" , polyHavenHdriObject.getName());
            models.put(curObj);
        }
        JSONObject returnedObject = new JSONObject();
        returnedObject.put("hasNext" , endIdx < filterdNames.size());
        returnedObject.put("models" , models);
        return returnedObject;
    }

    void filter(String query){
        query = query.replace('-' , '_');
        query = query.replace(' ' , '_');
        query = query.toLowerCase();
        if(query.equals(lastQuery)) return;
        filterdNames.clear();
        lastQuery = query;
        for(int i = 0 ; i < hdrisObjectsNames.size() ; i++){
            System.out.println(i);
            String curName = hdrisObjectsNames.get(i);
            if(curName.contains(query)){
                filterdNames.add(hdrisObjectsNames.get(i));
            }
        }
    }

}
