package com.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;
import tw.cheyingwu.ckip.WordSegmentationService;

public class SegCkip extends ActionSupport {
    private Map<String, Object> dataMap;
    private String content;
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Map<String,Object> getDataMap() {
        return dataMap;
    }
    
    public String loadJsonFromMap() {
        Map<String, Object> segWords = segWords(content);
        dataMap = new HashMap<String, Object>();
        dataMap = segWords;
//        dataMap.put("test", segWords);
        return SUCCESS;
    }
    
    public Map<String, Object> segWords(String txt) {
        WordSegmentationService c; //宣告一個class變數c
        ArrayList<String> inputList = new ArrayList<String>(); //宣告動態陣列 存切詞的name
        ArrayList<String> TagList = new ArrayList<String>();   //宣告動態陣列 存切詞的詞性
//        txt = "早安，台新金控12月3日將召開股東臨時會進行董監改選";
         
//        System.out.println("********** 使用中研院斷詞伺服器 *********");
         
        c = new CKIP( "140.109.19.104" , 1501, "reyes", "reyes123"); //輸入申請的IP、port、帳號、密碼
        
        c.setRawText(txt);
        c.send(); //傳送至中研院斷詞系統服務使用
        
        for (Term t : c.getTerm()) {
            
            inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列
        }
        
        dataMap = new HashMap<String, Object>();
        dataMap.put("word", inputList);
        dataMap.put("speech", TagList);
        
        return dataMap;
    }

}
