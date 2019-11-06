package cn.appsys.service.backend;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.BackendUser;
import cn.appsys.pojo.DataDictionary;
/**
 * 后台用户服务接口
 * @author LinWen:1324281458@QQ.com
 *
 */
public interface BackendUserService {
	/**
	 * 用户登录
	 * @param userCode
	 * @param userPassword
	 * @return BackendUser
	 */
	public BackendUser login(String userCode,String userPassword) throws Exception;
	
}
