package com.zifang.util.core.demo.thirdpart.components.database.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongoConnection {

	public static Map<String, MongoClient> mongoClientMap = new HashMap<String, MongoClient>();

	public static MongoClient getMongoClientInstance(String ip, Integer port,String user,String defultdb,String password) {//用户名 数据库名称 密码
		String key = ip + port;
		if (mongoClientMap.containsKey(key)) {
			return mongoClientMap.get(key);
		} else {
			MongoClient mongoClient = createMondoClient(ip,  port, user, defultdb, password);
			mongoClientMap.put(key, mongoClient);
			return mongoClient;
		}
	}
	
	public static MongoClient geDefultMongoClientInstance() {
		String ip = MongoConfig.IP;
		Integer port = MongoConfig.PORT;
		String user = MongoConfig.USER;
		String defultdb = MongoConfig.DEFULT_DB;
		String password = MongoConfig.PASSWORD;
		String key = ip + port;
		if (mongoClientMap.containsKey(key)) {
			return mongoClientMap.get(key);
		} else {
			MongoClient mongoClient = createMondoClient(ip,  port, user, defultdb, password);
			mongoClientMap.put(key, mongoClient);
			return mongoClient;
		}
	}

	private static MongoClient createMondoClient(String ip, Integer port, String user, String defultdb, String password) {
		ServerAddress serverAddress = new ServerAddress(ip,port);
		List<ServerAddress> addrs = new ArrayList<ServerAddress>();
		addrs.add(serverAddress);
		MongoCredential credential = MongoCredential.createScramSha1Credential(user,defultdb,password.toCharArray());
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		credentials.add(credential);
		MongoClient mongoClient = new MongoClient(addrs, credentials);
		System.out.println("Connect to database successfully");
		return mongoClient;
	}
	
	public  static MongoClient mongodbTest (String ip,int port,String user,String password,String dbname){
		  MongoDatabase db=null;
		  MongoClient client=null;
		  try{
		     MongoClientOptions.Builder build = new MongoClientOptions.Builder();  
		        //与数据最大连接数50  
		        build.connectionsPerHost(50);  
		        //如果当前所有的connection都在使用中，则每个connection上可以有50个线程排队等待  
		        build.threadsAllowedToBlockForConnectionMultiplier(50);  
		        build.connectTimeout(1*60*1000);  
		        build.maxWaitTime(2*60*1000);  
		        MongoClientOptions options = build.build();	
                       //设置服务器信息	        
		        ServerAddress serverAddress = new ServerAddress(ip, port);  
	                List<ServerAddress> seeds = new ArrayList<ServerAddress>();  
	                seeds.add(serverAddress);  
                       //设置验证信息
	                MongoCredential credentials = MongoCredential.createScramSha1Credential(user, dbname,  
	            		password.toCharArray());  
	                List<MongoCredential> credentialsList = new ArrayList<MongoCredential>();  
	                credentialsList.add(credentials); 	            
		        client =new MongoClient(seeds, credentialsList,options);  
		        return client;
		       
		  }catch (Exception e) {  
			  	e.printStackTrace();	            
		  }
			return client;                
	}
	
	
	public static void main(String[] args) {
		// 连接到 mongodb 服务
		MongoClient mongoClient = MongoConnection.geDefultMongoClientInstance();
		//MongoClient mongoClient = mongodbTest("dds-bp170f103d9169d4-pub.mongodb.rds.aliyuncs.com", 3717,"root", "funsole_411282999","funsole");

		// 连接到数据库
		MongoDatabase mongoDatabase = mongoClient.getDatabase("funsole");
		// get the document of mongodb
		MongoCollection<Document> collection = mongoDatabase.getCollection(MongoConfig.RECOMMANDATION);
		FindIterable<Document> iter = collection.find(new Document("key", "649541-001"));
		ArrayList<String> skuList = new ArrayList<>();
		for (MongoCursor<Document> doc = iter.iterator(); doc.hasNext();) {
			List aList = (List) doc.next().get("value");
			for (int i = 0; i < aList.size(); i++) {
				skuList.add(aList.get(i).toString());
				System.out.println(aList.get(i).toString());
			}
		}
		System.out.println(skuList);
		//return skuList;
	}

}
