package cn.appsys.service.appversion;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
/**
 * 版本服务实现类
 * @author Administrator
 *
 */
@Service
public class AppVersionServiceImpl implements AppVersionService {
	@Autowired
	private AppVersionMapper versionMapper;//注入dao
	@Autowired
	private AppInfoMapper infoMapper;
	
	@Override
	public List<AppVersion> getAppVersionList(Integer id,Integer appId) {
		return versionMapper.getAppVersionList(null,appId);
	}

	@Override
	public int addVersion(AppVersion version) {
		if (versionMapper.addVersion(version)>0) {//保存成功
			//获取最新添加的appversion的id
			List<AppVersion> versions = this.getAppVersionList(null,version.getAppId());
			version.setId(versions.get(0).getId());
			if (infoMapper.updateVersionId(version.getAppId(), version.getId())>0) {
				return 1;
			}
		}
		return 0;
	}

	@Override
	public int updateVersion(AppVersion appVersion) {
		// TODO Auto-generated method stub
		return versionMapper.updateVersion(appVersion);
	}

	@Override
	public int deleteVersionByAppId(Integer appId) {
		// TODO Auto-generated method stub
		return versionMapper.deleteVersionByAppId(appId);
	}

	@Override
	public int deleteApkFile(Integer id) {
		return versionMapper.deleteApkFile(id);
	}

	@Override
	public AppVersion getAppVersion(Integer vid) {
		// TODO Auto-generated method stub
		return versionMapper.getAppVersion(vid);
	}
}
