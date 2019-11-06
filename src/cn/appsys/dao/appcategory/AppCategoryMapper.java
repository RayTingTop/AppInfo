package cn.appsys.dao.appcategory;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.appsys.pojo.AppCategory;
/**
 * app等级dao接口
 * @author Administrator
 *
 */
public interface AppCategoryMapper {
	/**
	 * 根据父级id查询,为空则查询第一级
	 * @param parentId
	 * @return
	 * @throws Exception
	 */
	public List<AppCategory> getAppCategoryListByParentId(@Param(value="parentId") Integer parentId);
}
