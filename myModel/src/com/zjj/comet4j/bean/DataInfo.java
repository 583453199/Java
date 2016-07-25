package com.zjj.comet4j.bean;

public class DataInfo {

	private final String type = "dataInfo";
	private String connectorCount;
	
	public String getConnectorCount() {
		return connectorCount;
	}

	public void setConnectorCount(String connectorCount) {
		this.connectorCount = connectorCount;
	}

	public String getType() {
		return type;
	}
}
