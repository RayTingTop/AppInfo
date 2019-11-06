package cn.appsys.service.developer;
import java.util.List;
import cn.appsys.pojo.DevUser;

public interface DevUserService {
	/**
	 * 用户登录
	 * @param devCode
	 * @param devPassword
	 * @return
	 */
	public DevUser login(String devCode,String devPassword) throws Exception;
	
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
	 * 根据编码查找用户
	 * @param devCode
	 * @return
	 */
	public DevUser getDevUserByDevCode(String devCode);
	
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
	 * 此处删除deb_user表,appinfo表中信息和logo图片,version表中信息和本地apk文件
	 * @param id
	 * @return
	 */
	public int deleteUser(Integer id);
}
