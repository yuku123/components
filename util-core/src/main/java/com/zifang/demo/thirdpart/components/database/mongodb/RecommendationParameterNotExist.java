package com.zifang.demo.thirdpart.components.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Random;

public class RecommendationParameterNotExist {
	
	private static Random random = new Random();


	public ArrayList<Object> getRecommendationList() {
		ArrayList<Object> recommandationList = new ArrayList<>();
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		// get the document of mongodb
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.SEARCH);
		
		FindIterable<Document> iter = collection.find().skip(random.nextInt(10000)).limit(20);
		for (MongoCursor<Document> doc = iter.iterator(); doc.hasNext();) {
			Document document = doc.next();
			document.replace("_id", document.get("_id").toString());
			recommandationList.add(document);
		}
		return recommandationList;
	}

}
