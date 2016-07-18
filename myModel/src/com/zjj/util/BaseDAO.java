package com.zjj.util;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

public class BaseDAO {
		//驱动名称
		private static final String DRIVER="oracle.jdbc.driver.OracleDriver";
		private static final String URL="jdbc:oracle:thin:@localhost:1521:ORCL";
		private static final String UID="scott";
		private static final String PWD="tiger";
		public static Connection getConnection()
		{
			Connection con=null;
			try{
				Class.forName(DRIVER);
				con=DriverManager.getConnection(URL,UID,PWD);
			}catch(Exception e)
			{
				e.printStackTrace();
			}
			return con;
		}
		//带参的查询
		public static Result runSelectSql(String sql,Object[] params)
		{
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet res=null;
			Result result=null;
			try
			{
				//调用方法得到连接
				con=getConnection();
				ps=con.prepareStatement(sql);
				//循环设置参数
				for(int i=0;i<params.length;i++)
				{
					ps.setObject(i+1, params[i]);
				}					
				   //获取rusultset对象
					res=ps.executeQuery();
					//通过rusultSupport对象的toResult方法获取result对象
					result=ResultSupport.toResult(res);
			}catch(Exception e)
			{
				e.printStackTrace();
			}finally
			{
				try {
					res.close();
					ps.close();
					con.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return result;//返回结果
			
		}
		//没有参的查询方法
		public static Result runSelectSql(String sql)
		{
			Connection con=null;
			PreparedStatement ps=null;
			ResultSet res=null;
			Result result=null;
			try {
				con=getConnection();
				ps=con.prepareStatement(sql);
				res=ps.executeQuery();
				result=ResultSupport.toResult(res);
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
				try{
					res.close();
					ps.close();
					con.close();
				}catch(Exception e)
				{
					e.printStackTrace();
				}
			}
			return result;
		}
		//有参的增删改
		public static boolean UpdateSql(String sql,Object [] params)
		{
			Connection con=null;
			PreparedStatement ps=null;
			try
			{
				con=getConnection();
				ps=con.prepareStatement(sql);
				for(int i=0;i<params.length;i++)
				{
					ps.setObject(i+1, params[i]);
				}
					ps.executeUpdate();//调方法
					return true;
			}catch(Exception e)
				{
					e.printStackTrace();
					return false;
				}
				finally
				{
					try
					{
						ps.close();
						con.close();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		}
		
		//没有参的增删改
		public static boolean UpdateSql(String sql)
		{
			Connection con=null;
			PreparedStatement ps=null;
			try
			{
				con=getConnection();
				ps=con.prepareStatement(sql);
				ps.executeUpdate();//调方法
				return true;
			}catch(Exception e)
				{
					e.printStackTrace();
					return false;
				}
				finally
				{
					try
					{
						ps.close();
						con.close();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
		}
		//调用存储过程
		  //有参数的存储过程，有结果
		   public static boolean runProcSelectParameter(String procName,Object [] params)
		   {
			   Connection con=null;
			   CallableStatement cs=null;
			   try {
				con=getConnection();
				String proc="{call "+procName+"(";
				  for(int i=0;i<params.length;i++)
				  { proc+="?,";}
				  proc=proc.substring(0,proc.length()-1)+")}";
				  cs=con.prepareCall(proc);
				  for(int i=0;i<params.length;i++)
				  {
					  //是输入参数
					  if(params[i]!=null)
					  {
						  cs.setObject(i+1, params[i]);
					  }
					  //是输出参数,要注册
					  else
					  {
						  cs.registerOutParameter(i+1,java.sql.Types.VARCHAR);
					  }
				 }
				  //执行存储过程,但是没有把执行赋给输出参数；
				   cs.execute();
				   //result=ResultSupport.toResult(res);
		           for(int i=0;i<params.length;i++)
		           {
		        	   //说明是输出参数
		        	   if(params[i]==null)
		        	   {
		        		   params[i]=cs.getObject(i+1);
		        	   }
		           }
		           return true;
			   } 
			   catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 finally
			 {
				 try {
					cs.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 return false;
		   }
		   public static Result resultProc(String procName,Object [] params)
		   {
			   Connection con=null;
			   CallableStatement cs=null;
			   ResultSet res=null;
			   Result result=null;
			   try {
				con=getConnection();
				String proc="{call "+procName+"(";
				  for(int i=0;i<params.length;i++)
				  { proc+="?,";}
				  proc=proc.substring(0,proc.length()-1)+")}";
				  cs=con.prepareCall(proc);
				  for(int i=0;i<params.length;i++)
				  {
					  //是输入参数
					  if(params[i]!=null)
					  {
						  cs.setObject(i+1, params[i]);
					  }
					  //是输出参数,要注册
					  else
					  {
						  cs.registerOutParameter(i+1,java.sql.Types.VARCHAR);
					  }
				 }
				  //执行存储过程,但是没有把执行赋给输出参数；
				   res=cs.executeQuery();
				   result=ResultSupport.toResult(res);
		           for(int i=0;i<params.length;i++)
		           {
		        	   //说明是输出参数
		        	   if(params[i]==null)
		        	   {
		        		   params[i]=cs.getObject(i+1);
		        	   }
		           }
		           return result;
			   } 
			   catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			 }
			 finally
			 {
				 try {
					cs.close();
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 return result;
		   }
}
