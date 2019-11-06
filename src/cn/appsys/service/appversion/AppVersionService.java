package cn.appsys.service.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;
/**
 * 版本服务接口
 * @author Administrator
 *
 */
public interface AppVersionService {
	/**
	 * 根据条件获取列表,为空获取全部按时间降序
	 * @param appId
	 * @return
	 */
	public List<AppVersion> getAppVersionList(Integer id,Integer appId);
	
	/**
	 * 添加版本
	 * @param appVersion
	 * @return
	 */
	public int addVersion(AppVersion appVersion);
	
	/**
	 * 修改版本
	 * @param appVersion
	 * @return
	 */
	public int updateVersion(AppVersion appVersion);
	
	/**
	 * 删除appId的所有版本信息
	 * @param appId
	 * @return
	 */
	public int deleteVersionByAppId(Integer appId);
	
	/**
	 * 删除apk文件
	 * @param appId
	 * @return
	 */
	public int deleteApkFile(Integer id);
	
	/**
	 * 根据id获取appVersion
	 * @param appId
	 * @return
	 */
	public AppVersion getAppVersion(Integer vid);

}
