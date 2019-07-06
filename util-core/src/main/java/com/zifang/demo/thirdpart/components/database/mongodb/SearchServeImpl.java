package com.zifang.demo.thirdpart.components.database.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.zifang.demo.thirdpart.jar.json.GsonUtil;
import org.bson.Document;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Pattern;


@Service
public class SearchServeImpl {

	public Object getSearchList(HttpServletRequest httpServletRequest) {
		//define the result class
		Result result = new Result();
		// get the parameter from request
		String kw = httpServletRequest.getParameter("kw");
		String pageIndex = httpServletRequest.getParameter("pageIndex");
		String pageSize = httpServletRequest.getParameter("pageSize");
		// dealing with the parameter
		kw = kw==null?"":kw;
		kw = HardCode.getAJ(kw);
		pageIndex = isBiggerThanZeroInteger(pageIndex) ? pageIndex : "1";
		pageSize = isBiggerThanZeroInteger(pageSize) ? pageSize : "10";
		ArrayList arrayListAsSku = getKWListAsSku(kw);
		if(arrayListAsSku.size()!= 0) {
			result.setList(arrayListAsSku);
			result.setPageCount(1);
		}else {
			if("".equals(kw)) {
				ArrayList arrayList = new ArrayList<>();
				MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
				MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
				MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
				int count = (int) collection.countDocuments();
				Integer pageIndex_int =  Integer.parseInt(pageIndex);
				Integer pageSize_int = Integer.parseInt(pageSize);
				int pageCount = count/pageSize_int;
				int addition = count%pageSize_int==0?0:1;
				pageCount = pageCount + addition;
				pageIndex_int = pageIndex_int >= pageCount?pageCount:pageIndex_int;
				
				FindIterable<Document> iter = collection.find().skip((pageIndex_int-1)*pageSize_int).limit(pageSize_int);
				for (Iterator<Document> doc = iter.iterator(); doc.hasNext();) {
					//System.out.println(aIterator.next());
					Document document = doc.next();
					document.replace("_id", document.get("_id").toString());
					arrayList.add(document);
				}
				result.setList(arrayList);
				result.setPageCount(pageCount);
				System.out.println(collection.countDocuments());
			}else if(!"".equals(kw)) {
				ArrayList arrayList = getList(kw);
				//count the page count
				int length = arrayList.size();
				int pageCount = length/Integer.parseInt(pageSize);
				int addition = length%Integer.parseInt(pageSize)==0?0:1;
				pageCount = pageCount + addition;
				//subList the ArrayList
				Integer pageIndex_int =  Integer.parseInt(pageIndex);
				Integer pageSize_int = Integer.parseInt(pageSize);
				//if request include the like -6,give the defult number
				pageIndex_int = pageIndex_int >=1?pageIndex_int:1;
				pageSize_int = pageSize_int >=1?pageSize_int:10;
				//if user choose out of choose ARRAY then show the last page
				pageIndex_int = pageIndex_int >= pageCount?pageCount:pageIndex_int;
				List sublist = null;

				if( pageSize_int*pageIndex_int <= length  && length!= 0 ) {
					sublist = arrayList.subList((pageIndex_int-1)*pageSize_int, pageSize_int*pageIndex_int);
				} else if(pageSize_int*pageIndex_int > length && length!= 0){
					sublist = arrayList.subList((pageIndex_int-1)*pageSize_int, length);
				} else {
					sublist = new ArrayList<>();
				}
				result.setList(sublist);
				result.setPageCount(pageCount);
			}
		}
		
		
		return GsonUtil.objectToJsonStr(result);
	}
	
	private ArrayList getKWListAsSku(String kw) {
		ArrayList arrayList = new ArrayList<>();
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		
		FindIterable<Document> iter = collection.find(new Document("sku",kw));
		for (Iterator<Document> doc = iter.iterator(); doc.hasNext();) {
			//System.out.println(aIterator.next());
			Document document = doc.next();
			document.replace("_id", document.get("_id").toString());
			arrayList.add(document);
		}
		return arrayList;
	}

	public static boolean isBiggerThanZeroInteger(String str) {
		try {
			Integer aInteger = Integer.parseInt(str);
			if(aInteger<=0) {
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
	        return false;
		}
  }
	

	public ArrayList getList(String kw) {
		String compile = getCompilePattern(kw);
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		// get the document of mongodb
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		ArrayList recommandationList = new ArrayList<>();//    aa bb
		BasicDBObject query = new BasicDBObject();
		Pattern pattern = Pattern.compile(compile, Pattern.CASE_INSENSITIVE);
		query.put("search_key", pattern);// key为表字段名
		FindIterable<Document> iter = collection.find(query);
		for (Iterator<Document> doc = iter.iterator(); doc.hasNext();) {
			//System.out.println(aIterator.next());
			//recommandationList.add(aIterator.next());
			Document document = doc.next();
			String search_key = document.getString("search_key");
			search_key = search_key.replace("'", "").replace("[", "").replace("]", "").toLowerCase();
			//sSystem.out.println(search_key);
			if(isNeed(kw,search_key)) {
				document.replace("_id", document.get("_id").toString());
				recommandationList.add(document);
			}
			
		}
		return recommandationList;
	}
	
	public static void main(String[] args) {
		SearchServeImpl searchServeImpl = new SearchServeImpl();
		ArrayList arrayList = searchServeImpl.getList("air jordan 1");
		System.out.println(GsonUtil.objectToJsonStr(arrayList));
	}

	private boolean isNeed(String kw, String search_key) {
		boolean flag = true;
		Set<String> set = new HashSet<>();
		String tobeSpite = kw.trim().replace(",", " ").replace("-", " ");
		String[] toLoop = tobeSpite.split(" ");
		String[] search_array = search_key.split(" ");
		for(String se:search_array) {
			set.add(se.toLowerCase());
		}
		for(String to:toLoop) {
			if(!set.contains(to.toLowerCase())) {
				flag = false;
				break;
			}
		}
		return flag;
	}

	/**
	 * 1. trim
	 * 2. replace "," "-" to be ""
	 * 3. filter the split part of ""
	 * 4. transform the data to be pattern
	 * */
	public static String getCompilePattern(String kw) {
		String pattern = null;
		String tobeSpite = kw.trim().replace(",", " ").replace("-", " ");
		String[] tobe = tobeSpite.split(" ");
		for(int i = 0;i<tobe.length;i++) {
			if(!tobe[i].equals("")) {
				if(pattern == null) {
					pattern = "^(?=.*?"+tobe[i]+")";
				}else {
					pattern = pattern + "(?=.*?"+tobe[i]+")";
				}
			}
		}
		pattern = pattern +".+$";
		if(pattern == null) {
			pattern = "^.*$";
		}
		return pattern;
	}


}
