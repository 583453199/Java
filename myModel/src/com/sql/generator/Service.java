package com.sql.generator;


import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * a) 传递给bee端的数据格式
 * {
 *     service={
 *     	   consumerId=talk915Web
 *         description=,
 *         useTransaction=false,
 *         useCustomResultTransformer=false,
 *         serviceData=[
 *             Command{
 *                 statement=inset into user(name) VALUES(|\1|),
 *                 params=['Jack'],
 *                 resultCallback="a8d0953a-cf34-4b7d-be47-2a5d82ae5350"
 *             },
 *             Command{
 *                 statement=select score, user_id from user where user_id = |\1|,
 *                 params=[
 *                     ResultColumnValue{
 *                         resultCallback: a8d0953a-cf34-4b7d-be47-2a5d82ae5350,
 *                         columnName: _lastInsertId
 *                     }
 *                 ],
 *                 resultCallback=_rn_1
 *             },
 *             Command{
 *                 statement=select count(*) from user where user_id = |\1|,
 *                 params=[
 *                     ResultColumnValue{
 *                         resultCallback: a8d0953a-cf34-4b7d-be47-2a5d82ae5350,
 *                         columnName: _lastInsertId
 *                     }
 *                 ],
 *                 resultCallback=_rn_1_count
 *             },
 *             Command{
 *                 statement=select book_id, book_name, creator from book where book_id = |\1|,
 *                 params=[
 *                     1
 *                 ],
 *                 resultCallback=_rn_2
 *             },
 *             Command{
 *                 statement=select book_id, lesson_name from lesson where book_id in |\1|,
 *                 params=[
 *                     ResultColumnValue{
 *                         resultCallback: _rn_2,
 *                         columnName: book_id
 *                     }
 *                 ],
 *                 resultCallback=mergedLesson
 *             },
 *             Command{
 *                 statement=select creator as createUser from user where user_id in |\1|,
 *                 params=[
 *                     ResultColumnValue{
 *                         resultCallback: _rn_2,
 *                         columnName: creator,
 *                         defaultValue: -99
 *                     }
 *                 ],
 *                 resultCallback=_rn_4
 *             }
 *         ],
 *         mergeRule=[
 *             {merge=mergedLesson, into=_rn_2, mergeCol=book_id, intoCol=book_id, oneToMany=true},
 *             {merge=_rn_4, into=_rn_2, mergeCol=createUser, intoCol=creator, oneToMany=false}
 *         ]
 *     }
 * }
 *     
 * b) bee端返回的数据格式
 * {
 *     SYS_HEAD={
 *         RET_STATUS=F  # or RET_STATUS=S
 *     },
 *     a8d0953a-cf34-4b7d-be47-2a5d82ae5350={
 *         _lastInsertId=339,
 *         _affectedRow=1
 *     },
 *     _rn_1=[
 *         {score:30, user_id:1}
 *     ],
 *     _rn_1_count=[
 *         {TOTAL_NUM: 1}
 *     ],
 *     _rn_2=[
 *         {book_id:1, book_name:新概念英语, creator:管理员
 *          mergedLesson:[{book_id:1, lesson_name:hi},{book_id:1, lesson_name:bye}],
 *          createUser:1
 *         }
 *     ]
 * }
 * 
 * c) 通过框架自动生成的
 * </pre>
 * 
 */
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