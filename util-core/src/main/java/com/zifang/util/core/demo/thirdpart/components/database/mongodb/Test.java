package com.zifang.util.core.demo.thirdpart.components.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.zifang.util.core.demo.thirdpart.jar.json.GsonUtil;
import org.bson.Document;

import java.util.ArrayList;

public class Test {
	
	public static void main(String[] args) {
		ArrayList<Object> recommandationList = new ArrayList<>();
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		// get the document of mongodb
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		
		FindIterable<Document> iter = collection.find().skip(0).limit(1);
		for (MongoCursor<Document> doc = iter.iterator(); doc.hasNext();) {
			Document s = doc.next();
			s.replace("_id", s.get("_id").toString());
			recommandationList.add(s);
		}
		System.out.println(GsonUtil.objectToJsonStr(recommandationList));
	}
	
	

}
