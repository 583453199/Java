package com.zjj.shiro.tag;

import org.apache.shiro.web.tags.PermissionTag;

public class HasResourceTag extends PermissionTag {
	private static final long serialVersionUID = -5504941052301440781L;
	private String value = null;
	private boolean reverse = false;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
		this.setName(value);
	}

	public boolean isReverse() {
		return reverse;
	}

	public void setReverse(boolean reverse) {
		this.reverse = reverse;
	}

	protected boolean showTagBody(String p) {
		boolean allow = isPermitted(p);
		return reverse ? !allow : allow;
	}

}
