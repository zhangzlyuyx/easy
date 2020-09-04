package com.zhangzlyuyx.easy.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.Result;
import com.zhangzlyuyx.easy.storage.file.FileStorageEngine;
import com.zhangzlyuyx.easy.storage.gofastdfs.GoFastDFSStorageEngine;

/**
 * 存储工厂模式处理
 * @author zhangzlyuyx
 *
 */
public class StorageFactory {
	
	private static final Logger log = LoggerFactory.getLogger(StorageFactory.class);

	private static class StorageFactoryHolder{
		private static final StorageFactory INSTANCE  = new StorageFactory();
	}
	
	/**
	 * 获取实例
	 * @return
	 */
	public static final StorageFactory getInstance() {
		return StorageFactoryHolder.INSTANCE ;
	}
	
	/**
	 * 存储引擎缓存
	 */
	private final Map<StorageType, StorageEngine> storageEngineMap = new ConcurrentHashMap<>(StorageType.values().length);
	
	/**
	 * 获取存储引擎
	 * @param storageType 存储类型
	 * @param config 存储配置
	 * @return
	 */
	public synchronized StorageEngine getStorageEngine(StorageType storageType, Map<String, Object> config){
		//存储引擎
		StorageEngine storageEngine = null;
		//获取存储引擎缓存
		if(this.storageEngineMap.containsKey(storageType)){
			storageEngine = this.storageEngineMap.get(storageType);
		} else {
			if(storageType.equals(StorageType.File)){
				//本地文件存储
				storageEngine = new FileStorageEngine();
			}else if(storageType.equals(StorageType.GoFastDFS)){
				//go fastdfs存储
				storageEngine = new GoFastDFSStorageEngine();
			} else {
				//TODO:
			}
			//缓存存储引擎
			if(storageEngine != null) {
				this.storageEngineMap.put(storageType, storageEngine);
			}
		}
		//加载配置
		if(storageEngine != null && config != null && config.size() > 0){
			Result<String> ret = storageEngine.loadConfig(config);
			if(!ret.isSuccess()) {
				log.error(ret.getMsg());
			}
		}
		return storageEngine;
	}
}
