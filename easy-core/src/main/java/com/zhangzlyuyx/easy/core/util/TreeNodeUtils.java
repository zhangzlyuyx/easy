package com.zhangzlyuyx.easy.core.util;

import java.util.ArrayList;
import java.util.List;

import com.zhangzlyuyx.easy.core.ActionCallback;
import com.zhangzlyuyx.easy.core.ITreeNode;

/**
 * 树节点工具类
 *
 */
public class TreeNodeUtils {

	/**
	 * 树节点递归
	 * @param rooId 根节点id(可空)
	 * @param rootNodes 根节点
	 * @param callback 回调
	 */
	public static void recursion(String rootId, List<ITreeNode> rootNodes, ActionCallback<ITreeNode> callback) {
		if(rootNodes == null || rootNodes.size() == 0 || callback == null) {
			return;
		}
		if(rootId == null || rootId.length() == 0) {
			for(ITreeNode treeNode : rootNodes) {
				//触发回调
				callback.action(treeNode);
				//子节点递归
				if(treeNode.getTreeNodes() != null && treeNode.getTreeNodes().size() > 0) {
					recursion(rootId, treeNode.getTreeNodes(), callback);
				}
			}
		}else {
			for(ITreeNode treeNode : rootNodes) {
				//忽略不匹配的节点
				if(!rootId.equalsIgnoreCase(treeNode.getTreeParentId())) {
					continue;
				}
				//触发回调
				callback.action(treeNode);
				//子节点递归
				if(treeNode.getTreeNodes() != null && treeNode.getTreeNodes().size() > 0) {
					recursion(rootId, treeNode.getTreeNodes(), callback);
				}
				//列表重新递归
				if(treeNode.getTreeId() != null && treeNode.getTreeId().length() > 0 && !treeNode.getTreeId().equalsIgnoreCase(treeNode.getTreeParentId())) {
					recursion(treeNode.getTreeId(), rootNodes, callback);
				}
			}
		}
	}

	/**
	 * list 转换为树结构 
	 * @param nextTreeNode 下一个树节点(可空)
	 * @param list 数据集合
	 * @param rootId 根节点id
	 * @param idField id节点字段
	 * @param pidField 父id节点字段
	 * @param clazz 树节点类型
	 * @return
	 */
	public static <T,L> List<L> parse(ITreeNode nextTreeNode, List<T> list, String rootId, String idField, String pidField, Class<L> clazz) throws Exception {
		if(clazz == null) {
			throw new RuntimeException("TreeNode type Not Allow Null");
		}
		List<L> treeList = new ArrayList<>();
		if(list == null) {
			return treeList;
		}
		
		for(T item : list) {
			Object itemPid = ReflectUtils.getFieldValue(item, pidField);
			if(itemPid == null || !itemPid.toString().equalsIgnoreCase(rootId)) {
				continue;
			}
			Object itemId = ReflectUtils.getFieldValue(item, idField);
			ITreeNode newTreeResult = (ITreeNode)clazz.newInstance();
			newTreeResult.setTreeId(itemId.toString());
			newTreeResult.setTreeTag(item);
			newTreeResult.setTreeParentId(itemPid != null ? itemPid.toString() : null);
			
			parse(newTreeResult, list, itemId.toString(), idField, pidField, clazz);
			
			treeList.add((L)newTreeResult);
		}
		
		if(nextTreeNode != null && treeList.size() > 0) {
			for(L l : treeList) {
				if(nextTreeNode.getTreeNodes() == null) {
					nextTreeNode.setTreeNodes(new ArrayList<ITreeNode>());
				}
				nextTreeNode.getTreeNodes().add((ITreeNode)l);
			}
		}
		return treeList;
	}
}
