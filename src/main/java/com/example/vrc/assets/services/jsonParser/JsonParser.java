package com.example.vrc.assets.services.jsonParser;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;

@Component
public class JsonParser {
    private JSONArray models = new JSONArray();
    private String name;
    private String id;
    private String url;
    private String thumbnailUrl;
    public JsonParser(){
        JSONParser jsonParser = new JSONParser();
        try(FileReader reader = new FileReader("polyPizzaObjects.json")){
            Object obj = jsonParser.parse(reader);
            parseModelList(obj);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
    void parseModelList(Object modelsList) throws ParseException {
        String s = modelsList.toString();
        JSONObject jsonObj = new JSONObject(s);
        JSONArray models = jsonObj.getJSONArray("models");
        this.models = models;
    }
    public void fetch(String id){
        this.id = id;
        for(int i = 0 ; i < models.length() ; i++){
            JSONObject curObj = models.getJSONObject(i);

            if(curObj.get("id").equals(id)){
                this.name = curObj.getString("name");
                this.url = curObj.getString("glfUrl");
                this.thumbnailUrl = curObj.getString("thumbnailUrl");
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

}
