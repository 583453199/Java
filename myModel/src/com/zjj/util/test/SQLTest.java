package com.zjj.util.test;

import org.junit.Test;

import com.sql.generator.Command;
import com.sql.generator.Query;
import com.sql.generator.SQL;
import com.sql.generator.Service;



public class SQLTest {

	@SuppressWarnings("unused")
	@Test
	public void test1() {
		Query query = SQL.select("id,type,value,description,status");
		query.from("shiro_resource");
		query.where("status=", 0);
		query.and("type=", 1);
		query.limit(0, 10);
		query.setGenerateCountQuery(true);
		
		Service service = new Service(query); 
		

		Command command = query.getCommand();
		command.getStatement();
		System.out.println(command);
	}
}
