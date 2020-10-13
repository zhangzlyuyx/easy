package com.zhangzlyuyx.easy.core;

import java.util.List;

public interface ITreeResult {

	String getId();
	
	void setId(String id);
	
	String getName();
	
	void setName(String name);
	
	String getPId();
	
	void setPId(String pid);
	
	Object getTag();
	
	void setTag(Object tag);
	
	List<ITreeResult> getChildren();
}
