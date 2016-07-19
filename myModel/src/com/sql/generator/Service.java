package com.sql.generator;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Service {
	private static final String _RN = "_rn_";
	public static final String KEY_TRANSFER_SERVICE = "service";
	public static final String KEY_TRANSFER_DESCRIPTION = "description";
	public static final String KEY_TRANSFER_CONSUMER_ID = "consumerId";
	public static final String KEY_TRANSFER_USE_TRANSACTION = "useTransaction";
	public static final String KEY_TRANSFER_SERVICE_DATA = "serviceData";
	public static final String KEY_TRANSFER_MERGE_RULE = "mergeRule";
	public static final String KEY_TRANSFER_AFFECTED_ROW = "_affectedRow";
	public static final String KEY_TRANSFER_USE_CUSTOM_RESULTTRANSFORMER = "useCustomResultTransformer";
	// 总记录数KEY
	public static final String TOTAL_NUM = "TOTAL_NUM";
	// 总记录数 callback 后缀
	public static final String KEY_COUNT_CALLBACK_POSIX = "_count";

	private List<Generatable<?>> generatableList;
	private String url;
	private boolean useTransaction = false;
	private boolean useCustomResultTransformer = false;
	private String description = "";
	private String consumerId;
	private List<MergeRule> ruleList = new LinkedList<MergeRule>();

	public Service() {
		generatableList = new LinkedList<Generatable<?>>();
	}

	public Service(List<Generatable<?>> generatableList) {
		this.generatableList = generatableList;
	}

	public Service(Generatable<?> generatable, Generatable<?>... otherGeneratables) {
		generatableList = new LinkedList<Generatable<?>>();
		generatableList.add(generatable);
		generatableList.addAll(Arrays.asList(otherGeneratables));
	}

	public Service(Generatable<?> generatable) {
		generatableList = new LinkedList<Generatable<?>>();
		generatableList.add(generatable);
	}

	public Service addGeneratable(Generatable<?> generatable) {
		generatableList.add(generatable);
		return this;
	}

	public Service setUrl(String url) {
		this.url = url;
		return this;
	}

	public String getUrl() {
		return url;
	}

	public Service useTransaction() {
		this.useTransaction = true;
		return this;
	}

	public Service useCustomResultTransformer() {
		this.useCustomResultTransformer = true;
		return this;
	}

	public Service setDescription(String description) {
		this.description = description;
		return this;
	}

	public String getConsumerId() {
		return consumerId;
	}

	public Service setConsumerId(String consumerId) {
		this.consumerId = consumerId;
		return this;
	}

	public Service addMergeRule(MergeRule mergeRule) {
		this.ruleList.add(mergeRule);
		return this;
	}

	public Map<String, Object> getRequestMap() {
		Map<String, Object> requestMap = new HashMap<String, Object>();
		Map<String, Object> serviceMap = new HashMap<String, Object>();
		serviceMap.put(KEY_TRANSFER_DESCRIPTION, this.description);
		serviceMap.put(KEY_TRANSFER_CONSUMER_ID, this.consumerId);
		serviceMap.put(KEY_TRANSFER_USE_TRANSACTION, this.useTransaction);
		serviceMap.put(KEY_TRANSFER_USE_CUSTOM_RESULTTRANSFORMER, this.useCustomResultTransformer);

		List<Generatable<?>> generatableList = this.generatableList;
		List<Command> commandList = new LinkedList<Command>();

		for (int i = 0; i < generatableList.size(); i++) {
			Generatable<?> generatable = generatableList.get(i);
			// 一般最外层query提供默认limit
			if (generatable.isProhibitDefaultLimit() == false && generatable instanceof Query) {
				generatable.setProvideDefaultLimit(true);
			}
			Command command = generatable.getCommand();
			if (generatable.getResultCallback() != null) {
				command.setResultCallback(generatable.getResultCallback());
			} else {
				command.setResultCallback(_RN + i);
			}
			// 更新结果集名称
			generatable.setResultCallback(command.getResultCallback());
			commandList.add(command);

			// 查询总记录数
			if (generatable instanceof Query) {
				Query query = (Query) generatable;
				if (query.isGenerateCountQuery()) {
					Command countCommand = query.getCountCommand();
					countCommand.setResultCallback(command.getResultCallback() + KEY_COUNT_CALLBACK_POSIX);
					commandList.add(countCommand);
				}
			}
		}
		serviceMap.put(KEY_TRANSFER_SERVICE_DATA, commandList);
		List<Map<String, Object>> resovedRuleList = new LinkedList<Map<String, Object>>();
		for (MergeRule rule : this.ruleList) {
			resovedRuleList.add(rule.getRuleMap());
		}
		serviceMap.put(KEY_TRANSFER_MERGE_RULE, resovedRuleList);

		requestMap.put(KEY_TRANSFER_SERVICE, serviceMap);
		return requestMap;
	}
}