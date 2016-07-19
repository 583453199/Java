package com.zjj.shiro.resource;

public class NamespaceResource {
	private int id;
	private String namespace; // 比如 /admin/, /stu/

	public NamespaceResource(int id, String namespace) {
		this.id = id;
		this.namespace = namespace;
	}

	/**
	 * 如下情况才会匹配成功，requestURI以namespace开头
	 * 
	 * @param requestURI
	 * @return
	 */
	public boolean match(String requestURI) {
		if (requestURI.indexOf(namespace) >= 0) {
			return true;
		}
		return false;
	}

	public int getId() {
		return id;
	}
}
