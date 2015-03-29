package com.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import com.opensymphony.xwork2.ActionSupport;

import tw.cheyingwu.ckip.CKIP;
import tw.cheyingwu.ckip.Term;
import tw.cheyingwu.ckip.WordSegmentationService;

public class SegTFIDF extends ActionSupport {
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
    	dataMap = new HashMap<String, Object>();
    	ArrayList<String> fileList = new ArrayList<String>();
    	ArrayList<String> tfidfList = new ArrayList<String>();
    	Map<String, Map<String, Double>> allTfMap=SegTFIDF.allTf("/Users/reyes/Downloads/tfidf");
    	Map<String, Double> idfMap=SegTFIDF.idf(allSegsMap);
    	
    	Map<String, Map<String, Double>> tfIdfMap=SegTFIDF.tfIdf(allTfMap, idfMap);
        Set<String> files=tfIdfMap.keySet();
        
        
        for(String filePath : files){
            Map<String, Double> tfIdf=tfIdfMap.get(filePath);
            Set<String> segs=tfIdf.keySet();
            
            // sort
            List<Map.Entry<String,Double>> list=new ArrayList<>();
            list.addAll(tfIdf.entrySet());
            ValueComparator vc = new ValueComparator();
            Collections.sort(list, vc);
//            Object[] objArray = list.toArray();

            System.out.println(list.toString());
            
            for(String word: segs){
//                System.out.println("fileName:"+filePath+"     word:"+word+"        tf-idf:"+tfIdf.get(word));
            }
            fileList.add(filePath);
            tfidfList.add(list.toString());
        }
        dataMap.put("filePath", fileList);
        dataMap.put("tfidf", tfidfList);
        
//        System.out.println("********** 使用中研院斷詞伺服器 *********");
//        WordSegmentationService c; //宣告一個class變數c
//        ArrayList<String> inputList = new ArrayList<String>(); //宣告動態陣列 存切詞的name
//        ArrayList<String> TagList = new ArrayList<String>();   //宣告動態陣列 存切詞的詞性
//         
//        
//        c = new CKIP( "140.109.19.104" , 1501, "reyes", "reyes123"); //輸入申請的IP、port、帳號、密碼
//        
//        c.setRawText(txt);
//        c.send(); //傳送至中研院斷詞系統服務使用
//        
//        for (Term t : c.getTerm()) {
//            
//            inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
//            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列
//        }
        
//        dataMap.put("word", inputList);
//        dataMap.put("speech", TagList);
        
        return dataMap;
    }
    
    /**
     * 檔案名保存在list
     */
    private static List<String> fileList = new ArrayList<String>(); 
    /**
     * 所有檔tf結果.key:檔案名,value:該文件tf
     */
    private static Map<String, Map<String, Double>> allTfMap = new HashMap<String, Map<String, Double>>();  
    
    /**
     * 所有檔分詞結果.key:檔案名,value:該檔分詞統計
     */
    private static Map<String, Map<String, Integer>> allSegsMap = new HashMap<String, Map<String, Integer>>(); 
    
    /**
     * 所有檔分詞的idf結果.key:檔案名,value:詞w在整個文檔集合中的逆向文檔頻率idf (Inverse Document Frequency)，即文檔總數n與詞w所出現檔數docs(w, D)比值的對數
     */
    private static Map<String, Double> idfMap = new HashMap<String, Double>();  
    
    /**
     * 統計包含單詞的文檔數  key:單詞  value:包含該詞的文檔數
     */
    private static Map<String, Integer> containWordOfAllDocNumberMap=new HashMap<String, Integer>();
    
    /**
     * 統計單詞的TF-IDF
     * key:檔案名 value:該文件tf-idf
     */
    private static Map<String, Map<String, Double>> tfIdfMap = new HashMap<String, Map<String, Double>>();
