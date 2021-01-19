package com.zhangzlyuyx.easy.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zhangzlyuyx.easy.core.util.ReflectUtils;

/**
 * 树结果
 * @author zhangzhyuyx
 *
 */
public class TreeResult implements ITreeResult, Serializable {

	private static final long serialVersionUID = 7191520978252287303L;
	
	private static final Logger log = LoggerFactory.getLogger(TreeResult.class);

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
	public static <T> List<ITreeResult> parse(ITreeResult nextTreeResult, List<T> list, String rootId, String idField, String nameField, String pidField){
		Class<?> clazz = nextTreeResult == null ? TreeResult.class : nextTreeResult.getClass();
		List<?> ls = parse((ITreeResult)nextTreeResult, list, rootId, idField, nameField, pidField, clazz);
		List<ITreeResult> array = new ArrayList<>();
		for(Object item :ls) {
			array.add((ITreeResult)item);
		}
		return array;
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
	@SuppressWarnings("unchecked")
	public static <T,L> List<L> parse(ITreeResult nextTreeResult, List<T> list, String rootId, String idField, String nameField, String pidField, Class<L> clazz) {
		
		List<L> treeList = new ArrayList<>();
		if(list == null) {
			return treeList;
		}
		
		//自动查找根节点id
		if(rootId == null) {
			String lastId = null;
			for(T item : list) {
				Object itemId = ReflectUtils.getFieldValue(item, idField);
				Object itemPid = ReflectUtils.getFieldValue(item, pidField);
				if(itemId == null || itemPid == null) {
					continue;
				}
				if(lastId == null || itemId.toString().equalsIgnoreCase(lastId)) {
					lastId = itemPid.toString();
				}
			}
			rootId = lastId;
		}
		
		for(T item : list) {
			Object itemPid = ReflectUtils.getFieldValue(item, pidField);
			if(itemPid == null || !itemPid.toString().equalsIgnoreCase(rootId)) {
				continue;
			}
			Object itemId = ReflectUtils.getFieldValue(item, idField);
			Object itemName = ReflectUtils.getFieldValue(item, nameField);
			
			ITreeResult newTreeResult = null;
			if(clazz != null) {
				try {
					newTreeResult = (ITreeResult)clazz.newInstance();
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
			if(newTreeResult == null) {
				newTreeResult = new TreeResult();
			}
			
			newTreeResult.setId(itemId.toString());
			newTreeResult.setName(itemName.toString());
			newTreeResult.setTag(item);
			newTreeResult.setPId(itemPid != null ? itemPid.toString() : null);
			
			//递归
			parse(newTreeResult, list, itemId.toString(), idField, nameField, pidField, clazz);
			
			treeList.add((L)newTreeResult);
		}
		
		if(nextTreeResult != null && treeList.size() > 0) {
			for(L l : treeList) {
				nextTreeResult.getChildren().add((ITreeResult)l);
			}
		}
		return treeList;
	}
}
