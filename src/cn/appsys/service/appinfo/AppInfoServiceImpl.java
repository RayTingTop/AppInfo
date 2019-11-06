package cn.appsys.service.appinfo;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.appinfo.AppInfoMapper;
import cn.appsys.dao.appversion.AppVersionMapper;
import cn.appsys.pojo.AppInfo;
import cn.appsys.pojo.AppVersion;
@Service
public class AppInfoServiceImpl implements AppInfoService {
	@Autowired
	AppInfoMapper mapper;//appInfo的dao
	@Autowired
	AppVersionMapper versionMapper;//版本dao

	@Override
	public List<AppInfo> getAppInfoList(String querySoftwareName,
			Integer queryStatus, Integer flatformId,
			Integer queryCategoryLevel1, Integer queryCategoryLevel2,
			Integer queryCategoryLevel3,Integer devId) {
		return mapper.getAppInfoList(querySoftwareName,queryStatus,flatformId,
				queryCategoryLevel1,queryCategoryLevel2,queryCategoryLevel3,devId);
	}

	@Override
	public AppInfo getAppInfo(Integer id,String APKName) {
		return mapper.getAppInfo(id,APKName);
	}

	@Override
	public int addAppInfo(AppInfo appInfo) {
		return mapper.addAppInfo(appInfo);
	}

	@Override
	public int modifyAppInfo(AppInfo appInfo) {
		// TODO Auto-generated method stub
		return mapper.updateAppInfo(appInfo);
	}

	@Override
	public int updateVersionId(Integer id, Integer versionId) {
		return mapper.updateVersionId(id, versionId);
	}
	
	@Override
	public int deleteAppInfo(Integer appId) {
		int result = -1;
		boolean deleteAPK = true;//是否删除apk
		boolean deleteLogo =true;//是否删除图标
		AppInfo appInfo = mapper.getAppInfo(appId, null);//获取appinfo信息
		List<AppVersion> appVersionList =versionMapper.getAppVersionList(null, appId);//获取该app的版本列表
		
		if (appVersionList.size()>0) {//如果有版本信息,则先删除版本
			for (AppVersion appVersion : appVersionList) {
				//删除apk文件
				if (null != appVersion.getApkLocPath() && !appVersion.getApkLocPath().equals("")) {
					File file = new File(appVersion.getApkLocPath());
					if (file.exists()) {
						System.out.println("-删除apk文件...");
						deleteAPK = file.delete();
					}
				}
			}
			versionMapper.deleteVersionByAppId(appId);//执行删除appversion表中数据
		}
		//删除图标
		if (null != appInfo.getLogoLocPath() && !appInfo.getLogoLocPath().equals("")) {
			File file = new File(appInfo.getLogoLocPath());
			if (file.exists()) {
				System.out.println("-删除logo文件...");
				deleteLogo = file.delete();
			}
		}
		if (deleteAPK&&deleteLogo) {
			result = mapper.deleteAppInfo(appId);//执行删除appinfo表中数据
		}
		System.out.println("--deleteAPK["+deleteAPK+"],deleteLogo["+deleteLogo+"],result["+result+"]");
		return result;
	}

	@Override
	public int deleteAppLogo(Integer id) {
		return mapper.deleteAppLogo(id);
	}
	
	@Override
	public int updateSatus(AppInfo appInfo) throws Exception {
		int result =-1;//结果
		Integer operator = appInfo.getModifyBy();
		if (operator < 0 || appInfo.getId() < 0) {
			throw new Exception();
		}
		appInfo.setOffSaleDate(new Date(System.currentTimeMillis()));
		switch (appInfo.getStatus()) {//根据状态选择操作
			case 2: // 审核通过时，可上架
				appInfo.setStatus(4);//修改为上架
				result=mapper.updateAppInfo(appInfo);
				System.out.println("--执行上架id["+appInfo.getId()+"],执行结果["+result+"]");
				break;
			case 5:// 下架时，可上架
				appInfo.setStatus(4);//修改为上架
				result=mapper.updateAppInfo(appInfo);
				System.out.println("--执行上架id["+appInfo.getId()+"],执行结果["+result+"]");
				break;
			case 4:// 上架时，可下架
				appInfo.setStatus(5);//修改为下架
				result=mapper.updateAppInfo(appInfo);
				System.out.println("--执行下架id["+appInfo.getId()+"],执行结果["+result+"]");
				break;
			default:
				return -1;
		}
		return result;
	}


	@Override
	public AppInfo getAppInfoPlus(Integer aid) {
		// TODO Auto-generated method stub
		return mapper.getAppInfoPlus(aid);
	}

	@Override
	public int updateAppInfoStatus(Integer id, Integer status) {
		// TODO Auto-generated method stub
		return mapper.updateAppInfoStatus(id, status);
	}

}
