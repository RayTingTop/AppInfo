package cn.appsys.controller.developer;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import cn.appsys.pojo.DevUser;
import cn.appsys.service.developer.DevUserService;
@Controller
@RequestMapping("/dev/user")
public class DevUserController {
	private Logger logger = Logger.getLogger(DevLoginController.class);
	@Autowired
	private DevUserService derUserService;
	
	/**
	 * 跳转注册页面
	 * @return
	 */
	@RequestMapping("/regist")
	public String toRegist(){
		logger.info("================DevUserController跳转开发者注册界面===============");
		return "developer/devuserregist";
	}	
	
	/**
	 * ajax判断用户名是否可用
	 * @return
	 */
	@RequestMapping("/checkdevCode.json")
	@ResponseBody
	public Object checkDevCode(String devCode){
		logger.info("[DevUserController]执行判断devCode是否可用[checkDevCode()],查询devCode["+devCode+"]");
		Map<String, String> resultMap = new HashMap<String, String>();
		if (devCode != null && ! devCode.equals("")) {
			if (derUserService.getDevUserByDevCode(devCode)!=null) {//没有找到
				resultMap.put("devCode", "exist");
			}else{
				resultMap.put("devCode", "noexist");
			}
		}else{
			resultMap.put("devCode", "empty");
		}
		return JSON.toJSONString(resultMap);
	}

	/**
	 * 执行添加
	 * @param devUser
	 * @return
	 */
	@RequestMapping("/devuserregistsave")
	public String devUserRegistSave(DevUser devUser,Model model){
		logger.info("[DevUserController]执行注册用户[devUserRegistSave()],devCode["+devUser.getDevCode()+"]");
		int result = -1;
		devUser.setCreationDate(new Date());
		try {
			result=derUserService.addUser(devUser);
		} catch (Exception e) {
			model.addAttribute("fileUploadError", "系统出错,保存失败!");
			return "developer/devuserregist";
		}
		logger.info("--执行注册结果["+result+"]");
		return "redirect:/dev/login";
	}

	/**
	 * 跳转修改
	 * @param session
	 * @return
	 */
	@RequestMapping("/goUpdateDevUser")
	public String goUpdateDevUser(){
		return "developer/devuserupdate";
	}

	@RequestMapping("/devuserupdatesave")
	public String devUserUpdateSave(DevUser devUser,Model model,HttpSession session){
		logger.info("[DevUserController]执行注册用户[devUserRegistSave()],id["+devUser.getId()+"],devCode["+devUser.getDevCode()+"]");
		int result = -1;
		devUser.setModifyBy(devUser.getId());//修改人,用户修改只能修改自己信息
		devUser.setModifyDate(new Date());//修改日期
		try {
			result=derUserService.updateUser(devUser);
		} catch (Exception e) {
			model.addAttribute("fileUploadError", "系统出错,修改失败!");
		}
		if (result>0) {
			model.addAttribute("fileUploadError", "修改信息成功!");
		}else{
			model.addAttribute("fileUploadError", "修改信息失败!");
		}
		session.setAttribute("devUserSession", devUser);//保存至会话
		logger.info("--执行修改结果["+result+"]");
		return "developer/devuserupdate";
	}
}
