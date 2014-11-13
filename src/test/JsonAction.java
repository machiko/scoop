package test;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class JsonAction extends ActionSupport {
    private static final long serialVersionUID = 1L;
    private Map<String,Object> dataMap;
    private String jsonStr;
    public String loadJson(){
        jsonStr = "{message:\"json\"}";
        return SUCCESS;
    }
    public String loadJsonFromMap(){
        //dataMap中的数据将会被Struts2转换成JSON字符串，所以这里要先清空其中的数据
        dataMap = new HashMap<String, Object>();
        dataMap.put("message", "mapJson");
        return SUCCESS;
    }
    
    public Map<String,Object> getDataMap() {
        return dataMap;
    }
    
    public void setDataMap(Map<String,Object> dataMap) {
        this.dataMap = dataMap;
    }
    public String getJsonStr() {
        return jsonStr;
    }
    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}
