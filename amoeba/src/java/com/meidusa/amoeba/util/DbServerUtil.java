/**
 * 
 */
package com.meidusa.amoeba.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.config.BeanObjectEntityConfig;
import com.meidusa.amoeba.config.DBServerConfig;
import com.meidusa.amoeba.context.ProxyRuntimeContext;

/**
 * @author CZX
 * @date 2015-7-10
 */
public class DbServerUtil {
	private static Logger logger = Logger.getLogger(DbServerUtil.class);
	
	public static boolean isExists(String serverName){
		return ProxyRuntimeContext.getInstance().getPoolMap().containsKey(serverName);
	}
	
	public static boolean createDbServer(String serverName,String ipAddr,int port,String userName,String password,String schema,String parent){
		DBServerConfig dbServerConfig = new DBServerConfig();
		dbServerConfig.setAbstractive(false);
		dbServerConfig.setName(serverName);
		dbServerConfig.setParent(parent);
		BeanObjectEntityConfig entityConfig = new BeanObjectEntityConfig();
		if(!StringUtil.isEmpty(ipAddr)){
			entityConfig.getParams().put("ipAddress", ipAddr);
		}
		if(port>0){
			entityConfig.getParams().put("port", port);
		}
		if(!StringUtil.isEmpty(userName)){
			entityConfig.getParams().put("user", userName);
		}
		if(!StringUtil.isEmpty(password)){
			entityConfig.getParams().put("password", password);
		}
		if(!StringUtil.isEmpty(schema)){
			entityConfig.getParams().put("schema", schema);
		}
		dbServerConfig.setFactoryConfig(entityConfig);
		try {
			ProxyRuntimeContext.getInstance().createDbServer(dbServerConfig);
			return true;
		} catch (Exception e) {
			logger.error("create dbserver error", e);
			return false;
		}
		
	}
	
	/**选择一个合适的数据库服务器
	 * @return 数据库服务器名称
	 * @author CZX
	 * @date 2015-7-15
	 */
	public static String selectDbserver(){
		Map<String,Double> dbServerUsage = ProxyRuntimeContext.getInstance().getDbServerUsage();
		if(ProxyRuntimeContext.getInstance().getDbServerUsage()!=null){
			Iterator<String> itr = dbServerUsage.keySet().iterator();
			Iterator<Double> valueItr = dbServerUsage.values().iterator();
			while(itr.hasNext() && valueItr.hasNext()){
				String server = itr.next();
				Double validUsage = valueItr.next();
				if(validUsage.compareTo(0.0)>0){
					return server;
				}
				
			}
		}
		
		return null;
	}
	
	public static void increaseUsage(String serverName){
		Map<String,Double> dbServerUsage = ProxyRuntimeContext.getInstance().getDbServerUsage();
		synchronized (dbServerUsage) {
			Map<String,Double> temp = new HashMap<String, Double>();
			temp.putAll(dbServerUsage);
			double usage = temp.get(serverName)-1;
			temp.put(serverName, usage);
			//ValueComparator bvc =  new ValueComparator(temp);  
			dbServerUsage.clear();
			dbServerUsage.putAll(temp);
		}
	}
}
