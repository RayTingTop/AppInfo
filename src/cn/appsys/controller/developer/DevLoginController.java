package cn.appsys.controller.developer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;

@Controller
@RequestMapping("/dev")
public class DevLoginController {
	private Logger logger = Logger.getLogger(DevLoginController.class);
	@Autowired
	private DevUserService devUserService;
	/**
	 * 跳转登录页面
	 * @return
	 */
	@RequestMapping(value="/login")
	public String login(){
		logger.info("================DevLoginController跳转开发者登录界面===============");
		return "devlogin";
	}	
	/**
	 * 登录
	 * @param devCode
	 * @param devPassword
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value="/dologin",method=RequestMethod.POST)
	public String doLogin(@RequestParam String devCode, @RequestParam String devPassword,
					HttpServletRequest request,HttpSession session){
		logger.info("[DevLoginController]执行登录方法[doLogin()]====================================");
		//调用service方法，进行用户匹配
		DevUser user = null;
		try {
			user = devUserService.login(devCode,devPassword);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(null != user){//登录成功
			session.setAttribute("devUserSession", user);//放入session
			return "redirect:/dev/flatform/main";//页面跳转（main.jsp）
		}else{
			//页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");
			return "devlogin";
		}
	}
	
	/**
	 * 跳转首页方法
	 * @param session
	 * @return
	 */
	@RequestMapping("/flatform/main")
	public String main(HttpSession session){
		if(session.getAttribute("devUserSession") == null){//通过常量判断用户已登录用户是否为空
			return "redirect:/dev/login";
		}
		return "developer/main";
	}
	
	/**
	 * 普通用户注销
	 * @param session
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpSession session){
		logger.info("[DevLoginController]执行注销方法[logout()]====================================");
		session.removeAttribute("devUserSession");//清除session
		return "devlogin";
	}
}
