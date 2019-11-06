package cn.appsys.controller.backend;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.appsys.pojo.BackendUser;
import cn.appsys.service.backend.BackendUserService;
import cn.appsys.tools.Constants;

/**
 * 管理员用户登录控制器
 * 
 * @author Administrator
 * 
 */
@RequestMapping("manager")
@Controller
public class UserLoginController {
	private Logger logger = Logger.getLogger(UserLoginController.class);// 日志

	@Autowired
	private BackendUserService backendUserService;

	/**
	 * 跳转登录页面
	 * 
	 * @return
	 */
	@RequestMapping("login")
	public String login() {
		logger.debug("================UserLoginController跳转管理员登录界面===============");
		return "backendlogin";
	}

	/**
	 * 登录处理方法
	 * 
	 * @param userCode
	 * @param userPassword
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "dologin", method = RequestMethod.POST)
	public String doLogin(@RequestParam String userCode,
			@RequestParam String userPassword, HttpServletRequest request,
			HttpSession session) {
		logger.info("[UserLoginController]执行登录方法[doLogin()]=========="+userCode+"||"+userPassword+"");
		// 调用service方法，进行用户匹配
		BackendUser user = null;
		try {
			user = backendUserService.login(userCode, userPassword);// 登录方法
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null != user) {// 登录成功
			session.setAttribute(Constants.USER_SESSION, user);// 放入session
			return "redirect:/manager/backend/main";// 页面跳转（main.jsp）
		} else {
			// 页面跳转（login.jsp）带出提示信息--转发
			request.setAttribute("error", "用户名或密码不正确");// 错误信息
			return "backendlogin";// 重新登录
		}
	}

	/**
	 * 跳转首页方法
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping("/backend/main")
	public String main(HttpSession session) {
		if (session.getAttribute(Constants.USER_SESSION) == null) {// 通过常量判断用户已登录用户是否为空
			return "redirect:/manager/login";
		}
		return "backend/main";
	}

	/**
	 * 后台用户注销
	 * 
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String logout(HttpSession session) {
		logger.info("[UserLoginController]执行注销方法[logout()]====================================");
		session.removeAttribute(Constants.USER_SESSION);// 清除session
		return "backendlogin";
	}

}
