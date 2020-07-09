package com.zhangzlyuyx.easy.storage;

import java.util.HashMap;
import java.util.Map;

import com.zhangzlyuyx.easy.storage.file.FileStorageEngine;
import com.zhangzlyuyx.easy.storage.gofastdfs.GoFastDFSStorageEngine;

/**
 * 存储工厂模式处理
 * @author zhangzlyuyx
 *
 */
public class StorageFactory {

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
	private final Map<StorageType, StorageEngine> storageEngineMap = new HashMap<StorageType, StorageEngine>();
	
	/**
	 * 获取存储引擎
	 * @param storageType 存储类型
	 * @param config 存储配置
	 * @return
	 */
	public synchronized StorageEngine getStorageEngine(StorageType storageType, Map<String, Object> config){
		//获取存储引擎缓存
		if(this.storageEngineMap.containsKey(storageType)){
			return this.storageEngineMap.get(storageType);
		}
		StorageEngine storageEngine = null;
		if(storageType.equals(StorageType.File)){
			storageEngine = new FileStorageEngine();//本地文件存储
		}else if(storageType.equals(StorageType.GoFastDFS)){
			storageEngine = new GoFastDFSStorageEngine();//go fastdfs存储
		}
		//缓存存储引擎
		if(storageEngine != null){
			//加载配置
			storageEngine.loadConfig(config);
			this.storageEngineMap.put(storageType, storageEngine);
		}
		return storageEngine;
	}
}