//    private static Map<String, Map<String, Double>> tfIdfMap = new TreeMap<String, Map<String, Double>>();
    
    
    /**
     * 
    * @Title: readDirs
    * @Description: 遞迴獲取文件
    * @param @param filepath
    * @param @return List<String>
    * @param @throws FileNotFoundException
    * @param @throws IOException    
    * @return List<String>   
    * @throws
     */
    private static List<String> readDirs(String filepath) throws FileNotFoundException, IOException {  
        try {  
            File file = new File(filepath);  
            if (!file.isDirectory()) {  
                System.out.println("輸入的參數應該為[資料夾名]");  
                System.out.println("filepath: " + file.getAbsolutePath());  
            } else if (file.isDirectory()) {  
                String[] filelist = file.list();  
                for (int i = 0; i < filelist.length; i++) {  
                    File readfile = new File(filepath + File.separator + filelist[i]);  
                    if (!readfile.isDirectory()) {  
                        fileList.add(readfile.getAbsolutePath());  
                    } else if (readfile.isDirectory()) {  
                        readDirs(filepath + File.separator + filelist[i]);  
                    }  
                }  
            }  
  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();
        }  
        return fileList;  
    }
    
    /**
     * 
    * @Title: readFile
    * @Description: 讀取文件轉化成string
    * @param @param file
    * @param @return String
    * @param @throws FileNotFoundException
    * @param @throws IOException    
    * @return String   
    * @throws
     */
    private static String readFile(String file) throws FileNotFoundException, IOException {  
        StringBuffer sb = new StringBuffer();  
        InputStreamReader is = new InputStreamReader(new FileInputStream(file), "UTF-8");  
        BufferedReader br = new BufferedReader(is);  
        String line = br.readLine();  
        while (line != null) {  
            sb.append(line).append("\r\n");  
            line = br.readLine();  
        }  
        br.close();  
        return sb.toString();  
    }  
    

    /**
     * 
    * @Title: segString
    * @Description: 用ik進行字串分詞,統計各個詞出現的次數
    * @param @param content
    * @param @return  Map<String, Integer>  
    * @return Map<String,Integer>   
    * @throws
     */
    private static Map<String, Integer> segString(String content){
        // 分詞
        Reader input = new StringReader(content);
        // 智慧分詞關閉（對分詞的精度影響很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Integer> words = new HashMap<String, Integer>();
        try {
            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return words;
    }
    
    /**
     * 
    * @Title: segCKIP
    * @Description: 用ckip進行字串分詞,統計各個詞出現的次數
    * @param @param content
    * @param @return  Map<String, Integer>  
    * @return Map<String,Integer>   
    * @throws
     */
    private static Map<String, Integer> segCKIP(String content){
        // 分詞
        Reader input = new StringReader(content);
        // 智慧分詞關閉（對分詞的精度影響很大）
//        IKSegmenter iks = new IKSegmenter(input, true);
//        Lexeme lexeme = null;
        Map<String, Integer> words = new HashMap<String, Integer>();
        
        WordSegmentationService c; //宣告一個class變數c
//        ArrayList<String> inputList = new ArrayList<String>(); //宣告動態陣列 存切詞的name
//        ArrayList<String> TagList = new ArrayList<String>();   //宣告動態陣列 存切詞的詞性
         
//        System.out.println("********** 使用中研院斷詞伺服器 *********");
         
        c = new CKIP( "140.109.19.104" , 1501, "reyes", "reyes123"); //輸入申請的IP、port、帳號、密碼
        
        c.setRawText(content);
        c.send(); //傳送至中研院斷詞系統服務使用
        
        for (Term t : c.getTerm()) {
            if (words.containsKey(t.getTerm())) {
            	words.put(t.getTerm(), words.get(t.getTerm()) + 1);
            } else {
            	words.put(t.getTerm(), 1);
            }
//            inputList.add(t.getTerm()); // t.getTerm()會讀到斷詞的String，將其存到inputList陣列
//            TagList.add(t.getTag());    // t.getTag() 會讀到斷詞的詞性，將其存到TagList陣列
        }
        
        return words;
    }
    
    
    /**
     * 
    * @Title: segStr
    * @Description: 返回LinkedHashMap的分詞
    * @param @param content
    * @param @return    
    * @return Map<String,Integer>   
    * @throws
     */
    public static Map<String, Integer> segStr(String content){
        // 分詞
        Reader input = new StringReader(content);
        // 智慧分詞關閉（對分詞的精度影響很大）
        IKSegmenter iks = new IKSegmenter(input, true);
        Lexeme lexeme = null;
        Map<String, Integer> words = new LinkedHashMap<String, Integer>();
        try {
            while ((lexeme = iks.next()) != null) {
                if (words.containsKey(lexeme.getLexemeText())) {
                    words.put(lexeme.getLexemeText(), words.get(lexeme.getLexemeText()) + 1);
                } else {
                    words.put(lexeme.getLexemeText(), 1);
                }
            }
        }catch(IOException e) {
            e.printStackTrace();
        }
        return words;
    }
    
    public static Map<String, Integer> getMostFrequentWords(int num,Map<String, Integer> words){
        
        Map<String, Integer> keywords = new LinkedHashMap<String, Integer>();
        int count=0;
        // 詞頻統計
        List<Map.Entry<String, Integer>> info = new ArrayList<Map.Entry<String, Integer>>(words.entrySet());
        Collections.sort(info, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
                return obj2.getValue() - obj1.getValue();
            }
        });
        
        // 高頻詞輸出
        for (int j = 0; j < info.size(); j++) {
            // 詞-->頻
            if(info.get(j).getKey().length()>1){
                if(num>count){
                    keywords.put(info.get(j).getKey(), info.get(j).getValue());
                    count++;
                }else{
                    break;
                }
            }
        }
        return keywords;
    }
    
    /**
     * 
    * @Title: tf
    * @Description: 分詞結果轉化為tf,公式為:tf(w,d) = count(w, d) / size(d)
    * 即詞w在文檔d中出現次數count(w, d)和文檔d中總詞數size(d)的比值
    * @param @param segWordsResult
    * @param @return    
    * @return HashMap<String,Double>   
    * @throws
     */
    private static HashMap<String, Double> tf(Map<String, Integer> segWordsResult) { 
        
        HashMap<String, Double> tf = new HashMap<String, Double>();// 正規化  
        if(segWordsResult==null || segWordsResult.size()==0){
            return tf;
        }
        Double size=Double.valueOf(segWordsResult.size());
        Set<String> keys=segWordsResult.keySet();
        for(String key: keys){
            Integer value=segWordsResult.get(key);
            tf.put(key, Double.valueOf(value)/size);
        }
        return tf;  
    }  
    
    /**
     * 
    * @Title: allTf
    * @Description: 得到所有檔的tf
    * @param @param dir
    * @param @return Map<String, Map<String, Double>>
    * @return Map<String,Map<String,Double>>   
    * @throws
     */
    public static Map<String, Map<String, Double>> allTf(String dir){
        try{
            fileList=readDirs(dir);
            for(String filePath : fileList){
                String content=readFile(filePath);
//                Map<String, Integer> segs=segString(content);
                Map<String, Integer> segs=segCKIP(content);
                allSegsMap.put(filePath, segs);
                allTfMap.put(filePath, tf(segs));
            }
        }catch(FileNotFoundException ffe){
            ffe.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return allTfMap;
    }
    
    /**
     * 
    * @Title: wordSegCount
    * @Description: 返回分詞結果,以LinkedHashMap保存
    * @param @param dir
    * @param @return    
    * @return Map<String,Map<String,Integer>>   
    * @throws
     */
    public static Map<String, Map<String, Integer>> wordSegCount(String dir){
        try{
            fileList=readDirs(dir);
            for(String filePath : fileList){
                String content=readFile(filePath);
//                Map<String, Integer> segs=segStr(content);
                Map<String, Integer> segs=segCKIP(content);
                allSegsMap.put(filePath, segs);
            }
        }catch(FileNotFoundException ffe){
            ffe.printStackTrace();
        }catch(IOException io){
            io.printStackTrace();
        }
        return allSegsMap;
    }
    
    
    /**
     * 
    * @Title: containWordOfAllDocNumber
    * @Description: 統計包含單詞的文檔數  key:單詞  value:包含該詞的文檔數
    * @param @param allSegsMap
    * @param @return    
    * @return Map<String,Integer>   
    * @throws
     */
    private static Map<String, Integer> containWordOfAllDocNumber(Map<String, Map<String, Integer>> allSegsMap){
        if(allSegsMap==null || allSegsMap.size()==0){
            return containWordOfAllDocNumberMap;
        }
        
        Set<String> fileList=allSegsMap.keySet();
        for(String filePath: fileList){
            Map<String, Integer> fileSegs=allSegsMap.get(filePath);
            //獲取該檔分詞為空或為0,進行下一個檔
            if(fileSegs==null || fileSegs.size()==0){
                continue;
            }
            //統計每個分詞的idf
            Set<String> segs=fileSegs.keySet();
            for(String seg : segs){
                if (containWordOfAllDocNumberMap.containsKey(seg)) {
                    containWordOfAllDocNumberMap.put(seg, containWordOfAllDocNumberMap.get(seg) + 1);
                } else {
                    containWordOfAllDocNumberMap.put(seg, 1);
                }
            }
            
        }
        return containWordOfAllDocNumberMap;
    }
    
    /**
     * 
    * @Title: idf
    * @Description: idf = log(n / docs(w, D)) 
    * @param @param containWordOfAllDocNumberMap
    * @param @return Map<String, Double> 
    * @return Map<String,Double>   
    * @throws
     */
    public static Map<String, Double> idf(Map<String, Map<String, Integer>> allSegsMap){
        if(allSegsMap==null || allSegsMap.size()==0){
            return idfMap;
        }
        containWordOfAllDocNumberMap=containWordOfAllDocNumber(allSegsMap);
        Set<String> words=containWordOfAllDocNumberMap.keySet();
        Double wordSize=Double.valueOf(containWordOfAllDocNumberMap.size());
        for(String word: words){
            Double number=Double.valueOf(containWordOfAllDocNumberMap.get(word));
            idfMap.put(word, Math.log(wordSize/(number+1.0d)));
        }
        return idfMap;
    }
    
    /**
     * 
    * @Title: tfIdf
    * @Description: tf-idf
    * @param @param tf,idf
    * @return Map<String, Map<String, Double>>   
    * @throws
     */
    public static Map<String, Map<String, Double>> tfIdf(Map<String, Map<String, Double>> allTfMap,Map<String, Double> idf){
        
        Set<String> fileList=allTfMap.keySet();
        for(String filePath : fileList){
            Map<String, Double> tfMap=allTfMap.get(filePath);
            Map<String, Double> docTfIdf=new HashMap<String,Double>();
            Set<String> words=tfMap.keySet();
            for(String word: words){
                Double tfValue=Double.valueOf(tfMap.get(word));
                Double idfValue=idf.get(word);
                docTfIdf.put(word, tfValue*idfValue);
            }
            
            tfIdfMap.put(filePath, docTfIdf);
        }
        return tfIdfMap;
    }

}

//sort
class ValueComparator implements Comparator<Map.Entry<String, Double>>  
{  
  public int compare(Map.Entry<String, Double> mp1, Map.Entry<String, Double> mp2)   
  {  
      return (int)((mp2.getValue() - mp1.getValue()) * Math.pow(10, 17));
  }  
}  
