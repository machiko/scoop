package com.tfidf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * <p>Title:</p>
 * <p>Description: 余弦獲取文章相似性
 * </p>
 * @createDate：2013-8-26
 * @author xq
 * @version 1.0
 */
public class CosineSimilarAlgorithm {

	/**
	 * 
	* @Title: cosSimilarityByFile
	* @Description: 獲取兩個文件相似性
	* @param @param firstFile
	* @param @param secondFile
	* @param @return    
	* @return Double   
	* @throws
	 */
	public static Double cosSimilarityByFile(String firstFile,String secondFile){
		try{
			Map<String, Map<String, Integer>> firstTfMap=TfIdfAlgorithm.wordSegCount(firstFile);
			Map<String, Map<String, Integer>> secondTfMap=TfIdfAlgorithm.wordSegCount(secondFile);
			if(firstTfMap==null || firstTfMap.size()==0){
				throw new IllegalArgumentException("firstFile not found or firstFile is empty! ");
			}
			if(secondTfMap==null || secondTfMap.size()==0){
				throw new IllegalArgumentException("secondFile not found or secondFile is empty! ");
			}
			Map<String,Integer> firstWords=firstTfMap.get(firstFile);
			Map<String,Integer> secondWords=secondTfMap.get(secondFile);
			if(firstWords.size()<secondWords.size()){
				Map<String, Integer> temp=firstWords;
				firstWords=secondWords;
				secondWords=temp;
			}
			return calculateCos((LinkedHashMap<String, Integer>)firstWords, (LinkedHashMap<String, Integer>)secondWords);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0d;
	}
	
	/**
	 * 
	* @Title: cosSimilarityByString
	* @Description: 得到兩個字串的相似性
	* @param @param first
	* @param @param second
	* @param @return    
	* @return Double   
	* @throws
	 */
	public static Double cosSimilarityByString(String first,String second){
		try{
			Map<String, Integer> firstTfMap=TfIdfAlgorithmCKIP.segStr(first);
			Map<String, Integer> secondTfMap=TfIdfAlgorithmCKIP.segStr(second);
			
			Map<String, int[]> vectorSpace = new HashMap<String, int[]>();
			int[] itemCountArray = null;
		
			for (Object key: firstTfMap.keySet()) {
				itemCountArray = new int[2];
				itemCountArray[0] = firstTfMap.get(key);
   	         	itemCountArray[1] = 0;
				vectorSpace.put((String) key, itemCountArray);
			}
			
			for (Object key: secondTfMap.keySet()) {
				if (vectorSpace.containsKey(key)) {
					vectorSpace.get(key)[1] = secondTfMap.get(key);
				} else {
					itemCountArray = new int[2];
					itemCountArray[0] = 0;
					itemCountArray[1] = secondTfMap.get(key);
					vectorSpace.put((String) key, itemCountArray);
				}
				
			}
			
			firstTfMap.clear();
			secondTfMap.clear();
			
			for (Object key: vectorSpace.keySet()) {
				firstTfMap.put((String) key, vectorSpace.get(key)[0]);
				secondTfMap.put((String) key, vectorSpace.get(key)[1]);
			}
//			if(firstTfMap.size()<secondTfMap.size()){
//				Map<String, Integer> temp=firstTfMap;
//				firstTfMap=secondTfMap;
//				secondTfMap=temp;
//			}
			
			return calculateCos((LinkedHashMap<String, Integer>)firstTfMap, (LinkedHashMap<String, Integer>)secondTfMap);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0d;
	}

	/**
	 * 
	* @Title: calculateCos
	* @Description: 計算余弦相似性
	* @param @param first
	* @param @param second
	* @param @return    
	* @return Double   
	* @throws
	 */
	private static Double calculateCos(LinkedHashMap<String, Integer> first,LinkedHashMap<String, Integer> second){
		
		List<Map.Entry<String, Integer>> firstList = new ArrayList<Map.Entry<String, Integer>>(first.entrySet());
		List<Map.Entry<String, Integer>> secondList = new ArrayList<Map.Entry<String, Integer>>(second.entrySet());
		
		
		//計算相似度  
        double vectorFirstModulo = 0.00;//向量1的模  
        double vectorSecondModulo = 0.00;//向量2的模  
        double vectorProduct = 0.00; //向量積  
        int secondSize=second.size();
		for(int i=0;i<firstList.size();i++){
			if(i<secondSize){
				vectorSecondModulo+=secondList.get(i).getValue().doubleValue()*secondList.get(i).getValue().doubleValue();
				vectorProduct+=firstList.get(i).getValue().doubleValue()*secondList.get(i).getValue().doubleValue();
			}
			vectorFirstModulo+=firstList.get(i).getValue().doubleValue()*firstList.get(i).getValue().doubleValue();
		}
	   return vectorProduct/(Math.sqrt(vectorFirstModulo)*Math.sqrt(vectorSecondModulo));
	}
	
	public static void main(String[] args){
		Double result=cosSimilarityByString("（中央社記者唐佩君台北30日電）行政院長毛治國今天宣布，近日降水使用水量多了約一週，再加上因應清明假期，因此3階限水延後一個星期至4月8日實施，但仍請大家盡量節約用水。",
				"趕在截止前最後一天，總統府30日召開國安高層會議，決定將由財政部擬定參與亞投行意向書，對於台灣確定申請加入亞投行，財政部長張盛和表示，我們有資金、技術，絕對是對國際社會有貢獻的，也對國家經濟發展有正面影響。");
		
//		Double result = cosSimilarityByFile("/Users/reyes/Downloads/cosine1/", "/Users/reyes/Downloads/cosine2/");
		System.out.println(result);
	}
}
