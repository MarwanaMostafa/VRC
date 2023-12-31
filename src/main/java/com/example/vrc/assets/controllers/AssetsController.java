package com.example.vrc.assets.controllers;

import com.example.vrc.assets.services.HdrisAssetsService;
import com.example.vrc.assets.services.ModelsAssetsService;
import io.swagger.annotations.ApiOperation;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @ApiOperation(value = "Get Models Objects", notes = "Retrieve a list of models objects.")
    public ResponseEntity<Object> getModelsObjects(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize
    ) throws IOException, InterruptedException {
        JSONArray cur = modelsAssetsService.fetchObjects(query, pageNumber, pageSize);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }
    @GetMapping("hdris/")
    @ApiOperation(value = "Get HDRIs Objects", notes = "Retrieve a list of HDRIs objects.")
    public ResponseEntity<Object> getHdrisObjects(
            @RequestParam(name = "q") String query,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize
    ) throws IOException, InterruptedException{
        JSONArray cur = hdrisAssetsService.fetchObjects(query, pageNumber, pageSize);
        return new ResponseEntity<>(cur.toList(), HttpStatus.OK);
    }
}
