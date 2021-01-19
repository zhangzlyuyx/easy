package com.zhangzlyuyx.easy.core;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 * 
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 * 
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * 
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * @author zhangzlyuyx
 */
public class Snowflake {
	
	/** 默认开始时间戳(写代码时间) */
	private final static long DEFAULT_STARTTIMESTAMP = 1288834974657L;
	
	private final static long DEFAULT_STARTTIMESTAMP2020 = 1577808000000L;
	
	/** 默认机器标识占用的位数 */
	private final static long DEFAULT_WORKERIDBITS = 5L;
	
	/** 默认数据中心占用的位数 */
	private final static long DEFAULT_DATACENTERIDBITS = 5L;
	
	//private final long twepoch = 1288834974657L;
	/** twepoch */
	private long twepoch;
	//private final long workerIdBits = 5L;
	//private final long datacenterIdBits = 5L;
	/** 最大支持机器节点数0~31，一共32个 */
	private long maxWorkerId;
	/** 最大支持数据中心节点数0~31，一共32个 */
	private long maxDatacenterId;
	/** 序列号12位 */
	private final long sequenceBits = 12L;
	/** 4095 */
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);
	/** 机器节点左移12位 */
	private final long workerIdShift = sequenceBits;
	/** 数据中心节点左移17位 */
	private long datacenterIdShift;
	/** 时间毫秒数左移22位 */
	private long timestampLeftShift;
	
	private long workerId;
	private long datacenterId;
	private long sequence = 0L;
	private long lastTimestamp = -1L;

	/**
	 * 构造
	 * @param workerId 终端ID(0~31)
	 * @param datacenterId 数据中心ID(0~31)
	 */
	public Snowflake(long workerId, long datacenterId) {
		this(workerId, datacenterId, DEFAULT_WORKERIDBITS, DEFAULT_DATACENTERIDBITS, DEFAULT_STARTTIMESTAMP);
	}
	
	/**
	 * 构造
	 * 
	 * @param workerId 终端ID(0~31)
	 * @param datacenterId 数据中心ID(0~31)
	 * @param startTimestamp 开始时间戳(写代码时间)
	 */
	public Snowflake(long workerId, long datacenterId, long startTimestamp) {
		this(workerId, datacenterId, DEFAULT_WORKERIDBITS, DEFAULT_DATACENTERIDBITS, startTimestamp);
	}
	
	/**
	 * 构造
	 * 
	 * @param workerId 终端ID(0~31)
	 * @param datacenterId 数据中心ID(0~31)
	 * @param workerIdBits 机器标识占用的位数(默认5L)
	 * @param datacenterIdBits 数据中心占用的位数(默认5L)
	 * @param startTimestamp 开始时间戳(写代码时间)
	 */
	public Snowflake(long workerId, long datacenterId, long workerIdBits, long datacenterIdBits, long startTimestamp) {
		this.workerId = workerId;
		this.datacenterId = datacenterId;
		//this.workerIdBits = workerIdBits;
		//this.datacenterIdBits = datacenterIdBits;
		this.twepoch = startTimestamp;
		this.maxWorkerId = -1L ^ (-1L << workerIdBits);
		this.maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
		this.datacenterIdShift = sequenceBits + workerIdBits;
		this.timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException("worker Id can't be greater than " + maxWorkerId + " or less than 0");
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException("datacenter Id can't be greater than " + maxDatacenterId + " or less than 0");
		}
	}

	/**
	 * 下一个ID
	 * 
	 * @return ID
	 */
	public synchronized long nextId() {
		long timestamp = genTime();
		if (timestamp < lastTimestamp) {
			 //如果服务器时间有问题(时钟后退) 报错。
			throw new IllegalStateException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + "ms");
		}
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			if (sequence == 0) {
				timestamp = tilNextMillis(lastTimestamp);
			}
		} else {
			sequence = 0L;
		}

		lastTimestamp = timestamp;

		return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	}

	/**
	 * 循环等待下一个时间
	 * 
	 * @param lastTimestamp 上次记录的时间
	 * @return 下一个时间
	 */
	private long tilNextMillis(long lastTimestamp) {
		long timestamp = genTime();
		while (timestamp <= lastTimestamp) {
			timestamp = genTime();
		}
		return timestamp;
	}
	
	/**
	 * 生成时间戳
	 * @return 时间戳
	 */
	private long genTime() {
		return System.currentTimeMillis();
	}

	public static void main(String[] args) {
		Snowflake snowflake = new Snowflake(0, 0, 5, 5, DEFAULT_STARTTIMESTAMP2020);
		long times = 1000L;
		for(int i = 0; i < times; i++) {
			System.out.println(snowflake.nextId());
		}
	}
}
