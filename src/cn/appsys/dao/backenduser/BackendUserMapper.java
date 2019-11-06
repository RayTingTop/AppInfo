package cn.appsys.dao.backenduser;
import org.apache.ibatis.annotations.Param;
import cn.appsys.pojo.BackendUser;
/**
 * 后台管理员用户mapper
 * @author Administrator
 *
 */
public interface BackendUserMapper {

	/**
	 * 通过userCode获取User
	 * 用于登录
	 * @param userCode
	 * @return
	 * @throws Exception
	 */
	public BackendUser getLoginUser(@Param("userCode")String userCode)throws Exception;

}
