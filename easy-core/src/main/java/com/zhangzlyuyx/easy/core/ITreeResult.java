package com.zhangzlyuyx.easy.core;

import java.util.List;

public interface ITreeResult {

	String getId();
	
	String getName();
	
	String getPId();
	
	List<ITreeResult> getChildren();
}
