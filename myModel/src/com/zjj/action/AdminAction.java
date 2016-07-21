package com.zjj.action;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import jxl.Workbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.StrutsStatics;
import org.apache.struts2.json.annotations.JSON;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.zjj.bean.ShiroResourceBean;
import com.zjj.bean.UserBean;
import com.zjj.service.NewUserService;
import com.zjj.service.ShiroResourceService;
import com.zjj.service.ShiroRoleService;
import com.zjj.service.ShiroService;
import com.zjj.service.UserService;
import com.zjj.shiro.resource.ResourceManage;
import com.zjj.util.MyMD5Util;
import com.zjj.util.SessionUtil;
import com.zjj.util.common.Page;
import com.zjj.util.export.ExportTool;

public class AdminAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7032861181936643243L;

	private UserService userService;
	
	private NewUserService newUserService;
	
	private ShiroResourceService resourceService;
	
	private ShiroRoleService roleService;
	
	private ShiroService shiroService;
	
	private Map<String, String> requestMap;
	
	private List<UserBean> userList;
	
	@SuppressWarnings("rawtypes")
	private List list;
	private Page page;
	private String id;
	private String msg;
	private String content;
	private boolean resultStatus;
	private String down;
	private String excelPath;
	private String path;
	private String exportFlag;
	
	/**
	 * 管理员后台首页
	 * 
	 * @return
	 */
	public String adminIndex() {
		System.out.println("AdminAction.adminIndex()");
//		Subject subject = SecurityUtils.getSubject();  
//		System.out.println("a1 = " + subject.isPermitted(new MenuButtonResource("a1")));
//		System.out.println("a2 = " + subject.isPermitted(new MenuButtonResource("a2")));
//		System.out.println("a3 = " + subject.isPermitted(new MenuButtonResource("a3")));
//		System.out.println("a4 = " + subject.isPermitted(new MenuButtonResource("a4")));
		return "index";
	}
	
	/**
	 * 用户新增
	 * 
	 * @return
	 */
	public String userAdd() {
		String account =  (String) requestMap.get("account");
		String userName = (String) requestMap.get("userName");
		String password = (String) requestMap.get("password");
		String age = (String) requestMap.get("age");
		String userType = (String) requestMap.get("userType");
		
		UserBean bean = new UserBean();
		bean.setAccount(account);
		bean.setUserName(userName);
		bean.setPassword(MyMD5Util.getStrToMD5(password));
		bean.setAge(Integer.parseInt(age));
		bean.setUserType(Integer.parseInt(userType));
		String userId = userService.addUser(bean);
		// msg : 0、成功 1、失败
		if (StringUtils.isNotBlank(userId)) {
			msg = "0";
		} else {
			msg = "1";
		}
		return "userAdd";
	}
	
	/**
	 * 用户管理
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String userManage() {
		if (MapUtils.isEmpty(requestMap)) {
			requestMap = new HashMap<String, String>();
		}
		if ("down".equals(down)) {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("METHOD_NAME", "queryUserList");
			paramsMap.put("EXPORT_METHOD_NAME", "exportUserManage");
			paramsMap.put("PARAMS", requestMap);
			paramsMap.put("RESULT_LIST_NAME", "LIST");
			paramsMap.put("LOGIN_USER_ID", SessionUtil.getUserSessionInfo("USER_ID"));
			paramsMap.put("EXPORT_TITLE_NAME", "用户列表");
			paramsMap.put("EXTRA", "");
			excelPath = ExportTool.exportPageByGroup(newUserService, AdminAction.class, paramsMap);
			path = "userManage";
			return "toExcle";
		}
		
		Map<String, Object> map = newUserService.queryUserList(requestMap, page);
		if (MapUtils.isNotEmpty(map)) {
			userList =  (List<UserBean>) map.get("LIST");
			page.setTotalNum(Integer.parseInt((String) map.get("TOTAL_NUM")));
		}
		return "userManage";
	}
	
	/**
	 * 导出用户列表
	 * 
	 * @param fos
	 * @param exportList
	 */
	public void exportUserManage(String fos, ArrayList<Map<String, Object>> exportList) {
		jxl.write.WritableWorkbook wwb;
		try {
			wwb = Workbook.createWorkbook(new File(fos));
			// 自定义的标签名称
			jxl.write.WritableSheet ws = wwb.createSheet("用户", 0);
			String[] columnTitle = { "用户ID", "账号", "姓名", "年龄", "用户类型" };

			// 设置表格的列标题
			for (int i = 0; i < columnTitle.length; i++) {
				ws.setColumnView(i, 27);
				ws.addCell(new jxl.write.Label(i, 0, columnTitle[i]));
			}

			int m = 1;
			for (Map<String, Object> map : exportList) {
				ws.addCell(new jxl.write.Label(0, m, map.get("USER_ID") + ""));
				ws.addCell(new jxl.write.Label(1, m, map.get("ACCOUNT") + ""));
				ws.addCell(new jxl.write.Label(2, m, map.get("USER_NAME") + ""));
				ws.addCell(new jxl.write.Label(2, m, map.get("AGE") + ""));
				ws.addCell(new jxl.write.Label(4, m, map.get("USER_TYPE") + ""));
				m++;
			}
			wwb.write();
			// 关闭Excel工作薄对象
			wwb.close();
		} catch (IOException e) {
			LOG.error("导出用户数据excel数据IO异常。", e);
		} catch (RowsExceededException e) {
			LOG.error("导出用户数据excel数据行溢出(达到行的最大限制)异常。", e);
		} catch (WriteException e) {
			LOG.error("导出用户数据excel写入数据异常。", e);
		}
	}

	/**
	 * 跳转到 - 用户角色 - 授权
	 * 
	 * @return
	 */
	public String toUserRoleEdit() {
		list = roleService.queryUserRole(id);
		return "userRoleEdit";
	}
	
	/**
	 * 用户角色  - 授权 - 提交
	 * 
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String userRoleSub() {
		Set<String> set = new HashSet<String>();
		if (StringUtils.isNotBlank(content)) {
			String[] array = content.split(",");
			for (int i = 0; i < array.length; i++) {
				array[i] = array[i].trim();
			}
			set = new HashSet(Arrays.asList(array));
		}

		List<String> updateList = new ArrayList<String>();
		List<String> addList = new ArrayList<String>();
		List<String> allList = new ArrayList<String>(set);
		List<Map<String, Object>> list = roleService.queryExistUserRole(id, null);
		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				String dbRoleId = String.valueOf(map.get("ROLE_ID"));
				if (set.contains(dbRoleId)) {
					updateList.add(dbRoleId);
				}
			}
			boolean hasAddList = allList.removeAll(updateList);
			if (hasAddList) {
				addList = allList;
			}
		} else {
			addList.addAll(set);
		}

		resultStatus = roleService.updateUserRole(updateList, addList, id);
		return "toUserManage";
	}
	
	/**
	 * 资源管理
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String resourceManage() {
		if (MapUtils.isEmpty(requestMap)) {
			requestMap = new HashMap<String, String>();
		}
		int totalNum = 0;
		Map<String, Object> map = resourceService.queryResourceList(requestMap, page);
		if (MapUtils.isNotEmpty(map)) {
			list = (List<ShiroResourceBean>) map.get("LIST");
			totalNum = Integer.parseInt((String) map.get("TOTAL_NUM"));
		}
		page.setTotalNum(totalNum);
		return "resourceManage";
	}
	
	/**
	 * 资源编辑 - 跳转
	 * 
	 * @return
	 */
	public String toResourceEdit() {
		List<Map<String, Object>> list = resourceService.queryResourceType(1, "0");
		if (CollectionUtils.isNotEmpty(list)) {
			try {
				content = JSONArray.fromObject(list).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "resourceEdit";
	}
	
	/**
	 * 角色管理
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public String roleManage() {
		if (MapUtils.isEmpty(requestMap)) {
			requestMap = new HashMap<String, String>();
		}
		int totalNum = 0;
		Map<String, Object> map = roleService.queryRoleList(requestMap, page);
		if (MapUtils.isNotEmpty(map)) {
			list = (List<ShiroResourceBean>) map.get("LIST");
			totalNum = Integer.parseInt((String)map.get("TOTAL_NUM"));
		}
		page.setTotalNum(totalNum);
		return "roleManage";
	}
	
	/**
	 * 角色资源  - 授权跳转
	 * 
	 * @return
	 */
	public String roleResourceEdit() {
		List<Map<String, Object>> list = resourceService.queryRoleResourceList(id);
		if (CollectionUtils.isNotEmpty(list)) {
			try {
				content = JSONArray.fromObject(list).toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "roleResourceEdit";
	}
	
	/**
	 * 角色资源 - 授权 - 提交
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void roleResourceSub() {
		HttpServletRequest request = (HttpServletRequest) ActionContext.getContext().get(StrutsStatics.HTTP_REQUEST);
		HttpServletResponse response = (HttpServletResponse) ActionContext.getContext()
				.get(StrutsStatics.HTTP_RESPONSE);
		response.setCharacterEncoding(request.getCharacterEncoding());
		String roleId = request.getParameter("role_id");
		String selectedIds = request.getParameter("selected_ids");

		Set<String> set = new HashSet<String>();
		if (StringUtils.isNotBlank(selectedIds)) {
			set = new HashSet(Arrays.asList(selectedIds.split(",")));
		}

		List<Map<String, Object>> list = resourceService.queryPermissionByRoleId(roleId);
		List<String> updateList = new ArrayList<String>();
		List<String> addList = new ArrayList<String>();
		List<String> allList = new ArrayList<String>(set);

		if (CollectionUtils.isNotEmpty(list)) {
			for (Map<String, Object> map : list) {
				String dbResourceId = String.valueOf(map.get("RESOURCE_ID"));
				if (set.contains(dbResourceId)) {
					updateList.add(dbResourceId);
				}
			}
			boolean hasAddList = allList.removeAll(updateList); 
			if (hasAddList) {
				addList = allList;
			}
		} else {
			addList.addAll(set);
		}

		boolean resultStatus = resourceService.updatePermissionInfo(updateList, addList, roleId);
		if (resultStatus) {
			// 重新加载角色与资源的访问关系
			ResourceManage.resourceIdRolesMap = shiroService.queryResourceAndRoleMap();
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("RESULT_CODE", resultStatus ? "0" : "1");
		JSONObject json = JSONObject.fromObject(map);
		try {
			response.getWriter().print(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@JSON(serialize = false)
	public NewUserService getNewUserService() {
		return newUserService;
	}

	public void setNewUserService(NewUserService newUserService) {
		this.newUserService = newUserService;
	}

	@JSON(serialize = false)
	public UserService getUserService() {
		return userService;
	}
	
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	@JSON(serialize = false)
	public ShiroResourceService getResourceService() {
		return resourceService;
	}

	public void setResourceService(ShiroResourceService resourceService) {
		this.resourceService = resourceService;
	}

	@JSON(serialize = false)
	public ShiroRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(ShiroRoleService roleService) {
		this.roleService = roleService;
	}
	
	@JSON(serialize = false)
	public ShiroService getShiroService() {
		return shiroService;
	}

	public void setShiroService(ShiroService shiroService) {
		this.shiroService = shiroService;
	}

	public Map<String, String> getRequestMap() {
		return requestMap;
	}

	public void setRequestMap(Map<String, String> requestMap) {
		this.requestMap = requestMap;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<UserBean> getUserList() {
		return userList;
	}

	public void setUserList(List<UserBean> userList) {
		this.userList = userList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	@SuppressWarnings("rawtypes")
	public List getList() {
		return list;
	}
	
	@SuppressWarnings("rawtypes")
	public void setList(List list) {
		this.list = list;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isResultStatus() {
		return resultStatus;
	}

	public void setResultStatus(boolean resultStatus) {
		this.resultStatus = resultStatus;
	}
	
	public String getDown() {
		return down;
	}

	public void setDown(String down) {
		this.down = down;
	}

	public String getExcelPath() {
		return excelPath;
	}

	public void setExcelPath(String excelPath) {
		this.excelPath = excelPath;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getExportFlag() {
		return exportFlag;
	}

	public void setExportFlag(String exportFlag) {
		this.exportFlag = exportFlag;
	}
	
}