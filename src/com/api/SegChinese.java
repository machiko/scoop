package com.api;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;
import com.opensymphony.xwork2.ActionSupport;

public class SegChinese extends ActionSupport {
    // init variable
    protected Dictionary dic;
    private Map<String,Object> dataMap;
    private String content;
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public Map<String,Object> getDataMap() {
        return dataMap;
    }
    
    // 回傳斷詞結果 json
    public String loadJsonFromMap() throws IOException{
        //dataMap中的數據將會被Struts2轉換成JSON字符串，所以這裡要先清空其中的數​​據
        SegChinese seg = new SegChinese();
        dataMap = new HashMap<String, Object>();
        dataMap.put("mmseg", seg.run(content));
        return SUCCESS;
    }
    
    
    public SegChinese() {
        System.setProperty("mmseg.dic.path", "./src/com/segchinese/data");  //這裡可以指定自訂詞庫
        dic = Dictionary.getInstance();
    }

    protected Seg getSeg() {
        return new ComplexSeg(dic);
    }
    
    public ArrayList<String> segWords(String txt) throws IOException {
        Reader input = new StringReader(txt);
        ArrayList<String> list = new ArrayList<String>();
        Seg seg = getSeg();
        MMSeg mmSeg = new MMSeg(input, seg);
        Word word = null;
        
        while ((word = mmSeg.next()) != null) {
            String w = word.getString();
            list.add(w);      
        }
        return list;
    }
    
    protected ArrayList<String> run(String txt) throws IOException {
        ArrayList<String> segWrods = segWords(txt);
        return segWrods;
    }
}
