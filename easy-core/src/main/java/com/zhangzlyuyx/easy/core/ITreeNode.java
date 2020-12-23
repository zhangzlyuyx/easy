package com.zhangzlyuyx.easy.core;

import java.util.List;

public interface ITreeNode {

	String getTreeId();
	
	void setTreeId(String treeId);
	
	String getTreeParentId();
	
	void setTreeParentId(String parentId);
	
	List<ITreeNode> getTreeNodes();
	
	void setTreeNodes(List<ITreeNode> treeNodes);
	
	Object getTreeTag();
	
	void setTreeTag(Object treeTag);
}
