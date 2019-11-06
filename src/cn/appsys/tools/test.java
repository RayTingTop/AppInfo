package cn.appsys.tools;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.appsys.service.developer.DevUserService;
/**
 * 测试类
 * @author Administrator
 */

public class test {
	//测试删除
	@Test
	public void testDelDevUser(){
		ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext-mybatis.xml");
		DevUserService service =(DevUserService)context.getBean("devUserService");
		//System.out.println(service.getUserById(1).getDevCode());
		System.out.println(service.deleteUser(2));
		
	}
}
