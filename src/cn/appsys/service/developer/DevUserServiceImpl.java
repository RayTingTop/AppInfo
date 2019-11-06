package cn.appsys.service.developer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.devuser.DevUserMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.DevUser;
import cn.appsys.service.appinfo.AppInfoServiceImpl;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService {
	@Autowired
	private DevUserMapper mapper;
	
	@Autowired
	AppInfoServiceImpl appInfoService;
	
	@Override
	public DevUser login(String devCode, String devPassword) throws Exception {
		DevUser user = null;
		user = mapper.getLoginUser(devCode);
		//匹配密码
		if(null != user){
			if(!user.getDevPassword().equals(devPassword))
				user = null;
		}
		return user;
	}
	
	@Override
	public DevUser getUserById(Integer id) {
		// TODO Auto-generated method stub
		return mapper.getUserById(id);
	}
	@Override
	public List<DevUser> getUserList() {
		// TODO Auto-generated method stub
		return mapper.getUserList();
	}
	
	@Override
	public DevUser getDevUserByDevCode(String devCode) {
		return mapper.getLoginUser(devCode);
	}
	@Override
	public int addUser(DevUser devUser) {
		return mapper.addUser(devUser);
	}
	@Override
	public int updateUser(DevUser devUser) {
		return mapper.updateUser(devUser);
	}
	/**
	 * 根据id删除用户
	 * 此处删除deb_user表,appinfo表中信息和logo图片,version表中信息和本地apk文件
	 * 使用appInfoService进行删除app信息已经文件
	 */
	@Override
	public int deleteUser(Integer id) {
		//删除app成功result为正,app为空result为正,此时可以删除user
		//删除app失败result为负,不可继续删除user,并返回-1
		int result = -1;
		//根据id查询所有app
		List<AppInfo> apps = appInfoService.getAppInfoList(null, null, null, null, null, null, id);
		System.out.println("-删除开发者用户id["+id+"],该用户上传有app["+apps.size()+"]个");
		if (apps.size()>0) {//当前用户有关联app
			try {
				//循环删除所有
				for (int i = 0; i < apps.size(); i++) {
					AppInfo appinfo = apps.get(i);
					System.out.print("-正在删除id["+appinfo.getId()+"]的app,名称为["+appinfo.getSoftwareName()+"]");
					result = appInfoService.deleteAppInfo(appinfo.getId());//执行删除app,结果赋值
					System.out.println("-结果["+result+"]");
				}
			} catch (Exception e) {
				result = -1;
			}
		}else{
			result=1;//不执行删除app
		}
		if (result>0) {
			return mapper.deleteUser(id);//返回操作结果
		}
		return -1;//未执行了删除user,返回-1
	
	}
}
