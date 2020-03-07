package com.zifang.util.zex.demo.thirdpart.components.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.*;
import java.util.*;

public class JedisRepository {

	private static JedisRepository jedisRepository;
	private static JedisPool writePool = null;
	private static JedisPool readPool = null;
	private static Jedis jedis = null;
	
	private JedisRepository() {
		final String hostName = "192.168.1.104";//PidayConfig.getProperty("app.redis.hostname");
		final Integer port = Integer.valueOf("6379");//PidayConfig.getProperty("app.redis.port"));
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		//poolConfig.setMaxTotal(300);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		poolConfig.setMaxIdle(5);
		poolConfig.setMinIdle(1);
		poolConfig.setTestWhileIdle(true);
		poolConfig.setNumTestsPerEvictionRun(10);
		poolConfig.setTimeBetweenEvictionRunsMillis(60000);
		poolConfig.setMaxWaitMillis(3000);
		poolConfig.setMaxIdle(300);
//		poolConfig.setMaxActive(300);
//		poolConfig.setMaxWait(3000);
		writePool = new JedisPool(poolConfig, hostName, port, 20000);
		readPool = new JedisPool(poolConfig,  hostName, port, 20000);
		
		// check if localhost has redis slave
		// point pool to master if slave fails
		try {
			jedis = readPool.getResource();
		}
		catch(Exception e) {
			readPool = writePool;
		}
	}
	
	public static JedisRepository getInstance() {
		if (jedisRepository == null) {
			synchronized (JedisRepository.class) {
				if (jedisRepository == null) {
					jedisRepository = new JedisRepository();
				}
			}
		}
		return jedisRepository;
	}

