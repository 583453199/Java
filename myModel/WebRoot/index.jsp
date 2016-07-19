<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
<title>首页</title>
<link type="text/css" rel="stylesheet" href="css/common.css"/>
<link type="text/css" rel="stylesheet" href="css/index.css"/>

<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
</head>

<body>
	<div class="t_header">
        <div class="top_left">
           	<img src="images/logo.jpg" />
        </div>
        <div class="top_nav">
            <ul>
                <li><a href="#" class="btn1">菜单1</a></li>
                <li><a href="#" class="btn2">菜单2</a></li>
                <li><a href="#" class="btn1">菜单3</a></li>
                <li><a href="#" class="btn1">菜单4</a></li>
                <li><a href="#" class="btn1"><s:if test="#session.user.ACCOUNT != null && #session.user.ACCOUNT != ''"><s:property value="#session.user.ACCOUNT"/></s:if>用户</a></li>
            </ul>
        </div>
    </div>
    <div id="content">
        <!-- 广告页面及注册登录  begin -->
        <div>
            <img src="images/nav_1.jpg" height="450" style="width:100%"></img>
        </div>
        <div class="dl_div">
            <div class="dl_menu">
                <div class="dl_menu1 select_menu" onclick="switchMenu(1)">注册</div>
                <div class="dl_menu2" onclick="switchMenu(2)">登录</div>
            </div>
            <div class="dl_content">
            	<form action="user/register.action" id="form1" method="post">
	                <span class="dl_content_s">注册即可尊享75元5节课特惠</span>
	                <span class="dl_input">
	                    <label class="ml_10">账号：</label><input type="text" name="requestMap.account" class="input01 mt_10"/>
	                </span>
	                <span class="dl_input">
	                    <label class="ml_10">姓名：</label><input type="text" name="requestMap.userName" class="input01 mt_10"/>
	                </span>
	                <span class="dl_input">
	                    <label class="ml_10">密码：</label><input type="password" name="requestMap.password" class="input01 mt_10"/>
	                </span>
	                <span class="dl_input">
	                    <label class="ml_10">年龄：</label><input type="text" name="requestMap.age" class="input01 mt_10"/>
	                </span>
	                <span>
	                    <a href="javascript:;" class="dl_sub_btn ml_30 mt_20" onclick="register()">立即注册</a>
	                </span>
                </form>
            </div>
            <div class="dl_user" style="display: none;">
            	<form action="user/login.action" id="form2" method="post">
            		<span class="dl_input">
	                    <label class="ml_10">账号：</label><input type="text" id="login_account" name="account" class="input01 mt_10" value="admin"/>
	                </span>
	                <span class="dl_input">
	                    <label class="ml_10">密码：</label><input type="password" id="login_password" name="password" class="input01 mt_10" value="123456"/>
	                </span>
	                <span>
	                    <a href="javascript:;" class="dl_sub_btn ml_30 mt_20" onclick="login()">登录</a>
	                </span>
            	</form>
            </div>
        </div>
        <!-- 广告页面及注册登录  end -->

        <!-- 内容主体 -->
        <div class="main_content">
            <div class="content1">
                <div class="content1_1">网站公告</div>
                <div class="content1_2">
                    <span class="ml_10"><img src="images/notice.png"></img></span>
                    <span><a href="#" style="text-decoration: none;">恭贺武汉徐东体验中心隆重开业！</a> (2016-04-29 17:46:45)</span>
                    <span class="c_more"><a href="">更多</a></span>
                </div>
            </div>

            <div class="content2">
                <div class="cont2_1">
                    <div class="cont2_1_1">
                        <span class="cont_span_pos">优秀老师展示<br/>Excellent Teachers</span>
                    </div>
                    <div class="cont2_1_2">
                        <span class="cont_span_pos">查看更多优秀老师</span>
                    </div>
                </div>
                <div class="cont2_2">
                    <img src="images/teacher1.jpg" width="250" height="300"/>
                    <img src="images/teacher1.jpg" width="250" height="300"/>
                    <img src="images/teacher1.jpg" width="250" height="300"/>
                </div>
            </div>

            <div class="content3 mt_10">
                <div>明星学员展示OUTSTANDING STUDENTS <span style="float: right;" class="mr_10">更多</span></div>

                <ul class="cont3_ul mt_20">
                    <li class="cont3_ul_li_l">
                    <div class="cont3_the_div">
                        <div class="stu_phone">
                            <img src="images/stu.jpg" width="140" height="140"/>
                        </div>
                        <div class="stu_title">
                            <h3 style="color:#0099cc;line-height: 50px;">2016长颈鹿英语全国讲故事比赛第三名</h3>
                            <p>
                                Jerry在说客一对一上外教课已经1年多，从最初紧张得不知道怎么开口到现在可以和老师简单的交流各类感兴趣的话题，
                                我们可以明显感到他在表达方面的进步！在2016年长颈鹿英语全国讲故事...
                            </p>
                        </div>
                    </div>
                </li>
                    <li class="cont3_ul_li_r">
                        <div class="cont3_the_div">
                            <div class="stu_phone">
                                <img src="images/stu.jpg" width="140" height="140"/>
                            </div>
                            <div class="stu_title">
                                <h3 style="color:#0099cc;line-height: 50px;">2016长颈鹿英语全国讲故事比赛第三名</h3>
                                <p>
                                    Jerry在说客一对一上外教课已经1年多，从最初紧张得不知道怎么开口到现在可以和老师简单的交流各类感兴趣的话题，
                                    我们可以明显感到他在表达方面的进步！在2016年长颈鹿英语全国讲故事...
                                </p>
                            </div>
                        </div>
                    </li>
                    <li class="cont3_ul_li_l">
                        <div class="cont3_the_div">
                            <div class="stu_phone">
                                <img src="images/stu.jpg" width="140" height="140"/>
                            </div>
                            <div class="stu_title">
                                <h3 style="color:#0099cc;line-height: 50px;">2016长颈鹿英语全国讲故事比赛第三名</h3>
                                <p>
                                    Jerry在说客一对一上外教课已经1年多，从最初紧张得不知道怎么开口到现在可以和老师简单的交流各类感兴趣的话题，
                                    我们可以明显感到他在表达方面的进步！在2016年长颈鹿英语全国讲故事...
                                </p>
                            </div>
                        </div>
                    </li>
                    <li class="cont3_ul_li_r">
                        <div class="cont3_the_div">
                            <div class="stu_phone">
                                <img src="images/stu.jpg" width="140" height="140"/>
                            </div>
                            <div class="stu_title">
                                <h3 style="color:#0099cc;line-height: 50px;">2016长颈鹿英语全国讲故事比赛第三名</h3>
                                <p>
                                    Jerry在说客一对一上外教课已经1年多，从最初紧张得不知道怎么开口到现在可以和老师简单的交流各类感兴趣的话题，
                                    我们可以明显感到他在表达方面的进步！在2016年长颈鹿英语全国讲故事...
                                </p>
                            </div>
                        </div>
                    </li>
                </ul>
            </div>

        </div>

    </div>

    <div class="footer">
        <div style="position: relative;top: 0px;left: 0px;">
            <ul class="yqlj_ul"><li>友情链接：</li><li>腾讯视频</li><li>爱奇艺</li><li>优酷</li></ul>

            <div style="width: 450px;position: absolute;left: 138px;top: 60px;color: #fff;">
                Copyright©zjj
            </div>
            <%--<div style="width: 450px;position: absolute;left:720px;top: 60px;color: #fff;">
                <ul class="yqlj_ul2"><li>腾讯视频</li><li>爱奇艺</li><li>优酷</li></ul>
            </div>
        --%>
        </div>
    </div>
    
    <script type="text/javascript">
    	function switchMenu(type) {
   			$(".dl_menu1").toggleClass("select_menu");
   			$(".dl_menu2").toggleClass("select_menu");
    		if (type == 1) {
    			$(".dl_user").hide();
    			$(".dl_content").show();
    		} else {
    			$(".dl_content").hide();
    			$(".dl_user").show();
    		}
    	}
    	
    	function register() {
    		$("#form1").submit();
    	}
    	
    	function login() {
    		var loginAccount = $("#login_account").val();
    		var loginPassword = $("#login_password").val();
    		if (!loginAccount) {
    			alert("请输入账号！");
    			return;
    		} else if (!loginPassword) {
    			alert("请输入密码！");
    			return;
    		}
    		$("#form2").submit();
    	}
    </script>
</body>
</html>
