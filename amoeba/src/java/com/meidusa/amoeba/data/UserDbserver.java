/**
 * 
 */
package com.meidusa.amoeba.data;

import java.util.Map;

/**
 * @author CZX
 * @date 2015-7-17
 */
public class UserDbserver {
	private Long userId;
	private String dbserver;
	private String ipAddr;
	private Integer port;
	private String dbUser;
	private String dbPassword;
	private String schema;
	private String parent;
	private Integer userCount;
	private Integer maxCount;
	
	public UserDbserver(){
		
	}
	
	public UserDbserver(Map<String, Object> userDbserverMap){
		userId = (Long) userDbserverMap.get("userid");
		dbserver = (String) userDbserverMap.get("dbserver");
		ipAddr = (String) userDbserverMap.get("ipaddr");
		port = (Integer) userDbserverMap.get("port");
		dbUser = (String) userDbserverMap.get("dbuser");
		dbPassword = (String) userDbserverMap.get("dbpassword");
		schema = (String) userDbserverMap.get("schema");
		parent = (String) userDbserverMap.get("parent");
		userCount = (Integer) userDbserverMap.get("usercount");
		maxCount = (Integer) userDbserverMap.get("maxcount");
	}
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
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
