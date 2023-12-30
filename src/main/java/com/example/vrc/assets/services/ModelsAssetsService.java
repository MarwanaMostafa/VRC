package com.example.vrc.assets.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ModelsAssetsService extends Asset{

    public ModelsAssetsService(){
        type = "models";
    }

    @Override
    protected void parseModelObject(String responseBody){
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
    }
}
