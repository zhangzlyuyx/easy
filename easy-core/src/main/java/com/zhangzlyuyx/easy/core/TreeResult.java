package com.zhangzlyuyx.easy.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.zhangzlyuyx.easy.core.util.ReflectUtils;

/**
 * 树结果
 * @author zhangzhyuyx
 *
 */
public class TreeResult implements ITreeResult, Serializable {

	private static final long serialVersionUID = 7191520978252287303L;

	/**
	 * id
	 */
	private String id;
	
	@Override
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * 名称
	 */
	private String name;
	
	@Override
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 父id
	 */
	private String pId;
	
	@Override
	public String getPId() {
		return pId;
	}
	
	public void setPId(String pId) {
		this.pId = pId;
	}
	
	/**
	 * 树节点级别
	 */
	private Integer level;
	
	public Integer getLevel() {
		return level;
	}
	
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
	 * tag
	 */
	private Object tag;
	
	public Object getTag() {
		return tag;
	}
	
	public void setTag(Object tag) {
		this.tag = tag;
	}
	
	/**
	 * 子节点集合
	 */
	private List<ITreeResult> children;
	
	@Override
	public List<ITreeResult> getChildren() {
		if(this.children == null) {
			this.children = new ArrayList<>();
		}
		return children;
	}
	
	public void setChildren(List<ITreeResult> children) {
		this.children = children;
	}
	
	/**
	 * 树节点递归
	 * @param rooId 根节点id(可空)
	 * @param rootNodes 根节点
	 * @param callback 回调
	 */
	public static void recursion(String rootId, List<ITreeResult> rootNodes, ResultCallback<ITreeResult> callback) {
		if(rootNodes == null || rootNodes.size() == 0 || callback == null) {
			return;
		}
		if(rootId == null || rootId.length() == 0) {
			for(ITreeResult treeResult : rootNodes) {
				//触发回调
				callback.result(treeResult);
				//子节点递归
				if(treeResult.getChildren().size() > 0) {
					recursion(rootId, treeResult.getChildren(), callback);
				}
			}
		}else {
			for(ITreeResult treeResult : rootNodes) {
				//忽略不匹配的节点
				if(!rootId.equalsIgnoreCase(treeResult.getPId())) {
					continue;
				}
				//触发回调
				callback.result(treeResult);
				//子节点递归
				if(treeResult.getChildren().size() > 0) {
					recursion(rootId, treeResult.getChildren(), callback);
				}
				//列表重新递归
				if(treeResult.getId() != null && treeResult.getId().length() > 0 && !treeResult.getId().equalsIgnoreCase(treeResult.getPId())) {
					recursion(treeResult.getId(), rootNodes, callback);
				}
			}
		}
	}
	
	/**
	 * list 转换为树结构 
	 * @param nextTreeResult
	 * @param list
	 * @param rootId
	 * @param idField
	 * @param nameField
	 * @param pidField
	 * @return
	 */
	public static <T> List<TreeResult> parse(TreeResult nextTreeResult, List<T> list, String rootId, String idField, String nameField, String pidField){
		
		List<TreeResult> treeList = new ArrayList<>();
		
		for(T item : list) {
			Object itemPid = ReflectUtils.getFieldValue(item, pidField);
			if(itemPid == null || !itemPid.toString().equalsIgnoreCase(rootId)) {
				continue;
			}
			Object itemId = ReflectUtils.getFieldValue(item, idField);
			Object itemName = ReflectUtils.getFieldValue(item, nameField);
			
			TreeResult newTreeResult = new TreeResult();
			newTreeResult.setId(itemId.toString());
			newTreeResult.setName(itemName.toString());
			newTreeResult.setTag(item);
			
			parse(newTreeResult, list, itemId.toString(), idField, nameField, pidField);
			
			treeList.add(newTreeResult);
		}
		
		if(nextTreeResult != null) {
			nextTreeResult.getChildren().addAll(treeList);
		}
		return treeList;
	}
}