	/**
	 * Inserts an object into redis
	 * 
	 * @param collection
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	public void put(String collection, String key, Object value) throws Exception {
		byte[] serializedCollectionName = serialize(collection);
		byte[] serializedKey = serialize(key);
		byte[] serializedValue = serialize(value);

		Map<String, String> map = new HashMap<String, String>();
		map.put(key, String.valueOf(value));
		try {
			jedis = writePool.getResource();
			String result = jedis.hmset(collection,map);
			System.out.println(result);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				writePool.returnResource(jedis);
			}
			
		}
	}

	/**
	 * Inserts an object into redis
	 * 
	 * @param key
	 * @param value
	 * @throws Exception 
	 */
	public void put(String key, Object value) throws Exception {
		byte[] serializedKey = serialize(key);
		byte[] serializedValue = serialize(value);

		try {
			jedis = writePool.getResource();
			jedis.set(serializedKey, serializedValue);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				writePool.returnResource(jedis);
			}
		}
	}

	public void remove(String key) throws Exception {
		byte[] serializedKey = serialize(key);

		try {
			jedis = writePool.getResource();
			jedis.del(serializedKey);
		} catch (Exception ex) {
			throw ex;
		} finally {
			writePool.returnResource(jedis);
		}
	}

	
	/**
	 * returns keys under specified collection for regex mentioned from redis
	 * 
	 * @param collection
	 * @param pattern
	 * @return
	 * @throws Exception 
	 */
	public List<String> keys(String collection, String pattern) throws Exception {
		byte[] serializedCollectionName = serialize(collection);
		Set<byte[]> serializedKeys = null;
		List<String> keys = new ArrayList<String>();
		try {
			jedis = readPool.getResource();
			serializedKeys = jedis.hkeys(serializedCollectionName);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}

		for (byte[] serializedKey : serializedKeys) {
			if (deserialize(serializedKey).toString().matches(pattern)) {
				keys.add(deserialize(serializedKey).toString());
			}
		}
		return keys;
	}

	/**
	 * returns keys under default collection for regex mentioned from redis
	 * 
	 * @param pattern
	 * @return
	 * @throws Exception 
	 */
	public List<String> keys(String pattern) throws Exception {
		byte[] serializedPattern = serialize(pattern);
		Set<byte[]> serializedKeys = null;
		List<String> keys = new ArrayList<String>();
		try {
			jedis = readPool.getResource();
			serializedKeys = jedis.keys(serializedPattern);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}
		for (byte[] serializedKey : serializedKeys) {
			keys.add(deserialize(serializedKey).toString());
		}
		return keys;
	}

	/**
	 * Insert an object into redis and specify its expiry
	 * 
	 * @param collection
	 * @param key
	 * @param value
	 * @param expiry
	 * @throws Exception 
	 */
	public void put(String collection, String key, Object value, int expiry) throws Exception {
		byte[] serializedCollectionName = serialize(collection);
		byte[] serializedKey = serialize(key);
		byte[] serializedValue = serialize(value);

		Map<byte[], byte[]> map = new HashMap<byte[], byte[]>();
		map.put(serializedKey, serializedValue);

		try {
			jedis = writePool.getResource();
			if (expiry != 0) {
				jedis.hset(serializedCollectionName, serializedKey, serializedValue);
				jedis.expire(serializedCollectionName, expiry);
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				writePool.returnResource(jedis);
			}
		}
	}

	/**
	 * returns an object from redis store
	 * 
	 * @param collection
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Object get(String collection, String key) throws Exception {
		byte[] serializedCollectionName = serialize(collection);
		byte[] serializedKey = serialize(key);
		Object returnValue = null;

		try {
			jedis = readPool.getResource();
			returnValue = jedis.hget(collection, key);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}
		return returnValue;
	}

	/**
	 * returns an object from redis store
	 * 
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Object get(String key) throws Exception {
		byte[] serializedKey = serialize(key);
		Object returnValue = null;

		try {
			jedis = readPool.getResource();
			returnValue = deserialize(jedis.get(serializedKey));
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}
		return returnValue;
	}

	/**
	 * deletes an object from redis store
	 * 
	 * @param collection
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Object remove(String collection, String key) throws Exception {
		byte[] serializedCollectionName = serialize(collection);
		byte[] serializedKey = serialize(key);
		Object value = get(collection, key);

		try {
			jedis = writePool.getResource();
			jedis.del(serializedCollectionName, serializedKey);
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}

		return value;
	}

	/**
	 * checks if a key/value pair is present in redis
	 * 
	 * @param collection
	 * @param key
	 * @return
	 * @throws Exception 
	 */
	public Boolean containsKey(String collection, String key) throws Exception {
		return get(collection, key) != null;
	}

	/**
	 * checks if a collection is empty
	 * 
	 * @param collection
	 * @return
	 * @throws Exception 
	 */
	public Boolean isEmpty(String collection) throws Exception {
		Boolean returnValue = null;

		try {
			jedis = readPool.getResource();
			returnValue = jedis.exists(serialize(collection));
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}
		return returnValue;
	}

	/**
	 * returns size of collection
	 * 
	 * @param collection
	 * @return
	 * @throws Exception 
	 */
	public Long size(String collection) throws Exception {
		Long returnValue = null;

		try {
			jedis = readPool.getResource();
			returnValue = jedis.hlen(serialize(collection));
		} catch (Exception ex) {
			throw ex;
		} finally {
			if(jedis != null){
				readPool.returnResource(jedis);
			}
		}
		return returnValue;
	}

	/**
	 * serializes an object
	 * 
	 * @param obj
	 * @return
	 * @throws IOException
	 */
	public byte[] serialize(Object obj) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(out);
		if (obj == null) {
			return null;
		}
		os.writeObject(obj);
		return out.toByteArray();
	}

	/**
	 * deserializes an object
	 * 
	 * @param data
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
		if (data == null) {
			return null;
		}
		ByteArrayInputStream in = new ByteArrayInputStream(data);
		ObjectInputStream is = new ObjectInputStream(in);
		return is.readObject();
	}
	public static void main(String[] args) {
		JedisRepository jedisRepository = JedisRepository.getInstance();
		try {
//			jedisRepository.get("defaultOwnersMetadata","55894");
			//jedisRepository.put("jiangnan","test103","admin");
			System.out.println(jedisRepository.get("jiangnan","test103"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
