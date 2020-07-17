package com.zhangzlyuyx.easy.shiro.redis;

import java.util.Set;

import redis.clients.jedis.Protocol;

/**
 * redisManager
 *
 */
public class RedisManager implements IRedisManager {

	private static final String DEFAULT_HOST = "127.0.0.1:6379";
	
	private String host = DEFAULT_HOST;
	
	// timeout for jedis try to connect to redis server, not expire time! In milliseconds
	private int timeout = Protocol.DEFAULT_TIMEOUT;
	
    // timeout for jedis try to read data from redis server
    private int soTimeout = Protocol.DEFAULT_TIMEOUT;

	private String password;

	private int database = Protocol.DEFAULT_DATABASE;
	
	private static final int DEFAULT_MAX_ATTEMPTS = 3;
	
	private int maxAttempts = DEFAULT_MAX_ATTEMPTS;
	
	private static final String DEFAULT_MASTER_NAME = "mymaster";
	
	private String masterName = null;
	
	/**
	 * redis 管理实现
	 */
	private IRedisManager redisManagerImpl;
	
	@Override
	public byte[] get(byte[] key) {
		return this.getRedisManagerImpl().get(key);
	}

	@Override
	public byte[] set(byte[] key, byte[] value, int expire) {
		return this.getRedisManagerImpl().set(key, value, expire);
	}

	@Override
	public void del(byte[] key) {
		this.getRedisManagerImpl().del(key);
	}

	@Override
	public Long dbSize(byte[] pattern) {
		return this.getRedisManagerImpl().dbSize(pattern);
	}

	@Override
	public Set<byte[]> keys(byte[] pattern) {
		return this.getRedisManagerImpl().keys(pattern);
	}

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	
    public int getSoTimeout() {
        return this.soTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getDatabase() {
		return this.database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}
	
    public int getMaxAttempts() {
        return this.maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
        this.maxAttempts = maxAttempts;
    }
    
	public String getMasterName() {
		return this.masterName;
	}

	public void setMasterName(String masterName) {
		this.masterName = masterName;
	}
	
	/**
	 * 是否存在多个服务器
	 * @return
	 */
	public boolean isMultipleHost() {
		if(this.host == null || this.host.length() == 0) {
			return false;
		}
		String[] array = this.host.split(",");
		return array.length > 1;
	}
	
	/**
	 * redisManagerImpl
	 * @return
	 */
	protected IRedisManager getRedisManagerImpl() {
		
		synchronized (this) {
			
			if(this.redisManagerImpl != null) {
				return this.redisManagerImpl;
			}
			
			if(this.isMultipleHost()) {
				//判断是否为哨兵模式
				if(this.masterName != null && this.masterName.length() > 0) {//哨兵模式
					RedisSentinelManager sentinelManager = new RedisSentinelManager();
					sentinelManager.setHost(this.host);
					sentinelManager.setTimeout(this.timeout);
					sentinelManager.setDatabase(this.database);
					sentinelManager.setSoTimeout(this.soTimeout);
					sentinelManager.setMasterName(this.masterName);
					if(this.password != null && this.password.length() > 0) {
						sentinelManager.setPassword(this.password);
					}
					this.redisManagerImpl = sentinelManager;
					
				} else {//集群模式
					RedisClusterManager redisCluster = new RedisClusterManager();
					redisCluster.setHost(this.host);
					redisCluster.setTimeout(this.timeout);
					redisCluster.setDatabase(this.database);
					redisCluster.setSoTimeout(this.soTimeout);
					redisCluster.setMaxAttempts(this.maxAttempts);
					if(this.password != null && this.password.length() > 0) {
						redisCluster.setPassword(this.password);
					}
					this.redisManagerImpl = redisCluster;
				}
				
			}else {
				RedisGeneralManager redisClient = new RedisGeneralManager();
				redisClient.setHost(this.host);
				redisClient.setTimeout(this.timeout);
				redisClient.setDatabase(this.database);
				if(this.password != null && this.password.length() > 0) {
					redisClient.setPassword(this.password);
				}
				this.redisManagerImpl = redisClient;
			}
			
			return this.redisManagerImpl;
		}
	}
}
