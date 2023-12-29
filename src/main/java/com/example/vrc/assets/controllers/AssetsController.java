package com.example.vrc.assets.controllers;

import com.example.vrc.assets.services.HdrisAssetsService;
import com.example.vrc.assets.services.ModelsAssetsService;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("api/")
public class AssetsController {
    private final ModelsAssetsService modelsAssetsService;
    private final HdrisAssetsService hdrisAssetsService;
    @Autowired
    public AssetsController(ModelsAssetsService modelsAssetsService , HdrisAssetsService hdrisAssetsService){
        this.modelsAssetsService = modelsAssetsService;
        this.hdrisAssetsService = hdrisAssetsService;
    }
    @GetMapping("models/")
    public ResponseEntity<Object> getModelsObjects() throws IOException, InterruptedException{
        return getModelsObjects(1);
    }
    @GetMapping("models/{pageNumber}")
    @ApiOperation(value = "Get Models Objects", notes = "Retrieve a list of models objects.")
    public ResponseEntity<Object> getModelsObjects(@PathVariable int pageNumber) throws IOException, InterruptedException {
        JSONArray cur = modelsAssetsService.fetchObjects(pageNumber);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }
    @GetMapping("models/{query}/{pageNumber}")
    public ResponseEntity<Object> getModelsObjectsByName(@PathVariable String query , @PathVariable int pageNumber) throws IOException, InterruptedException {
        JSONArray cur = modelsAssetsService.fetchObjectsByName(query, pageNumber);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }
    @GetMapping("hdris/")
    public ResponseEntity<Object> getHdrisObjects() throws IOException, InterruptedException{
        return getHdrisObjects(1);
    }
    @GetMapping("hdris/{pageNumber}")
    @ApiOperation(value = "Get HDRIs Objects", notes = "Retrieve a list of HDRIs objects.")
    public ResponseEntity<Object> getHdrisObjects(@PathVariable int pageNumber) throws IOException, InterruptedException{
        JSONArray cur = hdrisAssetsService.fetchObjects(pageNumber);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }
    @GetMapping("hdris/{query}/{pageNumber}/")
    public ResponseEntity<Object> getHdrisObjectsByName(@PathVariable String query , @PathVariable int pageNumber) throws IOException, InterruptedException {
        JSONArray cur = hdrisAssetsService.fetchObjectsByName(query, pageNumber);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }

}
