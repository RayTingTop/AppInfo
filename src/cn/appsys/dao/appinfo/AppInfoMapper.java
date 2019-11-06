package cn.appsys.dao.appinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppInfo;
/**
 * appInfo dao接口
 * @author Administrator
 *
 */
public interface AppInfoMapper {
	/**
	 *更具条件 获取列表
	 * @param querySoftwareName:名称
	 * @param queryStatus:app状态编号
	 * @param queryFlatformId:查询的平台编号
	 * @param queryCategoryLevel1:一级分类
	 * @param queryCategoryLevel2:二级分类
	 * @param queryCategoryLevel3:三级分类
	 * @param devId:开发者编号
	 * @return
	 */
	public List<AppInfo> getAppInfoList(@Param(value="softwareName")String querySoftwareName,
			@Param(value="status")Integer queryStatus,@Param(value="flatformId")Integer queryFlatformId,
			@Param(value="queryCategoryLevel1")Integer queryCategoryLevel1,@Param(value="queryCategoryLevel2")Integer queryCategoryLevel2,
			@Param(value="queryCategoryLevel3")Integer queryCategoryLevel3,
			@Param(value="devId")Integer devId);
	
	/**
	 * 根据id或APK名称查询
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfo(@Param(value="id")Integer id,@Param(value="APKName")String APKName);
	
	/**
	 * 添加appinfo
	 * @return
	 */
	public int addAppInfo(AppInfo appInfo);
	
	/**
	 * 更新appinfo
	 * @return
	 */
	public int updateAppInfo(AppInfo appInfo);
	
	/**
	 * 根据Id，修改versionId
	 * @param Id 要修改的appid
	 * @param versionId 版本id
	 * @return
	 */
	public int updateVersionId(@Param(value="id")Integer id,@Param(value="versionId")Integer versionId);
	
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	public int deleteAppInfo(@Param(value="id")Integer id);
	
	/**
	 * 删除图标
	 * @return
	 */
	public int deleteAppLogo(@Param(value="id")Integer id);
	
	/**
	 * 根据id查询(详细)
	 * @param aid
	 * @return
	 */
	public AppInfo getAppInfoPlus(@Param(value="aid")Integer aid);
	
	/**
	 * 更新状态
	 * @param id 操作appid
	 * @param status 状态
	 * @return
	 */
	public int updateAppInfoStatus(@Param(value="id")Integer id,@Param(value="status")Integer status);
	
}
