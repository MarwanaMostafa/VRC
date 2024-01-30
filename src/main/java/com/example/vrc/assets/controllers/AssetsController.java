package com.example.vrc.assets.controllers;

import com.example.vrc.assets.services.HdrisAssetsService;
import com.example.vrc.assets.services.ModelsAssetsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "AddToken")
public class AssetsController {
    private final ModelsAssetsService modelsAssetsService;
    private final HdrisAssetsService hdrisAssetsService;
    @Autowired
    public AssetsController(ModelsAssetsService modelsAssetsService , HdrisAssetsService hdrisAssetsService){
        this.modelsAssetsService = modelsAssetsService;
        this.hdrisAssetsService = hdrisAssetsService;
    }
    @GetMapping("/models")
    @ApiOperation(value = "Get Models Objects", notes = "Retrieve a list of models objects.")
    public ResponseEntity<Object> getModelsObjects(
            Authentication auth,
            @RequestParam(name = "q") String query,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize
    ) throws IOException, InterruptedException {
        String userEmail = auth.getName();
        JSONObject cur = modelsAssetsService.fetchObjects(userEmail ,query, pageNumber, pageSize);
        return new ResponseEntity<>(cur.toMap(), HttpStatus.OK);
    }
    @GetMapping("/hdris")
    @ApiOperation(value = "Get HDRIs Objects", notes = "Retrieve a list of HDRIs objects.")
    public ResponseEntity<Object> getHdrisObjects(
            Authentication auth,
            @RequestParam(name = "q") String query,
            @RequestParam(name = "pageNumber") int pageNumber,
            @RequestParam(name = "pageSize") int pageSize
    ) throws IOException, InterruptedException{
        String userEmail = auth.getName();
        JSONObject cur = hdrisAssetsService.fetchObjects(userEmail , query, pageNumber, pageSize);
        return new ResponseEntity<>(cur.toMap(), HttpStatus.OK);
    }
}
