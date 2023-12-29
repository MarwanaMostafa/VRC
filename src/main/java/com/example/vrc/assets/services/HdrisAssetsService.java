package com.example.vrc.assets.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Service
public class HdrisAssetsService extends Asset{

    public HdrisAssetsService(){
        type = "hdris";
    }

    @Override
    protected void parseModelObject(String responseBody){
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
        models.put(modifiedModelObject);
    }
}
