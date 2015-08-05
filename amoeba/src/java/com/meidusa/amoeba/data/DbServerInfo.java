/**
 * 
 */
package com.meidusa.amoeba.data;

import java.util.Map;


/**
 * @author CZX
 * @date 2015-7-14
 */
public class DbServerInfo {
	private String dbserver;
	private String ipAddr;
	private Integer port;
	private String dbUser;
	private String dbPassword;
	private String schema;
	private String parent;
	private Integer userCount;
	private Integer maxCount;
	
	public DbServerInfo(){
		
	}
	
	public DbServerInfo(Map<String, Object> dbserverInfoMap){
		dbserver = (String) dbserverInfoMap.get("dbserver");
		ipAddr = (String) dbserverInfoMap.get("ipaddr");
		port = (Integer) dbserverInfoMap.get("port");
		dbUser = (String) dbserverInfoMap.get("dbuser");
		dbPassword = (String) dbserverInfoMap.get("dbpassword");
		schema = (String) dbserverInfoMap.get("schema");
		parent = (String) dbserverInfoMap.get("parent");
		userCount = (Integer) dbserverInfoMap.get("usercount");
		maxCount = (Integer) dbserverInfoMap.get("maxcount");
	}
	
	public String getDbserver() {
		return dbserver;
	}
	public void setDbserver(String dbserver) {
		this.dbserver = dbserver;
	}
	public String getIpAddr() {
		return ipAddr;
	}
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getDbUser() {
		return dbUser;
	}
	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}
	public String getDbPassword() {
		return dbPassword;
	}
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public Integer getUserCount() {
		return userCount;
	}
	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}
	public Integer getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}
}
