package cn.appsys.service.appinfo;

import java.util.List;

import cn.appsys.pojo.AppInfo;
/**
 * appinfo服务接口
 * @author Administrator
 *
 */
public interface AppInfoService {
	/**
	 * 根据条件 获取列表
	 * 开发者用户查询根据开发者编号区分不同开发者的列表
	 * admin传入开发者编号为null查询全部appinfo
	 * @param querySoftwareName:名称
	 * @param queryStatus:app状态编号
	 * @param queryFlatformId:查询的平台编号
	 * @param queryCategoryLevel1:一级分类
	 * @param queryCategoryLevel2:二级分类
	 * @param queryCategoryLevel3:三级分类
	 * @param devId:开发者编号
	 * @return
	 */
	List<AppInfo> getAppInfoList(String querySoftwareName,Integer queryStatus,Integer flatformId,
			Integer queryCategoryLevel1,Integer queryCategoryLevel2, Integer queryCategoryLevel3,
			Integer devId );
	
	/**
	 * 根据id或者APK名称查询
	 * @param APKName
	 * @return
	 */
	public AppInfo getAppInfo(Integer id,String APKName);
	
	/**
	 * 添加appinfo
	 * @param appInfo
	 * @return
	 */
	public int addAppInfo( AppInfo appInfo);
	
	/**
	 * 修改appinfo
	 * @param appInfo
	 * @return
	 */
	public int modifyAppInfo( AppInfo appInfo);
	
	/**
	/**
	 * 根据Id，修改versionId
	 * @param Id 要修改的appid
	 * @param versionId 版本id
	 * @return
	 */
	public int updateVersionId(Integer id,Integer versionId);
	
	/**
	 * 根据id删除
	 * 若version表中有信息则先删除version表中所有版本
	 * 要删除 图标 和 apk文件
	 * @param id
	 * @return
	 */
	public int deleteAppInfo(Integer id);
	
	/**
	 * 删除图标
	 * @return
	 */
	public int deleteAppLogo(Integer id);
	
	/**
	 * 上下架操作
	 * 状态审核通过(2),下架(5)时可以上架
	 * 状态上架(5)时可下架
	 * @param appInfo
	 * @return
	 */
	public int updateSatus(AppInfo appInfo) throws Exception ;
	
	/**
	 * 根据id更新status
	 * @param id
	 * @param status
	 * @return
	 */
	public int updateAppInfoStatus(Integer id,Integer status);
	
	/**
	 * 根据id查询(详细)
	 * @param aid
	 * @return
	 */
	public AppInfo getAppInfoPlus(Integer aid);
	
}
