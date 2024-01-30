package com.example.vrc.assets.services.objects;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@Component
public class PolyHavenObjectsList implements ObjectsList{
    private List<String> names = new ArrayList<>();
    protected String type;

    @Override
    public List<String> getNames()  throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://api.polyhaven.com/assets?t=%s" , type))).build();
        HttpResponse<String> responseBody = client
                .send(request, HttpResponse.BodyHandlers.ofString());
        parseObjectsName(responseBody.body());
        return names;
    }
    private void parseObjectsName(String responseBody){
        JSONObject allObjects = new JSONObject(responseBody);
        JSONArray objectsName = allObjects.names();
        for(int i = 0 ; i < objectsName.length() ; i++) {
            names.add(objectsName.getString(i));
        }
    }

}
