package cn.appsys.dao.appversion;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppVersion;
/**
 * app版本信息dao接口
 * @author Administrator
 *
 */
public interface AppVersionMapper {
	/**
	 * 根据条件获取列表
	 * @param appId
	 * @return
	 */
	public List<AppVersion> getAppVersionList(@Param("id")Integer id, @Param("appId")Integer appId);
	
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
	public int deleteVersionByAppId(@Param("appId")Integer appId);
	
	/**
	 * 删除apk文件
	 * @param appId
	 * @return
	 */
	public int deleteApkFile(@Param("id")Integer id);
	
	/**
	 * 根据id获取appVersion
	 * @param vid
	 * @return
	 */
	public AppVersion getAppVersion(@Param("vid")Integer vid);
}
