package com.zifang.util.db.database.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.zifang.util.core.demo.thirdpart.jar.json.GsonUtil;
import org.bson.Document;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class RecommendationParameterExist {

	private String sku;

	public void setSku(String sku) {
		this.sku = sku;
	}

	public ArrayList<Object> getRecommendationList() {
		// result list
		ArrayList<Object> recommandationList = new ArrayList<>();
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase(MongoConfig.DB_FUNSOLE);
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		// get sku list from other list
		ArrayList<String> skuList = getSkuList();
		recommandationList = getRecommendationByResearch();
		return recommandationList;

	}

	private ArrayList<Object> getRecommendationByResearch() {
		ArrayList arrayList = new ArrayList<>();
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		FindIterable<Document> iter = collection.find(new Document("sku", sku));
		Document document = iter.first();
		if(document == null) {
			return arrayList;
		} else {
			Object catagoryObject = document.get("category");
			String category = "";
			if(catagoryObject == null) {
				category = "null";
			}else {
				if(catagoryObject instanceof List) {
					for(String string : (List<String>) catagoryObject) {
						category = category+string+",";
					}
				}else if(catagoryObject instanceof String){
					category = (String)catagoryObject;
				}
			}
			category = category.replace("[", "").replace("]", "").replace("\"", "");
			//category = "Air Jordan";
			arrayList = getList(category);
		}
		return arrayList;
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
		FindIterable<Document> iter = collection.find(query).limit(20);
		for (Iterator<Document> doc = iter.iterator(); doc.hasNext();) {
			//System.out.println(aIterator.next());
			//recommandationList.add(aIterator.next());
			Document document = doc.next();
			document.replace("_id", document.get("_id").toString());
			recommandationList.add(document);
		}
		return recommandationList;
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
	

	public ArrayList<String> getSkuList() {
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.RECOMMANDATION);
		FindIterable<Document> iter = collection.find(new Document("key", sku));
		ArrayList<String> skuList = new ArrayList<>();
		for (MongoCursor<Document> doc = iter.iterator(); doc.hasNext();) {
			List<Object> aList = (List<Object>) doc.next().get("value");
			for (int i = 0; i < aList.size(); i++) {
				if (!skuList.contains(aList.get(i).toString())) {
					skuList.add(aList.get(i).toString());
					System.out.println(aList.get(i).toString());
				}
			}
		}
		return skuList;
	}

	public static void main(String[] args) throws IOException {
		RecommendationParameterExist recommendationParameterExist = new RecommendationParameterExist();
		recommendationParameterExist.setSku("308497-406");
		BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("t.txt"));
		try {
			bufferedWriter.write(GsonUtil.objectToJsonStr(recommendationParameterExist.getRecommendationList()));
			bufferedWriter.flush();
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
