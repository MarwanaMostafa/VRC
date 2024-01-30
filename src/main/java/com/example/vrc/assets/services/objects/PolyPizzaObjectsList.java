package com.example.vrc.assets.services.objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PolyPizzaObjectsList implements ObjectsList {
    private List<String> names = new ArrayList<>();
    private List<String> ids = new ArrayList<>();
    private Set<String> idSet = new HashSet<String>();

    public PolyPizzaObjectsList(){
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader("polyPizzaObjects.json")){
            Object obj = jsonParser.parse(reader);
            parseModelList(obj);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    public List<String> getNames(){
        return names;
    }
    public List<String> getIds(){
        return ids;
    }
    void parseModelList(Object modelsList) throws ParseException {
        String s = modelsList.toString();
        JSONObject jsonObj = new JSONObject(s);
        JSONArray models = jsonObj.getJSONArray("models");
        for(int i = 0 ; i < models.length() ; i++){
            JSONObject curObj = models.getJSONObject(i);
            if(!idSet.contains(curObj.getString("id"))){
                names.add(curObj.getString("name"));
                ids.add(curObj.getString("id"));
                idSet.add(curObj.getString("id"));

            }
        }
    }

}
