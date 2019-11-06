package cn.appsys.service.appcategory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.appsys.dao.appcategory.AppCategoryMapper;
import cn.appsys.pojo.AppCategory;
/**
 * 等级服务实现类
 * @author Administrator
 *
 */
@Service
public class AppcategoryServiceImpl implements AppcategoryService {
	@Autowired
	AppCategoryMapper mapper;
	@Override
	public List<AppCategory> getAppCategoryListByParentId(Integer parentId) {
		return mapper.getAppCategoryListByParentId(parentId);
	}

}
