package com.zjj.shiro.resource;

import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

/**
 * 
 * <pre>
 * <li> 去掉uri字段以节约空间
 * <li> ResourceManager使用Map<String, List<ActionURLResource>>直接取uri对应的ActionURLResource，以加快匹配速度
 * 
 */
public class ActionURLResource {
	private int id;
	private Set<String> paramSet; // 比如 [a=1, b=2]

	public ActionURLResource(int id) {
		this.id = id;
	}

	public ActionURLResource(int id, Set<String> paramSet) {
		this.id = id;
		this.paramSet = paramSet;
	}

	/**
	 * <pre>
	 * 如下情况才会匹配成功
	 * 
	 * &lt; li &gt; paramSet为空
	 * <li>paramSet存在 && paramSet 在 requestParamSet 中
	 * 
	 * @param requestParamSet
	 * @return
	 */
	public boolean match(Set<String> requestParamSet) {
		if (CollectionUtils.isEmpty(this.paramSet)) {
			return true;
		}
		// 开始匹配paramSet
		if (CollectionUtils.isEmpty(requestParamSet)) {
			return false;
		}
		return requestParamSet.containsAll(this.paramSet);
	}

	public int getId() {
		return id;
	}
}
