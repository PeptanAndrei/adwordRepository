package com.example.reclame.rest;

import com.example.reclame.dto.CostDto;
import com.example.reclame.model.AdModel;
import com.example.reclame.service.AdService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
@CrossOrigin("*")
@RestController
public class AdsController {

    private final AdService adService ;

    @Autowired
    public AdsController(AdService adService) {
        this.adService = adService;
    }


    @PostMapping("/api/processData")
    @ResponseBody
    public CostDto returnAdModel(@RequestBody String JsonList ) {
        //map json
        ObjectMapper mapper = new ObjectMapper();
        List<AdModel> list = null;
        try {
            list = Arrays.asList(mapper.readValue(JsonList, AdModel[].class));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return adService.processData(list);
    }


}




