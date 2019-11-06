package cn.appsys.service.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.appsys.dao.backenduser.BackendUserMapper;
import cn.appsys.pojo.BackendUser;

/**
 * 后台用户服务实现类
 * 
 * @author Administrator
 * 
 */
@Service
public class BackendUserServiceImpl implements BackendUserService {
	@Autowired
	private BackendUserMapper mapper;// dao接口

	@Override
	public BackendUser login(String userCode, String userPassword)
			throws Exception {
		BackendUser user = null;
		user = mapper.getLoginUser(userCode);// 个
		// 匹配密码
		if (null != user) {
			if (!user.getUserPassword().equals(userPassword))
				user = null;
		}
		// 密码和通过用户编码查找到的用户密码匹配时,返回查找所得用户
		return user;
	}
}
