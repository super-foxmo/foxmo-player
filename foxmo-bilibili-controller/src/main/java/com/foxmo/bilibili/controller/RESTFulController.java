package com.foxmo.bilibili.controller;

import org.apache.ibatis.annotations.Delete;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
public class RESTFulController {
    private Map<Integer,Map<String,Object>> dataMap;

    public RESTFulController(){
        dataMap = new HashMap<>();
        for (int i = 1;i <= 5;i++){
            HashMap<String, Object> map = new HashMap<>();
            map.put("id",i);
            map.put("name","name" + i);
            dataMap.put(i,map);
        }
    }

    @GetMapping("/restful-test/{id}")
    public Map<String,Object> gatData(@PathVariable("id") Integer id){
        Map<String, Object> map = dataMap.get(id);
        return map;
    }

    @DeleteMapping("/restful-test/{id}")
    public String deleteData(@PathVariable("id") Integer id){
        dataMap.remove(id);
        return "delete success";
    }

    @PostMapping("//*restful-test*/")
    public String postData(@RequestBody Map<String,Object> map){
        Integer[] idArray = map.keySet().toArray(new Integer[0]);
        Arrays.sort(idArray);
        Integer nextId = idArray[idArray.length - 1] + 1;
        dataMap.put(nextId,map);
        return "post success";
    }

    @PutMapping("/restful-test")
    public String putData(@RequestBody Map<String,Object> map){
        Integer id = Integer.valueOf(String.valueOf(map.get("id")));
        Map<String, Object> data = dataMap.get(id);
        if (data == null){
            Integer[] idArray = map.keySet().toArray(new Integer[0]);
            Arrays.sort(idArray);
            Integer nextId = idArray[idArray.length - 1] + 1;
            dataMap.put(nextId,map);
        }else{
            dataMap.put(id,map);
        }
        return "put success";
    }
}
