package com.zhangzlyuyx.easy.shiro.redis;

import java.util.Set;

import redis.clients.jedis.JedisPoolConfig;
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
	
	/** 资源池中的最大连接数 */
	private Integer maxTotal = 0;
	
	public Integer getMaxTotal() {
		return maxTotal;
	}
	
	public void setMaxTotal(Integer maxTotal) {
		this.maxTotal = maxTotal;
	}
	
	/** 资源池允许的最大空闲连接数 */
	private Integer maxIdle = 0;
	
	public Integer getMaxIdle() {
		return maxIdle;
	}
	
	public void setMaxIdle(Integer maxIdle) {
		this.maxIdle = maxIdle;
	}
	
	/** 资源池确保的最少空闲连接数 */
	private Integer minIdle = 0;
	
	public Integer getMinIdle() {
		return minIdle;
	}
	
	public void setMinIdle(Integer minIdle) {
		this.minIdle = minIdle;
	}
	
	/** 向资源池借用连接时是否做连接有效性检测（ping）。检测到的无效连接将会被移除。 */
	private Boolean testOnBorrow;
	
	public Boolean getTestOnBorrow() {
		return testOnBorrow;
	}
	
	public void setTestOnBorrow(Boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}
	
	/** 向资源池归还连接时是否做连接有效性检测（ping）。检测到无效连接将会被移除。 */
	private Boolean testOnReturn;
	
	public Boolean getTestOnReturn() {
		return testOnReturn;
	}
	
	public void setTestOnReturn(Boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}
	
	/** 是否开启空闲资源检测。 */
	private Boolean testWhileIdle;
	
	public Boolean getTestWhileIdle() {
		return testWhileIdle;
	}
	
	public void setTestWhileIdle(Boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
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
			
			//当前jedis连接池配置
			JedisPoolConfig jedisPoolConfig = null;
			if(this.isMultipleHost()) {
				//判断是否为哨兵模式
				if(this.masterName != null && this.masterName.length() > 0) {
					//哨兵模式
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
					jedisPoolConfig = sentinelManager.getJedisPoolConfig();
				} else {
					//集群模式
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
					jedisPoolConfig = redisCluster.getJedisPoolConfig();
				}
				
			}else {
				//单机模式
				RedisGeneralManager redisClient = new RedisGeneralManager();
				redisClient.setHost(this.host);
				redisClient.setTimeout(this.timeout);
				redisClient.setDatabase(this.database);
				if(this.password != null && this.password.length() > 0) {
					redisClient.setPassword(this.password);
				}
				this.redisManagerImpl = redisClient;
				jedisPoolConfig = redisClient.getJedisPoolConfig();
			}
			
			//jedis连接池统一配置
			if(jedisPoolConfig != null) {
				if(this.getMaxTotal() != null) {
					jedisPoolConfig.setMaxTotal(this.getMaxTotal().intValue());
				}
				if(this.getMaxIdle() != null) {
					jedisPoolConfig.setMaxIdle(this.getMaxIdle().intValue());
				}
				if(this.getMinIdle() != null) {
					jedisPoolConfig.setMinIdle(this.getMinIdle().intValue());
				}
				if(this.getTestOnBorrow() != null) {
					jedisPoolConfig.setTestOnBorrow(this.getTestOnBorrow().booleanValue());
				}
				if(this.getTestOnReturn() != null) {
					jedisPoolConfig.setTestOnReturn(this.getTestOnReturn().booleanValue());
				}
				if(this.getTestWhileIdle() != null) {
					jedisPoolConfig.setTestWhileIdle(this.getTestWhileIdle().booleanValue());
				}
			}
			return this.redisManagerImpl;
		}
	}
	
	/**
	 * 获取 jedis连接池配置
	 * @return
	 */
	public JedisPoolConfig getJedisPoolConfig() {
		IRedisManager redisManager = this.getRedisManagerImpl();
		if(redisManager instanceof RedisGeneralManager) {
			RedisGeneralManager redisGeneralManager = (RedisGeneralManager)redisManager;
			return redisGeneralManager.getJedisPoolConfig();
		} else if(redisManager instanceof RedisClusterManager) {
			RedisClusterManager redisClusterManager = (RedisClusterManager)redisManager;
			return redisClusterManager.getJedisPoolConfig();
		} else if(redisManager instanceof RedisSentinelManager) {
			RedisSentinelManager redisSentinelManager = (RedisSentinelManager)redisManager;
			return redisSentinelManager.getJedisPoolConfig();
		} else {
			throw new RuntimeException("Not Found JedisPoolConfig!");
		}
	}
}
