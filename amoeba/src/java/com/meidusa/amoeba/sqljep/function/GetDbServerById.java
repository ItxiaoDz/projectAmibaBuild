package com.meidusa.amoeba.sqljep.function;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.meidusa.amoeba.context.ProxyRuntimeContext;
import com.meidusa.amoeba.data.DbServerInfo;
import com.meidusa.amoeba.data.UserDbserver;
import com.meidusa.amoeba.net.poolable.ObjectPool;
import com.meidusa.amoeba.sqljep.ASTFunNode;
import com.meidusa.amoeba.sqljep.JepRuntime;
import com.meidusa.amoeba.sqljep.ParseException;
import com.meidusa.amoeba.util.DbServerUtil;
import com.meidusa.amoeba.util.redis.RedisAPI;

/**
 * 
 * @author struct
 *
 */
public class GetDbServerById extends PostfixCommand {
	private static Logger logger           = Logger.getLogger(GetDbServerById.class);
	private String        poolName;
	private String        sql;
	private String 		  dbInfoTab;
	private String 		  insertUserDb;
	
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public void setSql(String sql) {
        this.sql = sql;
    }
	
	public void setDbInfoTab(String dbInfoTab) {
		this.dbInfoTab = dbInfoTab;
	}
	public void setInsertUserDb(String insertUserDb) {
		this.insertUserDb = insertUserDb;
	}
	final public int getNumberOfParameters() {
		return 1;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime) throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}
	
	private Map<String, Object> query(Comparable<?>[] parameters,String querySql) {
        ObjectPool pool = ProxyRuntimeContext.getInstance().getPoolMap().get(poolName);
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            Map<String, Object> columnMap = null;
            conn = (Connection) pool.borrowObject();
            st = conn.prepareStatement(querySql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i] instanceof Comparative) {
                        st.setObject(i + 1, ((Comparative) parameters[i]).getValue());
                    } else {
                        st.setObject(i + 1, parameters[i]);
                    }
                }
            }

            rs = st.executeQuery();
            if (rs.next()) {
                columnMap = new HashMap<String, Object>();
                ResultSetMetaData metaData = rs.getMetaData();
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    String columnName = null;
                    String label = metaData.getColumnLabel(i);
                    if (label != null) {
                        columnName = label.toLowerCase();
                    } else {
                        columnName = metaData.getColumnName(i).toLowerCase();
                    }
                    Object columnValue = rs.getObject(i);
                    columnMap.put(columnName, columnValue);
                    if (logger.isDebugEnabled()) {
                        logger.debug("[columnName]:" + columnName + " [columnValue]:" + columnValue + " [args]:" + Arrays.toString(parameters));
                    }
                }
            } else {
                logger.error("no result!sql:[" + querySql + "], args:" + Arrays.toString(parameters));
            }
            return columnMap;
        } catch (Exception e) {
            logger.error("execute sql error :" + querySql, e);
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e1) {
                }
            }

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e1) {
                }
            }

            if (conn != null) {
                try {
                    pool.returnObject(conn);
                } catch (Exception e) {
                }
            }
        }
    }
	
	private int insert(Comparable<?>[] parameters,String querySql) {
        ObjectPool pool = ProxyRuntimeContext.getInstance().getPoolMap().get(poolName);
        Connection conn = null;
        PreparedStatement st = null;
//        ResultSet rs = null;

        try {
            Map<String, Object> columnMap = null;
            conn = (Connection) pool.borrowObject();
            st = conn.prepareStatement(querySql);
            if (parameters != null) {
                for (int i = 0; i < parameters.length; i++) {
                    if (parameters[i] instanceof Comparative) {
                        st.setObject(i + 1, ((Comparative) parameters[i]).getValue());
                    } else {
                        st.setObject(i + 1, parameters[i]);
                    }
                }
            }

            int rs = st.executeUpdate();
            
            return rs;
        } catch (Exception e) {
            logger.error("execute sql error :" + querySql, e);
            return -1;
        } finally {

            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e1) {
                }
            }

            if (conn != null) {
                try {
                    pool.returnObject(conn);
                } catch (Exception e) {
                }
            }
        }
    }
	
	public Comparable<?> getDbserverById(Comparable<?>  param) throws ParseException {
		if (param == null) {
			return null;
		}
		//long queryStart = new Date().getTime();
		Long userId = Long.valueOf(param.toString());
		UserDbserver userDbserver = RedisAPI.getUserDbserver(userId);
		DbServerInfo dbServerInfo = null;
		if(null == userDbserver){
			Comparable<?>[] params = new Comparable<?>[]{param};
			Map<String,Object> serverResult = query(params,sql);
			if(null == serverResult){
				dbServerInfo = null;
			}else{
				dbServerInfo = new DbServerInfo(serverResult);
				userDbserver = new UserDbserver();
				userDbserver.setUserId(userId);
				userDbserver.setDbserver(dbServerInfo.getDbserver());
				userDbserver.setIpAddr(dbServerInfo.getIpAddr());
				userDbserver.setPort(dbServerInfo.getPort());
				userDbserver.setDbUser(dbServerInfo.getDbUser());
				userDbserver.setDbPassword(dbServerInfo.getDbPassword());
				userDbserver.setSchema(dbServerInfo.getSchema());
				userDbserver.setParent(dbServerInfo.getParent());
				userDbserver.setUserCount(dbServerInfo.getUserCount());
				userDbserver.setMaxCount(dbServerInfo.getMaxCount());
				RedisAPI.setUserDbserver(userDbserver);
			}
			
		}
		//Long queryEnd = new Date().getTime();
		//System.out.println("查询对照关系耗时："+(queryEnd-queryStart));
		
		//新增对照关系
		if(userDbserver==null && dbServerInfo ==null){
			String minUsageSever = DbServerUtil.selectDbserver();
			Comparable<?>[] params = new Comparable<?>[]{param,minUsageSever};
			long starTime = new Date().getTime();
			//插入对照关系
			insert(params,insertUserDb);
			long endTime = new Date().getTime();
			//System.out.println("插入对照关系耗时"+(endTime-starTime));
			params = new Comparable<?>[]{minUsageSever};
			String updateUserCount = "update "+dbInfoTab+" set userCount=userCount+1 where dbserver=?";
			starTime = new Date().getTime();
			//更新使用用户数
			insert(params,updateUserCount);
			endTime = new Date().getTime();
			//System.out.println("修改服务器使用情况耗时"+(endTime-starTime));
			DbServerUtil.increaseUsage(minUsageSever);
			starTime = new Date().getTime();
			//从redis中获取数据库信息
			dbServerInfo = RedisAPI.getDbServerInfo(minUsageSever);
			//如果redis中没有，则去数据库查询
			if(null == dbServerInfo){
				String dbInfoSql = "select * from "+dbInfoTab+" where dbserver = ?";
				params = new Comparable<?>[]{minUsageSever};
				Map<String,Object> serverResult = query(params,dbInfoSql);
				if(serverResult!=null){
					//写入redis中
					dbServerInfo = new DbServerInfo(serverResult);
					RedisAPI.setDbserverInfo(dbServerInfo);
				}
				
			}
			userDbserver = new UserDbserver();
			userDbserver.setUserId(userId);
			userDbserver.setDbserver(dbServerInfo.getDbserver());
			userDbserver.setIpAddr(dbServerInfo.getIpAddr());
			userDbserver.setPort(dbServerInfo.getPort());
			userDbserver.setDbUser(dbServerInfo.getDbUser());
			userDbserver.setDbPassword(dbServerInfo.getDbPassword());
			userDbserver.setSchema(dbServerInfo.getSchema());
			userDbserver.setParent(dbServerInfo.getParent());
			userDbserver.setUserCount(dbServerInfo.getUserCount());
			userDbserver.setMaxCount(dbServerInfo.getMaxCount());
			//将对照关系写入redis
			RedisAPI.setUserDbserver(userDbserver);
			endTime = new Date().getTime();
			//System.out.println("获取服务器信息耗时"+(endTime-starTime));
		}
		
		String dbserver = userDbserver.getDbserver();
		
		//System.out.println("dbserver="+dbserver);
		if(DbServerUtil.isExists(dbserver)){
			return dbserver;
		}else{
			String ipAddr = userDbserver.getIpAddr();
			int port = 0;
			if(null != userDbserver.getPort()){
				port = userDbserver.getPort();
			}
			String dbUser = userDbserver.getDbUser();
			String dbPassword = userDbserver.getDbPassword();
			String schema = userDbserver.getSchema();
			String parent = userDbserver.getParent();
			boolean flag = DbServerUtil.createDbServer(dbserver, ipAddr, port, dbUser, dbPassword, schema, parent);
			if(flag){
				return dbserver;
			}else {
				return null;
			}
		}
		
	}

	public Comparable<?> getResult(Comparable<?>... comparables)
			throws ParseException {
		//System.out.println("使用GetDbServerById");
		return getDbserverById(comparables[0]);
	}
	
	
	/*public static void main(String[] args){
		String dd = "624265432";
		    int off = 0;
		    char val[] = dd.toCharArray();
		    int len = val.length;
		    int h = 0;
            for (int i = 0; i < len; i++) {
                h = 31*h + val[off++];
            }
		
		try {
			System.out.println(hash((dd)));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}*/
}

