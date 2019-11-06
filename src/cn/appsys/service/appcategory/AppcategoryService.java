package cn.appsys.service.appcategory;

import java.util.List;

import cn.appsys.pojo.AppCategory;
/**
 * 服务接口
 * @author Administrator
 *
 */
public interface AppcategoryService {
	/**
	 * 根据父级id查询,为空则查询第一级
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId);
}
