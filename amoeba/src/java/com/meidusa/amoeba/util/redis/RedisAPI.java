package com.meidusa.amoeba.util.redis;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import com.meidusa.amoeba.data.DbServerInfo;
import com.meidusa.amoeba.data.UserDbserver;

public class RedisAPI {

	private final static Logger logger = Logger.getLogger(RedisAPI.class);

	private final static int DBSERVER_INFO_INDEX = 11;
	private final static int USER_DBSERVER_INDEX = 12;

	private final static String DBSERVERINFO_IPADDR="ipAddr";
	private final static String DBSERVERINFO_PORT="port";
	private final static String DBSERVERINFO_DBUSER="dbUser";
	private final static String DBSERVERINFO_DBPWD="dbPassword";
	private final static String DBSERVERINFO_SCHEMA="schema";
	private final static String DBSERVERINFO_PARENT="parent";
	private final static String DBSERVERINFO_USERCOUNT="userCount";
	private final static String DBSERVERINFO_MAXCOUNT="maxCount";
	
	private final static String USERDBSERVER_DBSERVER="dbServer";
	
	public static void setDbserverInfo(DbServerInfo dbServerInfo) {
		Jedis jedis = null;
		if(null == dbServerInfo){
			throw new IllegalArgumentException("数据库对象为空");
		}
		try {
			jedis = RedisUtils.getResource();
			jedis.select(DBSERVER_INFO_INDEX);
			Pipeline pipeline = jedis.pipelined();
            //存储于redis
            String dbserver=dbServerInfo.getDbserver();
        	if(null!=dbServerInfo.getIpAddr()){
        		pipeline.hset(dbserver, DBSERVERINFO_IPADDR, dbServerInfo.getIpAddr());
        	}
        	if(null!=dbServerInfo.getPort()){
        		pipeline.hset(dbserver, DBSERVERINFO_PORT, dbServerInfo.getPort().toString());
        	}
        	if(null!=dbServerInfo.getDbUser()){
        		pipeline.hset(dbserver, DBSERVERINFO_DBUSER, dbServerInfo.getDbUser());
        	}
        	if(null!=dbServerInfo.getDbPassword()){
        		pipeline.hset(dbserver, DBSERVERINFO_DBPWD, dbServerInfo.getDbPassword());
        	}
        	if(null!=dbServerInfo.getSchema()){
        		pipeline.hset(dbserver, DBSERVERINFO_SCHEMA, dbServerInfo.getSchema());
        	}
        	if(null!=dbServerInfo.getParent()){
        		pipeline.hset(dbserver, DBSERVERINFO_PARENT, dbServerInfo.getParent());
        	}
        	if(null!=dbServerInfo.getUserCount()){
        		pipeline.hset(dbserver, DBSERVERINFO_USERCOUNT, dbServerInfo.getUserCount().toString());
        	}
        	if(null!=dbServerInfo.getMaxCount()){
        		pipeline.hset(dbserver, DBSERVERINFO_MAXCOUNT, dbServerInfo.getMaxCount().toString());
        	}
            pipeline.sync();
			
		} catch (Exception e) {
			logger.error("数据库信息写入redis失败，server="+dbServerInfo.getDbserver(),e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
	}
	
	public static void batchSetDbserverInfo(List<DbServerInfo> dbserverInfoList){
		Jedis jedis = null;
		if(null == dbserverInfoList){
			throw new IllegalArgumentException("数据库对象数组为空");
		}
		try {
			jedis = RedisUtils.getResource();
			jedis.select(DBSERVER_INFO_INDEX);
			Pipeline pipeline = jedis.pipelined();
            //存储于redis
			for(DbServerInfo dbServerInfo:dbserverInfoList){
				String dbserver=dbServerInfo.getDbserver();
	        	if(null!=dbServerInfo.getIpAddr()){
	        		pipeline.hset(dbserver, DBSERVERINFO_IPADDR, dbServerInfo.getIpAddr());
	        	}
	        	if(null!=dbServerInfo.getPort()){
	        		pipeline.hset(dbserver, DBSERVERINFO_PORT, dbServerInfo.getPort().toString());
	        	}
	        	if(null!=dbServerInfo.getDbUser()){
	        		pipeline.hset(dbserver, DBSERVERINFO_DBUSER, dbServerInfo.getDbUser());
	        	}
	        	if(null!=dbServerInfo.getDbPassword()){
	        		pipeline.hset(dbserver, DBSERVERINFO_DBPWD, dbServerInfo.getDbPassword());
	        	}
	        	if(null!=dbServerInfo.getSchema()){
	        		pipeline.hset(dbserver, DBSERVERINFO_SCHEMA, dbServerInfo.getSchema());
	        	}
	        	if(null!=dbServerInfo.getParent()){
	        		pipeline.hset(dbserver, DBSERVERINFO_PARENT, dbServerInfo.getParent());
	        	}
	        	if(null!=dbServerInfo.getUserCount()){
	        		pipeline.hset(dbserver, DBSERVERINFO_USERCOUNT, dbServerInfo.getUserCount().toString());
	        	}
	        	if(null!=dbServerInfo.getMaxCount()){
	        		pipeline.hset(dbserver, DBSERVERINFO_MAXCOUNT, dbServerInfo.getMaxCount().toString());
	        	}

			}
            pipeline.sync();
		} catch (Exception e) {
			logger.error("数据库信息写入redis失败",e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
	}
	
	public static DbServerInfo getDbServerInfo(String dbserver){
		Jedis jedis = null;
		if(null == dbserver){
			throw new IllegalArgumentException("传入的数据库名称参数为空");
		}
		
		try {
			jedis = RedisUtils.getResource();
			jedis.select(DBSERVER_INFO_INDEX);
			if(!jedis.exists(dbserver)){
				return null;
			}
            DbServerInfo dbServerInfo = new DbServerInfo();
            dbServerInfo.setDbserver(dbserver);
            
            String ipAddr = jedis.hget(dbserver, DBSERVERINFO_IPADDR);
        	if(null!=ipAddr){
        		dbServerInfo.setIpAddr(ipAddr);
        	}
        	String port = jedis.hget(dbserver, DBSERVERINFO_PORT);
        	if(null!=port && port.length()>0){
        		dbServerInfo.setPort(Integer.valueOf(port));
        	}
        	String dbUser = jedis.hget(dbserver, DBSERVERINFO_DBUSER);
        	if(null!=dbUser){
        		dbServerInfo.setDbUser(dbUser);
        	}
        	String dbPassword = jedis.hget(dbserver, DBSERVERINFO_DBPWD);
        	if(null!=dbPassword){
        		dbServerInfo.setDbPassword(dbPassword);
        	}
        	String schema = jedis.hget(dbserver, DBSERVERINFO_SCHEMA);
        	if(null!=schema){
        		dbServerInfo.setSchema(schema);
        	}
        	String parent = jedis.hget(dbserver, DBSERVERINFO_PARENT);
        	if(null!=parent){
        		dbServerInfo.setParent(parent);
        	}
        	String userCount = jedis.hget(dbserver, DBSERVERINFO_USERCOUNT);
        	if(null!=userCount && userCount.length()>0){
        		dbServerInfo.setUserCount(Integer.valueOf(userCount));
        	}
        	String maxCount = jedis.hget(dbserver, DBSERVERINFO_MAXCOUNT);
        	if(null!=maxCount && maxCount.length()>0){
        		dbServerInfo.setMaxCount(Integer.valueOf(maxCount));
        	}
            return dbServerInfo;
			
		} catch (Exception e) {
			logger.error("redis读取数据库信息异常",e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
		return null;
	}
	
	public static void setUserDbserver(UserDbserver userDbserver){
		Jedis jedis = null;
		if(null == userDbserver){
			throw new IllegalArgumentException("用户数据库对照关系对象为空");
		}
		try {
			jedis = RedisUtils.getResource();
			jedis.select(USER_DBSERVER_INDEX);
			Pipeline pipeline = jedis.pipelined();
            //存储于redis
			String userId=userDbserver.getUserId().toString();
        	if(null!=userDbserver.getDbserver()){
        		pipeline.hset(userId, USERDBSERVER_DBSERVER, userDbserver.getDbserver());
        	}
        	if(null!=userDbserver.getIpAddr()){
        		pipeline.hset(userId, DBSERVERINFO_IPADDR, userDbserver.getIpAddr());
        	}
        	if(null!=userDbserver.getPort()){
        		pipeline.hset(userId, DBSERVERINFO_PORT, userDbserver.getPort().toString());
        	}
        	if(null!=userDbserver.getDbUser()){
        		pipeline.hset(userId, DBSERVERINFO_DBUSER, userDbserver.getDbUser());
        	}
        	if(null!=userDbserver.getDbPassword()){
        		pipeline.hset(userId, DBSERVERINFO_DBPWD, userDbserver.getDbPassword());
        	}
        	if(null!=userDbserver.getSchema()){
        		pipeline.hset(userId, DBSERVERINFO_SCHEMA, userDbserver.getSchema());
        	}
        	if(null!=userDbserver.getParent()){
        		pipeline.hset(userId, DBSERVERINFO_PARENT, userDbserver.getParent());
        	}
        	if(null!=userDbserver.getUserCount()){
        		pipeline.hset(userId, DBSERVERINFO_USERCOUNT, userDbserver.getUserCount().toString());
        	}
        	if(null!=userDbserver.getMaxCount()){
        		pipeline.hset(userId, DBSERVERINFO_MAXCOUNT, userDbserver.getMaxCount().toString());
        	}
	        	
            pipeline.sync();
		} catch (Exception e) {
			logger.error("用户数据库对照关系信息写入redis失败",e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
	}
	
	public static void batchSetUserDbserver(List<UserDbserver> userDbservers){
		Jedis jedis = null;
		if(null == userDbservers){
			throw new IllegalArgumentException("用户数据库对照关系对象数组为空");
		}
		try {
			jedis = RedisUtils.getResource();
			jedis.select(USER_DBSERVER_INDEX);
			Pipeline pipeline = jedis.pipelined();
            //存储于redis
			for(UserDbserver userDbserver:userDbservers){
				String userId=userDbserver.getUserId().toString();
	        	if(null!=userDbserver.getDbserver()){
	        		pipeline.hset(userId, USERDBSERVER_DBSERVER, userDbserver.getDbserver());
	        	}
	        	if(null!=userDbserver.getIpAddr()){
	        		pipeline.hset(userId, DBSERVERINFO_IPADDR, userDbserver.getIpAddr());
	        	}
	        	if(null!=userDbserver.getPort()){
	        		pipeline.hset(userId, DBSERVERINFO_PORT, userDbserver.getPort().toString());
	        	}
	        	if(null!=userDbserver.getDbUser()){
	        		pipeline.hset(userId, DBSERVERINFO_DBUSER, userDbserver.getDbUser());
	        	}
	        	if(null!=userDbserver.getDbPassword()){
	        		pipeline.hset(userId, DBSERVERINFO_DBPWD, userDbserver.getDbPassword());
	        	}
	        	if(null!=userDbserver.getSchema()){
	        		pipeline.hset(userId, DBSERVERINFO_SCHEMA, userDbserver.getSchema());
	        	}
	        	if(null!=userDbserver.getParent()){
	        		pipeline.hset(userId, DBSERVERINFO_PARENT, userDbserver.getParent());
	        	}
	        	if(null!=userDbserver.getUserCount()){
	        		pipeline.hset(userId, DBSERVERINFO_USERCOUNT, userDbserver.getUserCount().toString());
	        	}
	        	if(null!=userDbserver.getMaxCount()){
	        		pipeline.hset(userId, DBSERVERINFO_MAXCOUNT, userDbserver.getMaxCount().toString());
	        	}
	        	
			}
            pipeline.sync();
		} catch (Exception e) {
			logger.error("用户数据库对照关系信息写入redis失败",e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
	}
	
	public static UserDbserver getUserDbserver(Long userId){
		Jedis jedis = null;
		if(null == userId){
			throw new IllegalArgumentException("用户id为空");
		}
		
		try {
			jedis = RedisUtils.getResource();
			jedis.select(USER_DBSERVER_INDEX);
			String userId_str=userId.toString();
			if(!jedis.exists(userId_str)){
            	return null;
            }
			
			
			
			/*String dbserver = jedis.hget(userId_str, USERDBSERVER_DBSERVER);
			if(null != dbserver && dbserver.length()>0){
				userDbserver.setDbserver(dbserver);
			}*/
			UserDbserver userDbserver = new UserDbserver();
			Map<String, String> userDbserverMap = jedis.hgetAll(userId_str);
			if(userDbserverMap.containsKey(USERDBSERVER_DBSERVER)){
				userDbserver.setDbserver(userDbserverMap.get(USERDBSERVER_DBSERVER));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_IPADDR)){
				userDbserver.setIpAddr(userDbserverMap.get(DBSERVERINFO_IPADDR));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_PORT) && userDbserverMap.get(DBSERVERINFO_PORT).length()>0){
				userDbserver.setPort(Integer.valueOf(userDbserverMap.get(DBSERVERINFO_PORT)));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_DBUSER)){
				userDbserver.setDbUser(userDbserverMap.get(DBSERVERINFO_DBUSER));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_DBPWD)){
				userDbserver.setDbPassword(userDbserverMap.get(DBSERVERINFO_DBPWD));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_SCHEMA)){
				userDbserver.setSchema(userDbserverMap.get(DBSERVERINFO_SCHEMA));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_PARENT)){
				userDbserver.setParent(userDbserverMap.get(DBSERVERINFO_PARENT));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_USERCOUNT) && userDbserverMap.get(DBSERVERINFO_USERCOUNT).length()>0){
				userDbserver.setUserCount(Integer.valueOf(userDbserverMap.get(DBSERVERINFO_USERCOUNT)));
			}
			if(userDbserverMap.containsKey(DBSERVERINFO_MAXCOUNT) && userDbserverMap.get(DBSERVERINFO_MAXCOUNT).length()>0){
				userDbserver.setUserCount(Integer.valueOf(userDbserverMap.get(DBSERVERINFO_MAXCOUNT)));
			}
			
			return userDbserver;
		} catch (Exception e) {
			logger.error("redis获取用户和数据库对照关系时异常失败",e);
		}finally{
			RedisUtils.returnResource(jedis);
		}
		
		return null;
	}
}
