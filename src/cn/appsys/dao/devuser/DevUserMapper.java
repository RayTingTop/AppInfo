package cn.appsys.dao.devuser;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.DevUser;

public interface DevUserMapper {
	/**
	 * 通过userCode获取User
	 * 用于登录 和 开发者修改自身信息
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public DevUser getLoginUser(@Param("devCode")String devCode);
	
	/**
	 * 根据id查询用户信息
	 * @param id
	 * @return
	 */
	public DevUser getUserById(Integer id);
	
	/**
	 * 查询所有用户
	 * @return
	 */
	public List<DevUser> getUserList();
	
	/**
	 * 添加新用户,修改人和日期为空
	 * @param devUser
	 * @return
	 */
	public int addUser(DevUser devUser);
	
	/**
	 * 修改用户信息,创建人和创建时间不能修改
	 * @param devUser
	 * @return
	 */
	public int updateUser(DevUser devUser);
	
	/**
	 * 根据id删除用户
	 * @param id
	 * @return
	 */
	public int deleteUser(Integer id);
	
}
