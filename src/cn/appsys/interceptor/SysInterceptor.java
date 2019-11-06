package cn.appsys.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DevUser;
import cn.appsys.tools.Constants;
/**
 * 拦截器类
 * @author Administrator
 *
 */
public class SysInterceptor extends HandlerInterceptorAdapter {
	private Logger logger = Logger.getLogger(SysInterceptor.class);
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		logger.info("拦截器[SysInterceptor]拦截请求[preHandle()] ==========================");
		HttpSession session = request.getSession();
		
		BackendUser backendUser = (BackendUser)session.getAttribute(Constants.USER_SESSION);
		DevUser devUser = (DevUser)session.getAttribute(Constants.DEV_USER_SESSION);
		
		if(null != devUser){ //用户登陆了,放过请求
			return true;
		}else if(null != backendUser){ //管理员登陆了,放过请求
			return true;
		}else{
			response.sendRedirect(request.getContextPath()+"/403.jsp");//拦截请求跳转至403.jap
			return false;
		}
		
	}
}
