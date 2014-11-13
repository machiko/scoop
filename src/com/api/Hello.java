package com.api;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class Hello extends ActionSupport{
    private Map<String, Object> dataMap;
    
    public String loadJsonFromMap() {
        dataMap = new HashMap<String, Object>();
        dataMap.put("message", "mapJson");
        return SUCCESS;
    }
    
    public Map<String, Object> getDataMap() {
        return dataMap;
    }
}
